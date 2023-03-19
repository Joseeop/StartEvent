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

        // Inflar la vista
        binding = LayoutDatosContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Rellenar los campos con los datos de la base de datos, si están disponibles
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

        // Configurar el botón para actualizar los datos en la base de datos
        binding.actualizarDatosButton.setOnClickListener {
            // Validar los campos de entrada
            val pais = binding.editTextPais.text.toString().trim()
            val provincia = binding.editTextProvincia.text.toString().trim()
            val cp = binding.editTextCP.text.toString().toIntOrNull()
            val movil = binding.editTextMovil.text.toString().trim()
            val carnetConducir = binding.checkBoxCarnetConducir.isChecked
            val transportePropio = binding.checkBoxTransportePropio.isChecked
            val movilidadGeografica = binding.checkBoxMovilidadGeografica.isChecked

            if (pais.isBlank() || provincia.isBlank() || cp == null || movil.isBlank()) {
                // Mostrar un mensaje de error si alguno de los campos obligatorios está vacío
                Toast.makeText(this,getString(R.string.rellenaTodos) , Toast.LENGTH_SHORT).show()
            } else {
                // Crear un mapa con los datos actualizados
                val newData = hashMapOf(
                    "pais" to pais,
                    "provincia" to provincia,
                    "cp" to cp,
                    "movil" to movil,
                    "carnet_conducir" to carnetConducir,
                    "transporte_propio" to transportePropio,
                    "movilidad_geografica" to movilidadGeografica
                )

                // Actualizar los datos en la base de datos
                usuariosRef.update(newData as Map<String, Any>).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, getString(R.string.datosActualizados), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.datosNoActualizados), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}