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

class InscritosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inscritosAdapter: InscritosAdapter
    private lateinit var listaInscritos: ArrayList<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_inscritos)

        recyclerView = findViewById(R.id.rvListaInscritos)
        listaInscritos = intent.getSerializableExtra("inscritosList") as ArrayList<Usuario>
        inscritosAdapter = InscritosAdapter(listaInscritos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = inscritosAdapter

        val eventoId = intent.getStringExtra("idEvento")

        if (eventoId != null) {
            val db = FirebaseFirestore.getInstance()
            val asistentesRef = db.collection("Eventos").document(eventoId).collection("asistentes")
            asistentesRef.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (document in documents) {
                            val usuario = document.toObject(Usuario::class.java)
                            listaInscritos.add(usuario)
                        }
                        inscritosAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error al obtener la lista de inscritos", exception)
                }
        } else {
            Toast.makeText(this, "Error al obtener el ID del evento", Toast.LENGTH_SHORT).show()
        }
    }
}