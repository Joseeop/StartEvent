package com.example.startevent

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.InscritosAdapter
import clases.Usuario
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity para mostrar la lista de inscritos de un evento.
 */
class InscritosActivity : AppCompatActivity() {

    // Declaración de variables de clase
    private lateinit var recyclerView: RecyclerView
    private lateinit var inscritosAdapter: InscritosAdapter
    private lateinit var listaInscritos: ArrayList<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_inscritos)

        // Inicialización de variables de clase
        recyclerView = findViewById(R.id.rvListaInscritos)
        listaInscritos = intent.getSerializableExtra("inscritosList") as ArrayList<Usuario>
        inscritosAdapter = InscritosAdapter(listaInscritos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = inscritosAdapter

        // Obtener el ID del evento de la intent
        val eventoId = intent.getStringExtra("idEvento")

        // Si el ID del evento no es nulo, obtener la lista de asistentes desde Firestore
        if (eventoId != null) {
            val db = FirebaseFirestore.getInstance()
            val asistentesRef = db.collection("Eventos").document(eventoId).collection("asistentes")
            asistentesRef.get()
                .addOnSuccessListener { documents ->
                    // Si la lista de asistentes no está vacía, agregar cada usuario a la lista de inscritos y notificar al adapter
                    if (!documents.isEmpty) {
                        for (document in documents) {
                            val usuario = document.toObject(Usuario::class.java)
                            listaInscritos.add(usuario)
                        }
                        inscritosAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    // En caso de error, imprimir el mensaje de error y mostrar un toast en la pantalla
                    Log.d(TAG, "Error al obtener la lista de inscritos", exception)
                    Toast.makeText(this, getString(R.string.error_obtener_inscritos), Toast.LENGTH_SHORT).show()
                }
        } else {
            // Si el ID del evento es nulo, mostrar un toast en la pantalla
            Toast.makeText(this, getString(R.string.error_obtener_id_evento), Toast.LENGTH_SHORT).show()
        }
    }
}