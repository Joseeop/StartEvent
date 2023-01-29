package clases

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

class EmpleosAdapter(private val eventList: ArrayList<Evento>) :RecyclerView.Adapter<EmpleosAdapter.MyViewHolder>() {

    private lateinit var context : android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleosAdapter.MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_event,parent,false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmpleosAdapter.MyViewHolder, position: Int) {


       val evento: Evento = eventList.get(position)

        var day = evento.fechaEvento?.subSequence(8, 10)
        var n_month = evento.fechaEvento?.subSequence(5, 7)
        var month: String ?= null
        var year = evento.fechaEvento?.subSequence(0, 4)

        when (n_month) {
            "01" -> month = "ENE"
            "02" -> month = "FEB"
            "03" -> month = "MAR"
            "04" -> month = "ABR"
            "05" -> month = "MAY"
            "06" -> month = "JUN"
            "07" -> month = "JUL"
            "08" -> month = "AGO"
            "09" -> month = "SEP"
            "10" -> month = "OCT"
            "11" -> month = "NOV"
            "12" -> month = "DIC"
        }
        var date: String = "$day-$month-$year"
        holder.txtFecha.text = date

        holder.txtTitulo.text=evento.tipoEvento
        holder.txtUbicacion.text=evento.ubicacion
        holder.txtVacantes.text=evento.nVacantes.toString()






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
