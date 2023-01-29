package com.example.startevent

import android.Manifest.permission.CAMERA
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.databinding.ActivityCameraBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.lang.Integer.max
import java.lang.Math.min
import kotlin.math.abs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {


    companion object{
        private val REQUIRED_PERMISSIONS= arrayOf(Manifest.permission.CAMERA)
        private val REQUEST_CODE_PERMISSIONS=10



        private const val RATIO_4_3_VALUE = 4.0/3.0
        private const val RATIO_16_9_VALUE = 16.0/9.0

    }
    private var FILENAME : String = ""
    /**
     * Creamos esta variable para hacer referencia a la actividad a la que queremos hacer binding
     */

    lateinit var binding : ActivityCameraBinding


    private var preview: Preview? = null

    /**
     * indicamos qué lente debe estar activa si la trasera o la frontal
     */
    //Cámara trasera por defecto
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    /**
     * Proveedor de la cámara
     */
    private var cameraProvider: ProcessCameraProvider?=null

    /**
     * la imagen que estamos capturando en si
     */
    private var imageCapture: ImageCapture?=null

    /**
     * Si lo vamos a guardar, determinamos el directorio en el que se va a guardar
     */
    private lateinit var outputDirectory: File

    /**
     * El ejecutor de la cámara, con el que ejecutaremos los servicios de la cámara
     */

    private lateinit var cameraExecutor:ExecutorService

    /**
     * Cómo guardar las fotos, para que no puedan guardarse con dos nombres iguales
     */
    private lateinit var dateRun : String
    private lateinit var usuario : String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializamos la variable, y que se coja de la actividad que necesitamos, le hacemos inflate,
        binding = ActivityCameraBinding.inflate(layoutInflater)

        //Aquí en vez de cargar el layout le decimos que cargue el binding donde tenemos la información
        //Hay que tener en encuenta que si tus ids tienen guiones bajos (boton_rojo2) lo transforma a botonRojo2, eliminando las barras bajas
        setContentView(binding.root)

        val bundle = intent.extras
        dateRun= bundle?.getString("dateRun").toString()
        usuario=bundle?.getString("usuario").toString()



        /**
         * Hilo independiente para manejar la cámara mientras la aplicación sigue funcionando
         */
        cameraExecutor = Executors.newSingleThreadExecutor()

        /**
         * ponemos onclick listener para el botón de hacer la foto
         */
        binding.cameraCapturaButton.setOnClickListener{
            takePhoto()
        }

        /**
         * Configuramos la carpeta en la que queremos que se guarden las fotos
         */

        outputDirectory = getOutputDirectory()
        /**
         * onClick listener para ver en qué orientación esta la cámara para cambiarla (Frontal/Trasera)
         * Comprobamos dónde está mirando y si pulsamos el botón, cambiamos la visión
         * En esta función no se podrá ejecutar la cámara, pues el botón solo estará visible cuando ya se haya ejecutado
         */
        binding.cameraSwitchButton.setOnClickListener{
            lensFacing = if(CameraSelector.LENS_FACING_FRONT == lensFacing){
                CameraSelector.LENS_FACING_BACK
            }else{
                CameraSelector.LENS_FACING_FRONT
            }

            bindCamera()
        }


        /**
         * Comprobamos si están los permisos concedidos, sino los pediría, y nos traerá un código de vuelta
         * En este punto será el único en el que se ejecutará la cámara, llamando a la función  startCamera()
         */
        if(allPermissionsGranted()) startCamera()
        else ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Ha vuelto de la ventana de permisos de la cámara y comprobamos si el código con el que vuelve es el mismo que el que lo valida (REQUEST_CODE_PERMISSIONS)
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            //En caso de que estén aprobados, se inicia la cámara
            if(allPermissionsGranted()) startCamera()
            else{
                Toast.makeText(this,"¡Debes proporcionar permisos si quieres tomar fotos!",Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {

        ContextCompat.checkSelfPermission(baseContext,it)==PackageManager.PERMISSION_GRANTED
    }




    /**
     * Función para vincular la cámara con las especificaciones correctas
     * aspectratio  y rotation  que hemos construido previamente
     */
    private fun bindCamera(){

        val metrics = DisplayMetrics().also { binding.viewFinder.display.getMetrics(it) }
        val screenAspectRatio= aspectRatio(metrics.widthPixels,metrics.heightPixels)
        val rotation=binding.viewFinder.display.rotation

        val cameraProvider=cameraProvider?: throw java.lang.IllegalStateException("Fallo al inciar la cámara")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture=ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        //Liberamos la cámara por si hubiese alguna cámara activa

        cameraProvider.unbindAll()
        //Hacemos un try-catch por si surgiese algún error al vincular la cámara, como si el dispositivo no tuviese cámara
        try{
        cameraProvider.bindToLifecycle(this,cameraSelector,imageCapture,preview)
            //Indicamos que preview se corresponde con el viewfinder
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }catch(exc: Exception){
            Log.e("CameraStartEvent","Fallo al vincular la cámara",exc)
        }
    }

    /**
     * Función que nos permite sacar el ratio dependiendo si el dispositivo está en vertical/horizontal
     * para ello dividimos el más grande entre el más pequeño.
     */
    private fun aspectRatio(width:Int,height:Int):Int{

        val previewRatio=max(width,height).toDouble() / min(width,height)

        if(abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio- RATIO_16_9_VALUE)){
            return AspectRatio.RATIO_4_3
        }
            return AspectRatio.RATIO_16_9

    }

    /**
     * En esta función(padre) vincularemos la aplicación con el hardware del dispositivo
     * llamando a la función binding
     */
    private fun startCamera(){
        val cameraProviderFinnaly= ProcessCameraProvider.getInstance(this)

        cameraProviderFinnaly.addListener( {
            cameraProvider = cameraProviderFinnaly.get()
            //Reconocemos cuántas cámaras hay disponibles
            lensFacing = when{
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw java.lang.IllegalStateException("No hay cámaras disponibles")
            }

            manageSwitchButton()
            //Llamamos a la función que construye toda la camera
            bindCamera()

        },ContextCompat.getMainExecutor(this))


    }

    /**
     * Funcion que devuelve un boolean y comprueba si tiene cámara trasera
     */
    private fun hasBackCamera(): Boolean{
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)?: false
    }

    /**
     * Función que devuelve un boolean y comprueba si el dispositivo tiene cámara delantera
     */

    private fun hasFrontCamera(): Boolean{
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)?: false
    }


    private fun manageSwitchButton(){
        val switchButton = binding.cameraSwitchButton

        //Hacemos Trycatch para manejar el botón de switch, en caso de que alguna de las camaras no esté disponible en el try comprobamos que ambas cámaras están disponibles.
        // En el catch lanzaremos una excepción. Y desactivaremos el botón

        try{
            switchButton.isEnabled = hasBackCamera() && hasFrontCamera()
        }catch (exc: CameraInfoUnavailableException){
            switchButton.isEnabled=false
        }

    }

    /**
     * Función para almacenar las posibles fotos que hagamos,indicamos el nombre de la carpeta donde queremos que se guarde
     * en el firstOrNull preguntamos si existe, en caso de que exista guardará en la carpeta de la aplicación
     * sino crearemos un nuevo directorio con apply
     */

    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            File(it,"StartEvent").apply { mkdirs() }
        }
        return if (mediaDir !=null && mediaDir.exists()) mediaDir else filesDir
    }
    /**
     * Función que hará la foto, y le daremos un nombre de archivo, que será el momento en el que se toma la foto y el usuario que la toma
     * sustituimos carácteres como ":" y "/" por cadena vacía para que el nombre del archivo no sea tan extenso.
     * también le añadimos la extensión .jpg, para que sea guardada en ese formato
     */
    private fun takePhoto(){
        FILENAME = getString(R.string.app_name)+ usermail + dateRun
        FILENAME = FILENAME.replace(":","")
        FILENAME = FILENAME.replace("/","")

        val photoFile=File(outputDirectory,FILENAME+".jpg")
        //Opciones de salida, que será igual a la imagen que estamos capturando
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        //Capturamos la imagen
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    //Actualizamos la galeria de las imagenes, usamos el "uri", identificador único de archivos

                    val savedUri = Uri.fromFile(photoFile)
                    //Comprobamos la SDK del terminal y actualizamos la galeria
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setGalleryThumbnail (savedUri)
                    }
                    //Referenciamos el archivo que queremos actualizar
                    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                    //Escaneamos el archivo que queremos encontrar
                    MediaScannerConnection.scanFile(baseContext, arrayOf(savedUri.toFile().absolutePath),
                    arrayOf(mimeType)
                    ){ _,uri->

                    }
                    var clMain = findViewById<ConstraintLayout>(R.id.clMain)
                    Snackbar.make(clMain, "Imagen guardada con éxito", Snackbar.LENGTH_LONG).setAction("OK"){
                        clMain.setBackgroundColor(Color.CYAN)
                    }.show()

                }
                //EN caso de que haya un error al hacer la captura
                override fun onError(exception: ImageCaptureException) {
                    var clMain = findViewById<ConstraintLayout>(R.id.clMain)
                    Snackbar.make(clMain, "Error al guardar la imagen", Snackbar.LENGTH_LONG).setAction("OK"){
                        clMain.setBackgroundColor(Color.CYAN)
                    }.show()
                }
            })
    }

    /**
     * Función que se encarga de cargar la miniatura
     */
    private fun setGalleryThumbnail(uri: Uri){

        var thumbnail = binding.photoViewButton
        thumbnail.post{
            Glide.with(thumbnail).load(uri).apply(RequestOptions.circleCropTransform()).into(thumbnail)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
                }


