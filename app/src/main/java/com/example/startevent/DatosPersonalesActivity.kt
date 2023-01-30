package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class DatosPersonalesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_personales)

        //Rescatamos las variables del XML
        val botonActualizarDatos:Button=findViewById<Button>(R.id.actualizar_datos_button)
        val etNombre:EditText=findViewById(R.id.etNombre)
        val etDNI:EditText=findViewById(R.id.etDNI)
        val natioSpinner = findViewById<Spinner>(R.id.natio_spinner)
        val genderSpinner = findViewById<Spinner>(R.id.gender_spinner)

        //Hacemos los adapters para los Spinners
        val adapterNatio = ArrayAdapter.createFromResource(this,R.array.nationality_options,android.R.layout.simple_spinner_item)
        val adapterGender = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item)
        adapterNatio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        natioSpinner.adapter= adapterNatio
        genderSpinner.adapter = adapterGender

        //OnItemSelectedListener para el género
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val selectedGender = parent.getItemAtPosition(position).toString()
              // aquí puedes hacer algo con el género seleccionado, como guardarlo en una variable o enviarlo a un servidor
        }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nada que hacer aquí
            }
        }
        //OnItemSelectedListener para la nacionalidad
        natioSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGender = parent.getItemAtPosition(position).toString()
                // aquí puedes hacer algo con el género seleccionado, como guardarlo en una variable o enviarlo a un servidor
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nada que hacer aquí
            }
        }

        /**
         * Cuando pulsamos el botón actualizar datos comprobamos que los campos no están vacíos y lanzamos un mensaje de error.
         */
        botonActualizarDatos.setOnClickListener{
            val name = etNombre.text.toString().trim()
            val dni = etDNI.text.toString().trim()

            if (name.isEmpty() || dni.isEmpty()) {
                if (name.isEmpty()) {
                    etNombre.error = "Nombre es un campo obligatorio"
                }
                if (dni.isEmpty()) {
                    etDNI.error = "DNI es un campo obligatorio"
                }
            } else {
                // Procesar los datos
            }
        }

    }

}