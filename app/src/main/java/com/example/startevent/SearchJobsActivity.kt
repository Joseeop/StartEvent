package com.example.startevent


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.ActividadMadre
import clases.EmpleosAdapter
import clases.Evento
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

/**
 * Clase destinada a la pantalla de buscar trabajo, en ella veremos reflejada con la ayuda de un RecyclerView
 * Toda la lista de eventos que se encuentren en la BBDD
 */
class SearchJobsActivity : ActividadMadre() {


    // Declaración de variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var empleosArrayList: ArrayList<Evento>
    private lateinit var myAdapter: EmpleosAdapter

    /**
     * Método onCreate que se ejecuta cuando la actividad es creada.
     * En este método se inicializa la vista, se configura el RecyclerView y se obtiene la lista de empleos
     * de Firebase Firestore y se muestra en el RecyclerView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_jobs)

        // Inicialización del RecyclerView y variables relacionadas
        recyclerView = findViewById(R.id.rvListaEventos)
        empleosArrayList = arrayListOf()
        myAdapter = EmpleosAdapter(this, empleosArrayList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

        // Obtener la lista de empleos de Firebase Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("Eventos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val evento = document.toObject(Evento::class.java)
                    evento.id = document.id // Asignar el ID al objeto Evento
                    empleosArrayList.add(evento)
                }
                myAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }


}


