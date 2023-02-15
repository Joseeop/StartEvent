package emergentes

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.startevent.R


/**
 * Alertaaaaaaaaa
 * Tienes que responsabilizarte tanto del show como del dismiss en el evento del botón.
 */

class Alerta(val titulo:String,val textoBotonCargar:String,val textoBotonFoto:String,val contexto: AppCompatActivity,
             val funcionGaleria:(() -> Unit)?=null,val funcionFoto:(() -> Unit)?=null):DialogFragment() {

    private val layoutInfladoAlerta:LinearLayout by lazy { contexto.layoutInflater.inflate(R.layout.layout_alerta_foto,null,false) as LinearLayout }
    private val textoTitulo:TextView by lazy { layoutInfladoAlerta.findViewById(R.id.titleAlerta) }

    private val botonCargarFoto:Button by lazy{layoutInfladoAlerta.findViewById(R.id.btnCargarFoto)}
    private val botonHacerFoto:Button by lazy{layoutInfladoAlerta.findViewById(R.id.btnHacerFoto)}

    private lateinit var emergente:Dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val dialogBuilder:AlertDialog.Builder=AlertDialog.Builder(contexto)
        /*  dialogBuilder.setTitle(titulo)
         dialogBuilder.setIcon(R.drawable.buenas_vibraciones)
         dialogBuilder.setMessage(mensaje)

         dialogBuilder.setNegativeButton(this.resources.getString(R.string.ok),
             DialogInterface.OnClickListener{
              dialog:DialogInterface,di:Int->
         })*/

        textoTitulo.setText(titulo)
        botonCargarFoto.setText(textoBotonCargar)
        botonHacerFoto.setText(textoBotonFoto)


        dialogBuilder.setView(layoutInfladoAlerta)

        this.emergente=dialogBuilder.create()
        botonCargarFoto.setOnClickListener {
            if(funcionGaleria!=null){
                funcionGaleria.invoke()
            }
            emergente.dismiss()
        }
        botonHacerFoto.setOnClickListener {
            if(funcionFoto!=null){
                funcionFoto.invoke()
            }
            emergente.dismiss()
        }

        return emergente
    }

    fun mostrar():Unit{
        val fManager: FragmentManager =contexto.supportFragmentManager
        this.show(fManager,"alerta"+titulo)
    }


}