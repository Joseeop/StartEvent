package clases

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.startevent.R
import com.example.startevent.Utility.setHeightLinearLayout
import io.grpc.Context

class EmpleosAdapter(val actividadMadre:Activity,val eventList: ArrayList<Evento>) :RecyclerView.Adapter<EmpleosAdapter.MyViewHolder>() {

    private lateinit var context : android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleosAdapter.MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_event,parent,false)

        return EmpleosAdapter.MyViewHolder(actividadMadre.layoutInflater.inflate(R.layout.card_event,parent,false))
    }

    override fun onBindViewHolder(holder: EmpleosAdapter.MyViewHolder, position: Int) {


       val evento: Evento = eventList.get(position)





        holder.txtTitulo.text=evento.tipoEvento
        holder.txtUbicacion.text=evento.ubicacion
        holder.txtVacantes.text="Vacantes: "+evento.nVacantes.toString()
        holder.txtFecha.text=evento.fechaEvento.toString()






       /* holder.lyContenedor.setOnClickListener{
            TODO DARLE ALGUNA FUNCIONALIDAD AL LAYOUT CUANDO LE HAGAMOS CLICK
        }*/


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




    }




}
