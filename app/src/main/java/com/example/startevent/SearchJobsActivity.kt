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


class SearchJobsActivity : ActividadMadre() {


    private lateinit var recyclerView: RecyclerView


    private lateinit var empleosArrayList : ArrayList<Evento>

    private lateinit var myAdapter: EmpleosAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_jobs)

       /* val valores= arrayListOf<Evento>()
        for (i in 100 downTo 1){
            var evento:Evento=Evento()
            valores.add(evento)
        }*/
        recyclerView = findViewById(R.id.rvListaEventos)
        empleosArrayList = arrayListOf()
        myAdapter = EmpleosAdapter(this, empleosArrayList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

        val db = FirebaseFirestore.getInstance()
        db.collection("Eventos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val evento = document.toObject(Evento::class.java)
                    empleosArrayList.add(evento)
                }
                myAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

       // val evento = document.toObject(Evento::class.java)




      /*  CÓDIGO PARA INFLAR RECYCLER MEDIANTE CONSULTAS A BBDD QUE NO HE LOGRADO IMPLEMENTAR
      recyclerView = findViewById(R.id.rvListaEventos)

        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        empleosArrayList = arrayListOf()
       /* var evento:Evento = Evento(null,"ASDF","Malaga","12/12/2045",null,2U)
        empleosArrayList.add(evento)*/
        //Vinculamos el array con la lista
        //myAdapter = EmpleosAdapter(empleosArrayList)
        recyclerView.adapter = myAdapter



        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_empleos)
        setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.eventosDisponibles)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.black))
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_pastel))

        //funciones para tener la posibilidad de volver atras desde una flecha en el toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        //loadRecyclerView("Eventos",Query.Direction.DESCENDING)


    }

    /**
     * Hacemos la consulta a la base de datos.
     */
    override fun onResume() {
        super.onResume()
        loadRecyclerView("Eventos",Query.Direction.DESCENDING)

    }

    /**
     * en la función onPause limpiamos el arrayList para que en el onResume vuelva a hacer la consulta y generar los datos
     */
    override fun onPause() {
        super.onPause()
        empleosArrayList.clear()
    }

    /**
     * Función para ir a home con el botón atrás
     */
    override fun onSupportNavigateUp(): Boolean {
         onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.orders_jobs_by,menu)

        return true //super.onCreateOptionsMenu(menu)
    }

    /**
     * Función para capturar mediante un when la opción que el usuario ha seleccionado del menú
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var order: Query.Direction=Query.Direction.DESCENDING
        //Aquí se cambiará la selección. Si aparecía más antiguos primero y pulsamos, la próxima vez que
        //Despleguemos el menú aparecerá mas recientes primero
        when(item.itemId){
            R.id.orderby_date -> {
                if(item.title == getString(R.string.orderby_dateZA)){
                    item.title = getString(R.string.orderby_dateAZ)
                    order = Query.Direction.DESCENDING
                    }
                else{
                    item.title = getString(R.string.orderby_dateZA)
                    order = Query.Direction.ASCENDING
                }
                loadRecyclerView("fecha_evento",Query.Direction.DESCENDING)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadRecyclerView(field: String, order: Query.Direction){
        empleosArrayList.clear()
        var dbEvents = FirebaseFirestore.getInstance()
        dbEvents.collection("Eventos").orderBy(field,order)
            .get()
            .addOnSuccessListener { documents ->
                for(events in documents)
                    empleosArrayList.add(events.toObject(Evento::class.java))

                myAdapter.notifyDataSetChanged()
                Log.d("Eventos", "Documents size: " + empleosArrayList.size)
            }
            .addOnFailureListener{ exception ->
                Log.w("Eventos", "Error getting documents: ", exception)
                Log.d("Eventos", "Documents size: " + empleosArrayList.size)
            }
        Log.d("Eventos", "Documents size: " + empleosArrayList.size)
        */

    }


    //loadRecyclerView("Eventos",Query.Direction.DESCENDING)


    }


