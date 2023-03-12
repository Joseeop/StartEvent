package com.example.startevent

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.ActividadMadre
import clases.EmpleosAdapter
import clases.Evento
import clases.EventosCreadosAdapter
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity destinada a filtrar en la BBDD los eventos que hay creados, filtra por el creador y el usuario que actualmente está logueado
 * Así puede ver los eventos que ha creado, y posteriormente ver los usuarios que se han escrito en su oferta.
 */
class EventCreatedByUsermail : ActividadMadre() {
    private lateinit var recyclerView: RecyclerView


    private lateinit var empleosArrayList : ArrayList<Evento>

    private lateinit var myAdapter: EventosCreadosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_created_by_usermail)


        recyclerView = findViewById(R.id.rvListaEventos)
        empleosArrayList = arrayListOf()
        myAdapter = EventosCreadosAdapter(this, empleosArrayList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

        /**
         * Aquí hacemos la consulta a la BBDD y filtramos por creador y el currentuser, así sólo verá los eventos que el mismo ha creado.
         */
        val db = FirebaseFirestore.getInstance()
        db.collection("Eventos").whereEqualTo("creador",usermail)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val evento = document.toObject(Evento::class.java)
                    empleosArrayList.add(evento)
                }
                myAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }
}