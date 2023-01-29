package com.example.startevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class CreateEventActivity : AppCompatActivity() {
    private lateinit var locationEditText: EditText
    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var employeeTypeEditText: EditText
    private lateinit var vacanciesEditText: EditText
    private lateinit var requirementsEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var createEventButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        database = FirebaseDatabase.getInstance().reference

        //Enlazamos los elementos del layout con las variables
        locationEditText = findViewById(R.id.location_edit_text)
        titleEditText = findViewById(R.id.title_edit_text)
        dateEditText = findViewById(R.id.date_edit_text)
        employeeTypeEditText = findViewById(R.id.employee_type_edit_text)
        vacanciesEditText = findViewById(R.id.vacancies_edit_text)
        requirementsEditText = findViewById(R.id.requirements_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        createEventButton = findViewById(R.id.create_event_button)

        //Aquí podría ir un código para el manejo del evento click del boton createEventButton

        createEventButton.setOnClickListener{
          FirebaseFirestore.getInstance().collection("Eventos").get().addOnCompleteListener(this) { task ->

             //TODO Hacer función que compruebe que el evento no ha sido creado con anterioridad, tomando como referencia
              //la clave primaria (el título)
              if(task.isSuccessful){
              FirebaseFirestore.getInstance().collection("Eventos").document(titleEditText.text.toString()).set(
                  hashMapOf(
                      "creador" to usermail,
                      "titulo" to titleEditText.text.toString(),
                      "ubicacion" to locationEditText.text.toString(),
                      "fecha_evento" to dateEditText.text.toString(),
                      "tipo_empleado" to employeeTypeEditText.text.toString(),
                      "vacantes" to vacanciesEditText.text.toString(),
                      "requisitos" to requirementsEditText.text.toString(),
                      "descripcion" to descriptionEditText.text.toString(),
                  ))
                  val intent = Intent (this,MainActivity::class.java)
                  startActivity(intent)
                  Toast.makeText(this,"!clases.Evento creado con éxito¡",Toast.LENGTH_SHORT).show()
              }else {
                  Toast.makeText(this,"No se ha podido crear el evento",Toast.LENGTH_SHORT).show()
              }





          }
        }
}}