package com.example.startevent

import android.Manifest.permission.CAMERA
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.startevent.databinding.ActivityCameraBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {


    companion object{
        private val REQUIRED_PERMISSIONS= arrayOf(Manifest.permission.CAMERA)
        private val REQUEST_CODE_PERMISSIONS=10
    }
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
        dateRun= bundle?.getString(dateRun).toString()
        usuario=bundle?.getString(usuario).toString()


        /**
         * Configuramos la carpeta en la que queremos que se guarden las fotos
         */

        outputDirectory = getOutputDirectory()
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
         * onClick listener para ver en qué orientación esta la cámara para cambiarla (Frontal/Trasera)
         * Comprobamos dónde está mirando y si pulsamos el botón, cambiamos la visión
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

    private fun bindCamera(){

    }
    private fun startCamera(){

    }
    /**
     * Función que hará la foto
     */
    private fun takePhoto(){

    }
}