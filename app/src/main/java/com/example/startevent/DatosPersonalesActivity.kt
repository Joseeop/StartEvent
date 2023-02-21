package com.example.startevent

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.startevent.ProfileActivity.Companion.upImage
import com.example.startevent.databinding.ActivityDatosPersonalesBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import emergentes.Alerta
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class DatosPersonalesActivity : AppCompatActivity() {

    lateinit var binding: ActivityDatosPersonalesBinding
    val lanzadorElegirImagen = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.fotoPerfil.setImageURI(it)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_personales)

        binding = ActivityDatosPersonalesBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

                val fecha = LocalDate.parse(fechaString) // Convertir fechaString a LocalDate
                val timestamp = java.sql.Timestamp(
                    fecha.atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000
                ) // Convertir LocalDate a Timestamp

                FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        val usuariosRef = FirebaseFirestore.getInstance().collection("users")
                        usuariosRef.document(binding.etDNI.text.toString()).set(
                            hashMapOf(
                                "nombre" to binding.etNombre.text.toString(),
                                "apellidos" to binding.etApellidos.text.toString(),
                                "dni" to binding.etDNI.text.toString(),
                                "fecha_nacimiento" to timestamp,
                                "genero" to binding.genderSpinner.selectedItem.toString(),
                                "nacionalidad" to binding.natioSpinner.selectedItem.toString(),
                                "foto_perfil" to upImage,
                                //TODO INSERTAR URL DE IMAGEN Y HACER QUE EL INSERT SE HAGA EN LA COLECCIÓN CORRECTA.
                            ))
                        val intent = Intent (this,MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this,"!Datos actualizados¡",Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this,"No se han podido actualizar los datos",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.fotoPerfil.setOnClickListener {
            val alerta: Alerta =
                Alerta(this.resources.getString(R.string.queQuieresHacer),
                    this.resources.getString(R.string.CargarFoto),
                    this.resources.getString(R.string.tomarFoto),
                    this, {
                        lanzadorElegirImagen.launch("image/*")
                    },
                    {
                        var dateRun = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
                        val intent = Intent(this, CameraActivity::class.java)
                        val inParameter = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        inParameter.putExtra("usuario", LoginActivity.usermail)
                        inParameter.putExtra("dateRun", dateRun)
                        startActivity(intent)
                    })
            alerta.mostrar()
        }

    }

}