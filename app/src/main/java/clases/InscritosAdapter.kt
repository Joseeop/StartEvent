package clases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.R

class InscritosAdapter(private val listaInscritos: ArrayList<Usuario>) :
    RecyclerView.Adapter<InscritosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscritosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_inscritos, parent, false)
        return InscritosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InscritosViewHolder, position: Int) {
        val inscrito = listaInscritos[position]
        holder.txtNombre.text = inscrito.nombre
        holder.txtApellidos.text = inscrito.apellidos
        holder.txtCorreo.text = usermail

    }

    override fun getItemCount(): Int {
        return listaInscritos.size
    }
}

public class InscritosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
    val txtApellidos: TextView = itemView.findViewById(R.id.txtApellidos)
    val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
    val imgUsuario: ImageView = itemView.findViewById(R.id.fotoInscrito)
}