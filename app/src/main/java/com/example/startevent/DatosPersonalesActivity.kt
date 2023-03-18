package com.example.startevent

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import clases.ActividadMadre
import clases.UsuarioActual.usuario
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.ProfileActivity.Companion.upImage
import com.example.startevent.databinding.ActivityDatosPersonalesBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import emergentes.Alerta
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class DatosPersonalesActivity : ActividadMadre() {

    lateinit var binding: ActivityDatosPersonalesBinding
    private val lanzadorElegirImagen = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> subirImagenFirebase(uri) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_personales)


        // Inflamos el layout usando view binding
        binding = ActivityDatosPersonalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargamos la imagen de perfil del usuario desde Firebase Storage
        Glide.with(this)
            .load(upImage)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.fotoPerfil)
        Log.d("DatosPersonalesActivity", "Imagen subida con éxito a Firebase Storage.")
        Log.d("DatosPersonalesActivity", "URL de descarga de la imagen: $upImage")

        val auth = FirebaseFirestore.getInstance()
        val userRef = auth.collection("users").document(usermail)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val foto_perfil = documentSnapshot.getString("foto_perfil")
                    Glide.with(this).load(foto_perfil).into(binding.fotoPerfil)
                } else {
                    Toast.makeText(this, "El usuario no existe en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al obtener datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        Glide.with(this).load(usuarioLogado?.foto_perfil).into(binding.fotoPerfil)
        /**
         * Aquí establecemos los hints con los datos que recogemos de la bbdd y hemos asignado a usuarioLogado
         * Pero para ello debemos comprobar primero si el campo es nulo, porque sino por defecto aparecería vacío
         * si el usuario aún no ha rellenado los datos.
         */


        // Asignamos los hints a los campos de texto con los datos del usuario, si están disponibles
        binding.tvNacionalidad.text= getString(R.string.nacionalidad)+" "+usuarioLogado?.nacionalidad ?: getString(R.string.nacionalidad)
        binding.tvGenero.text=getString(R.string.genero)+" "+usuarioLogado?.genero ?: getString(R.string.genero)
        binding.etNombre.hint = usuarioLogado?.nombre ?: getString(R.string.nombre)
        binding.etApellidos.hint = usuarioLogado?.apellidos ?: getString(R.string.apellidos)
        binding.etDNI.hint = usuarioLogado?.dni ?: getString(R.string.DNI)


        // Mostramos la fecha de nacimiento del usuario, si está disponible
        val fechaNacimiento = usuarioLogado?.fecha_nacimiento
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.txtFecha.hint = fechaNacimiento?.let { sdf.format(it.toDate()) } ?: getString(R.string.fecha_nacimiento)





        //Ponemos focus en el botón, para que al entrar an la app no lo fije en el primer editText
        binding.actualizarDatosButton.requestFocus()

        binding.fotoPerfil.setOnClickListener {
            val alerta: Alerta = Alerta(
                this.resources.getString(R.string.queQuieresHacer),
                this.resources.getString(R.string.CargarFoto),
                this.resources.getString(R.string.tomarFoto),
                this,
                {
                    lanzadorElegirImagen.launch("image/*")
                },
                {
                    var dateRun = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
                    val intent = Intent(this, CameraActivity::class.java)
                    val inParameter = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    inParameter.putExtra("usuario", LoginActivity.usermail)
                    inParameter.putExtra("dateRun", dateRun)
                    startActivity(intent)
                }
            )
            alerta.mostrar()
        }
        binding.btnFoto?.setOnClickListener {
            val alerta: Alerta = Alerta(
                this.resources.getString(R.string.queQuieresHacer),
                this.resources.getString(R.string.CargarFoto),
                this.resources.getString(R.string.tomarFoto),
                this,
                {
                    lanzadorElegirImagen.launch("image/*")
                },
                {
                    var dateRun = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
                    val intent = Intent(this, CameraActivity::class.java)
                    val inParameter = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    inParameter.putExtra("usuario", LoginActivity.usermail)
                    inParameter.putExtra("dateRun", dateRun)
                    startActivity(intent)
                }
            )
            alerta.mostrar()
        }






        //Hacemos los adapters para los Spinners
        val adapterNatio = ArrayAdapter.createFromResource(
            this,
            R.array.nationality_options,
            android.R.layout.simple_spinner_item
        )
        val adapterGender = ArrayAdapter.createFromResource(
            this,
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        )
        adapterNatio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.natioSpinner.adapter = adapterNatio
        binding.genderSpinner.adapter = adapterGender


        //OnItemSelectedListener para el género
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedGender = parent.getItemAtPosition(position).toString()
                // aquí puedes hacer algo con el género seleccionado, como guardarlo en una variable o enviarlo a un servidor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nada que hacer aquí
            }
        }


        //OnItemSelectedListener para la nacionalidad
        binding.natioSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedGender = parent.getItemAtPosition(position).toString()
                // aquí puedes hacer algo con el género seleccionado, como guardarlo en una variable o enviarlo a un servidor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nada que hacer aquí
            }
        }
        val dateSetListener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                val hoy: LocalDate = LocalDate.now()
                val fechaElegida: LocalDate = LocalDate.of(year, month, day)
                if (!fechaElegida.minusYears(18).isBefore(hoy)) {
                    Toast.makeText(this, R.string.mayorEdad, Toast.LENGTH_LONG).show()
                } else {
                    binding.txtFecha.text = fechaElegida.toString()
                }
            }

        binding.btnFecha.setOnClickListener {
            val calendario: Calendar = Calendar.getInstance()
            val datePicker: DatePickerDialog = DatePickerDialog(
                this,
                dateSetListener, calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.setIcon(R.drawable.starteventp)
            datePicker.setMessage(this.resources.getString(R.string.fecha_nacimiento))

            datePicker.show()
        }

        /**
         * Cuando pulsamos el botón actualizar datos comprobamos que los campos no están vacíos y lanzamos un mensaje de error.
         */
        binding.actualizarDatosButton.setOnClickListener {


            if (binding.etNombre.text.toString().isEmpty() || binding.etDNI.text.toString()
                    .isEmpty()
            ) {
                if (binding.etNombre.text.toString().isEmpty()) {
                    binding.etNombre.error = "Nombre es un campo obligatorio"
                }
                if (binding.etDNI.text.toString().isEmpty()) {
                    binding.etDNI.error = "DNI es un campo obligatorio"
                }
            } else {
                val fechaString = binding.txtFecha.text.toString()

                try {
                    val fecha = LocalDate.parse(fechaString) // Convertir fechaString a LocalDate

                    val timestamp = java.sql.Timestamp(
                        fecha.atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000
                    ) // Convertir LocalDate a Timestamp

                    FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            val usuariosRef = FirebaseFirestore.getInstance().collection("users").document(usermail)
                            usuariosRef.get().addOnSuccessListener { documentSnapshot ->
                                val data = documentSnapshot.data
                                // Crear un mapa con los campos y valores a actualizar
                                val newData = HashMap<String, Any>()
                                if (binding.etNombre.text.toString().isNotBlank()) {
                                    newData["nombre"] = binding.etNombre.text.toString()
                                }
                                if (binding.etApellidos.text.toString().isNotBlank()) {
                                    newData["apellidos"] = binding.etApellidos.text.toString()
                                }
                                if (binding.etDNI.text.toString().isNotBlank()) {
                                    newData["dni"] = binding.etDNI.text.toString()
                                }
                                if (timestamp != null) {
                                    newData["fecha_nacimiento"] = timestamp
                                }
                                if (binding.genderSpinner.selectedItem.toString().isNotBlank()) {
                                    newData["genero"] = binding.genderSpinner.selectedItem.toString()
                                }
                                if (binding.natioSpinner.selectedItem.toString().isNotBlank()) {
                                    newData["nacionalidad"] =
                                        binding.natioSpinner.selectedItem.toString()
                                }
                                if (upImage != null) {
                                    newData["foto_perfil"] = upImage
                                }
                                // Actualizar los campos en Firestore sin sobrescribir los valores existentes en los campos restantes
                                usuariosRef.update(newData)
                                this.cambiarAPantalla("MainActivity")
                                Toast.makeText(this, "!Datos actualizados¡", Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            Toast.makeText(this,"No se han podido actualizar los datos",Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch(e: DateTimeException) {
                    Toast.makeText(this,"Rellena el campo fecha", Toast.LENGTH_LONG).show()
                }
            }




        }

    }
    private fun subirImagenFirebase(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("images/${usermail}/foto_perfil.jpg")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Guardar la URL de la imagen en una variable
                upImage = downloadUri.toString()
                // Mostrar la imagen en la vista
                Glide.with(this).load(upImage).into(binding.fotoPerfil)
            }
        }
    }
}