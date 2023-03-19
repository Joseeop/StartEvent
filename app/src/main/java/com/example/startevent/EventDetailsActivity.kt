package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import clases.ActividadMadre
import clases.Evento
import clases.Usuario
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.databinding.ActivityDatosPersonalesBinding
import com.example.startevent.databinding.LayoutEventDetailsBinding
import com.google.android.gms.actions.ItemListIntents
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase que muestra los detalles del evento que hemos visto previamente listado en el recyclerView
 * en esta clase ofrecemos la posibilidad de poder inscribirnos a la oferta en cuestión siempre y cuando
 * el usuario haya rellenado previamente sus datos. (Por ahora sólo el nombre)
 */
class EventDetailsActivity : ActividadMadre() {
    lateinit var binding: LayoutEventDetailsBinding
    lateinit var itemDetail: ItemListIntents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_details)


        binding = LayoutEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.intent.extras
        val bundleRecibido: Bundle? = this.intent.extras


        val usuarioLogado = bundleRecibido?.getParcelable<Usuario>("usuarioLogado")

        binding.vacanciesEditText.text = "" + bundleRecibido?.getString("vacantes")
        binding.locationEditText.text = "" + bundleRecibido?.getString("ubicacion")
        binding.tvFecha.text = "" + bundleRecibido?.getString("fecha")
        binding.tvTitulo.text = "" + bundleRecibido?.getString("tipoEmpleado")
        binding.descriptionEditText.text = "" + bundleRecibido?.getString("descripcion")
        binding.requirementsEditText.text = "" + bundleRecibido?.getString("requisitos")
        binding.etNombreEmpresa.text = "" + bundleRecibido?.getString("empresa")


        /**
         * Comprobación que obligará al usuario a rellenar los campos de su zona personal antes de poder
         * inscribirse en cualquier oferta.
         * Lo haremos mediante un alert y pondremos un botón que nos llevará a la zona personal y otro que sea cancelar
         */
        binding.btnInscripcion.setOnClickListener {
            // Comprobamos si el usuario está logueado
            if (usuarioLogado?.nombre == null) {
                Toast.makeText(
                    this,
                    getString(R.string.inscripcion_faltan_campos),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Creamos una referencia a la colección de asistentes del evento en Firestore
                val db = FirebaseFirestore.getInstance()
                val eventoId = bundleRecibido?.getString("id")

                val asistentesRef = eventoId?.let { it1 ->
                    db.collection("Eventos").document(it1).collection("asistentes")
                }
                if (eventoId != null) {
                    val asistentesRef =
                        db.collection("Eventos").document(eventoId).collection("asistentes")

                } else {
                    //Toast.makeText(this, "Error al obtener el ID del evento", Toast.LENGTH_SHORT).show()
                }

                // Creamos un objeto con los datos del usuario que se va a inscribir en el evento
                val datosUsuario = hashMapOf(
                    "nombre" to usuarioLogado.nombre,
                    "apellidos" to usuarioLogado.apellidos,
                    "email" to usermail
                )

                // Añadimos los datos del usuario a la colección de asistentes del evento
                if (asistentesRef != null) {
                    asistentesRef.add(datosUsuario)
                        .addOnSuccessListener {
                            // Mostramos un mensaje de éxito y actualizamos el botón de inscripción
                            Toast.makeText(
                                this,
                                getString(R.string.inscripcion_exitosa),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnInscripcion.text = "Inscrito"
                            binding.btnInscripcion.isEnabled = false
                        }
                        .addOnFailureListener {
                            // Mostramos un mensaje de error en caso de que no se haya podido realizar la inscripción
                            Toast.makeText(
                                this,
                                getString(R.string.inscripcion_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

}