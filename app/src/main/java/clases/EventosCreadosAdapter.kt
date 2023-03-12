package clases

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startevent.EventDetailsActivity
import com.example.startevent.R
import kotlin.collections.ArrayList

/**
 * Clase destinada a crear un adapter para que el usuario pueda ver los eventos que ha creado desde su cuenta.
 */

class EventosCreadosAdapter(val actividadMadre:ActividadMadre,val eventList: ArrayList<Evento>) :RecyclerView.Adapter<EventosCreadosAdapter.MyViewHolder>() {

    private lateinit var context : android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventosCreadosAdapter.MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_event_created,parent,false)

        return EventosCreadosAdapter.MyViewHolder(actividadMadre.layoutInflater.inflate(R.layout.card_event_created,parent,false))
    }

    override fun onBindViewHolder(holder: EventosCreadosAdapter.MyViewHolder, position: Int) {


        val evento: Evento = eventList.get(position)





        holder.txtTitulo.text=evento.tipo_empleado
        holder.txtUbicacion.text="Ubicación: "+evento.ubicacion
        holder.txtVacantes.text="Vacantes: "+evento.vacantes
        val txtVacantesE="Vacantes: "+evento.vacantes
        holder.txtFecha.text="Fecha del evento: "+evento.fecha_evento?.toDate()
        val txtDescripcionE="Descripción de la oferta:\n "+evento.descripcion
        val txtRequisitos="Requisitos: "+evento.requisitos
        val txtEmpresaCreadora="Empresa Creadora: "+evento.empresa
        val txtTitulo=""+evento.tipo_empleado


        /**
         * Debemos modificar este setOnClickListener, pues pretendemos ver la lista de candidatos que se han inscrito en cada oferta.
         */

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventDetailsActivity::class.java)
            val datos: Bundle =Bundle()

            datos.putParcelable("usuarioLogado", actividadMadre.usuarioLogado)
            datos.putString("ubicacion",holder.txtUbicacion.text.toString())
            datos.putString("vacantes",holder.txtVacantes.text.toString())
            datos.putString("fecha",holder.txtFecha.text.toString())
            datos.putString("titulo",holder.txtTitulo.text.toString())
            datos.putString("tipoEmpleado",txtTitulo)
            datos.putString("descripcion",txtDescripcionE)
            datos.putString("requisitos",txtRequisitos)
            datos.putString("empresa",txtEmpresaCreadora)
            intent.putExtras(datos)
            holder.itemView.context.startActivity(intent)
        }

        holder.btnVerInscritos.setOnClickListener {

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
