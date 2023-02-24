package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import clases.ActividadMadre
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.databinding.LayoutDatosContactoBinding
import com.google.firebase.firestore.FirebaseFirestore

class DatosContactoActivity : AppCompatActivity() {

    lateinit var binding: LayoutDatosContactoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        binding = LayoutDatosContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Populate the fields with data from the database, if available
        val usuariosRef = FirebaseFirestore.getInstance().collection("users").document(usermail)
        usuariosRef.get().addOnSuccessListener { documentSnapshot ->
            binding.editTextPais.setText(documentSnapshot.getString("pais") ?: "")
            binding.editTextProvincia.setText(documentSnapshot.getString("provincia") ?: "")
            binding.editTextCP.setText(documentSnapshot.getLong("cp")?.toString() ?: "")
            binding.editTextMovil.setText(documentSnapshot.getString("movil") ?: "")
            binding.checkBoxCarnetConducir.isChecked = documentSnapshot.getBoolean("carnet_conducir") ?: false
            binding.checkBoxTransportePropio.isChecked = documentSnapshot.getBoolean("transporte_propio") ?: false
            binding.checkBoxMovilidadGeografica.isChecked = documentSnapshot.getBoolean("movilidad_geografica") ?: false
        }

        // Set up the button to update the data in the database
        binding.actualizarDatosButton.setOnClickListener {
            // Validate the input fields
            val pais = binding.editTextPais.text.toString().trim()
            val provincia = binding.editTextProvincia.text.toString().trim()
            val cp = binding.editTextCP.text.toString().toIntOrNull()
            val movil = binding.editTextMovil.text.toString().trim()
            val carnetConducir = binding.checkBoxCarnetConducir.isChecked
            val transportePropio = binding.checkBoxTransportePropio.isChecked
            val movilidadGeografica = binding.checkBoxMovilidadGeografica.isChecked

            if (pais.isBlank() || provincia.isBlank() || cp == null || movil.isBlank()) {
                // Show an error message if any of the required fields are empty
                Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Create a map with the updated data
                val newData = hashMapOf(
                    "pais" to pais,
                    "provincia" to provincia,
                    "cp" to cp,
                    "movil" to movil,
                    "carnet_conducir" to carnetConducir,
                    "transporte_propio" to transportePropio,
                    "movilidad_geografica" to movilidadGeografica
                )

                // Update the data in the database
                usuariosRef.update(newData as Map<String, Any>).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ha ocurrido un error al actualizar los datos. Por favor, inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}