package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.view.Event
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class CreateEventActivity : AppCompatActivity() {
    private lateinit var locationEditText: EditText
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
        dateEditText = findViewById(R.id.date_edit_text)
        employeeTypeEditText = findViewById(R.id.employee_type_edit_text)
        vacanciesEditText = findViewById(R.id.vacancies_edit_text)
        requirementsEditText = findViewById(R.id.requirements_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        createEventButton = findViewById(R.id.create_event_button)

        //Aquí podría ir un código para el manejo del evento click del boton createEventButton

}}