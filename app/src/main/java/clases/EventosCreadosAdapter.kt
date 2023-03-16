package clases

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startevent.EventDetailsActivity
import com.example.startevent.InscritosActivity
import com.example.startevent.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

/**
 * Clase destinada a crear un adapter para que el usuario pueda ver los eventos que ha creado desde su cuenta.
 */

class EventosCreadosAdapter(val actividadMadre:ActividadMadre,val eventList: ArrayList<Evento>) :RecyclerView.Adapter<EventosCreadosAdapter.MyViewHolder>() {

    private lateinit var context : android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventosCreadosAdapter.MyViewHolder {
        context = parent.context
        return MyViewHolder(actividadMadre.layoutInflater.inflate(R.layout.card_event_created,parent,false))
    }

    override fun onBindViewHolder(holder: EventosCreadosAdapter.MyViewHolder, position: Int) {
        if (eventList.isNotEmpty()) {
            val evento = eventList[position]
            holder.txtTitulo.text = evento.tipo_empleado
            holder.txtUbicacion.text = "Ubicación: ${evento.ubicacion}"
            holder.txtVacantes.text = "Vacantes: ${evento.vacantes}"
            holder.txtFecha.text = "Fecha del evento: ${evento.fecha_evento?.toDate()}"

            holder.btnVerInscritos.setOnClickListener {
                FirebaseFirestore.getInstance().collection("Eventos").whereEqualTo("id_evento", evento.id)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            val eventoDoc = result.documents[0]
                            FirebaseFirestore.getInstance().collection("Eventos")
                                .document(eventoDoc.id)
                                .collection("asistentes")
                                .get()
                                .addOnSuccessListener { inscritosDocs ->
                                    val inscritosList = ArrayList<Usuario>()
                                    for (inscritoDoc in inscritosDocs) {
                                        val usuario = inscritoDoc.toObject(Usuario::class.java)
                                        inscritosList.add(usuario)
                                    }
                                    val intent = Intent(holder.itemView.context, InscritosActivity::class.java)
                                    intent.putExtra("inscritosList", inscritosList) // Pasa la lista de inscritos a la actividad InscritosActivity
                                    holder.itemView.context.startActivity(intent)
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    }
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, EventDetailsActivity::class.java)
                // pasar datos a EventDetailsActivity
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    public class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){


        val lyContenedor: LinearLayout = itemView.findViewById(R.id.lyContenedor)
        val imgEmpresa: ImageView = itemView.findViewById(R.id.imgEmpresa)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtUbicacion: TextView= itemView.findViewById(R.id.txtUbicacion)
        val txtVacantes: TextView= itemView.findViewById(R.id.txtVacantes)
        val txtFecha: TextView= itemView.findViewById(R.id.txtFecha)
        val btnVerInscritos:Button=itemView.findViewById(R.id.btnVerInscritos)
        // val txtDescripcion: TextView=itemView.findViewById(R.id.description_edit_text)



    }




}
