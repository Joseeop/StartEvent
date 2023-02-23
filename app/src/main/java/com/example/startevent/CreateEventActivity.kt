package com.example.startevent

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import clases.ActividadMadre
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.time.*
import java.util.*



class CreateEventActivity : ActividadMadre() {
    private lateinit var locationEditText: EditText
    private lateinit var titleEditText: EditText
    private lateinit var tvFecha: TextView
    private lateinit var employeeTypeEditText: EditText
    private lateinit var vacanciesEditText: EditText
    private lateinit var requirementsEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var nombreEmpresaEditText: EditText
    private lateinit var createEventButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var btnFecha: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        database = FirebaseDatabase.getInstance().reference

        //Enlazamos los elementos del layout con las variables
        locationEditText = findViewById(R.id.location_edit_text)
        nombreEmpresaEditText = findViewById(R.id.etNombreEmpresa)
        titleEditText = findViewById(R.id.etTipoEvento)
        tvFecha = findViewById(R.id.tvFecha)
        employeeTypeEditText = findViewById(R.id.employee_type_edit_text)
        vacanciesEditText = findViewById(R.id.vacancies_edit_text)
        requirementsEditText = findViewById(R.id.requirements_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        createEventButton = findViewById(R.id.create_event_button)
        btnFecha = findViewById(R.id.btnFecha)
        //Aquí podría ir un código para el manejo del evento click del boton createEventButton

        val dateSetListener: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                val hoy: LocalDate = LocalDate.now()
                val fechaElegida: LocalDate = LocalDate.of(year, month,day )
                if (!fechaElegida.minusYears(18).isBefore(hoy)) {
                    Toast.makeText(this, R.string.mayorEdad, Toast.LENGTH_LONG).show()
                } else {
                    tvFecha.text = fechaElegida.toString()
                }
            }
        btnFecha.setOnClickListener {
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

        createEventButton.setOnClickListener{
          FirebaseFirestore.getInstance().collection("Eventos").get().addOnCompleteListener(this) { task ->
            // Obtener la fecha del TextView
              // Obtener la fecha del TextView
              val fechaString = tvFecha.text.toString()

              val fecha = LocalDate.parse(fechaString) // Convertir fechaString a LocalDate
              val timestamp = Timestamp(fecha.atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000) // Convertir LocalDate a Timestamp


             //TODO Hacer función que compruebe que el evento no ha sido creado con anterioridad, tomando como referencia
              //la clave primaria (el título)
              if(task.isSuccessful){
              FirebaseFirestore.getInstance().collection("Eventos").document(titleEditText.text.toString()).set(
                  hashMapOf(
                      "creador" to usermail,
                      "empresa" to nombreEmpresaEditText.text.toString(),
                      "titulo" to titleEditText.text.toString(),
                      "ubicacion" to locationEditText.text.toString(),
                      "fecha_evento" to timestamp,
                      "tipo_empleado" to employeeTypeEditText.text.toString(),
                      "vacantes" to vacanciesEditText.text.toString(),
                      "requisitos" to requirementsEditText.text.toString(),
                      "descripcion" to descriptionEditText.text.toString(),
                  ))
                  val intent = Intent (this,MainActivity::class.java)
                  startActivity(intent)
                  Toast.makeText(this,"Evento creado con éxito¡",Toast.LENGTH_SHORT).show()
              }else {
                  Toast.makeText(this,"No se ha podido crear el evento",Toast.LENGTH_SHORT).show()
              }





          }
        }
}}