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
 * Clase Alerta
 *
 * Esta clase es un diálogo personalizado para mostrar opciones al usuario.
 * Recibe el título y los textos de los botones de la alerta.
 * También recibe dos funciones, que se ejecutarán cuando se haga clic en los botones.
 * El diálogo se muestra en el contexto de la actividad que se le pasa como parámetro.
 */
class Alerta(
    val titulo: String, // Título del diálogo
    val textoBotonCargar: String, // Texto del primer botón
    val textoBotonFoto: String, // Texto del segundo botón
    val contexto: AppCompatActivity, // Contexto de la actividad en la que se mostrará el diálogo
    val funcionGaleria: (() -> Unit)? = null, // Función que se ejecutará al pulsar el primer botón
    val funcionFoto: (() -> Unit)? = null // Función que se ejecutará al pulsar el segundo botón
) : DialogFragment() {

    // Vistas del layout de la alerta
    private val layoutInfladoAlerta: LinearLayout by lazy {
        contexto.layoutInflater.inflate(
            R.layout.layout_alerta_foto,
            null,
            false
        ) as LinearLayout
    }
    private val textoTitulo: TextView by lazy { layoutInfladoAlerta.findViewById(R.id.titleAlerta) }
    private val botonCargarFoto: Button by lazy { layoutInfladoAlerta.findViewById(R.id.btnCargarFoto) }
    private val botonHacerFoto: Button by lazy { layoutInfladoAlerta.findViewById(R.id.btnHacerFoto) }

    // Dialog que se mostrará en la pantalla
    private lateinit var emergente: Dialog

    /**
     * Función onCreateDialog
     *
     * Esta función se encarga de crear el diálogo.
     * Configura las vistas y los botones del layout.
     * Asigna las funciones que se ejecutarán al hacer clic en los botones.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(contexto)

        // Asignar el título de la alerta
        textoTitulo.setText(titulo)

        // Asignar el texto de los botones de la alerta
        botonCargarFoto.setText(textoBotonCargar)
        botonHacerFoto.setText(textoBotonFoto)

        // Asignar el layout a la alerta
        dialogBuilder.setView(layoutInfladoAlerta)

        // Crear el diálogo
        this.emergente = dialogBuilder.create()

        // Asignar la función que se ejecutará al hacer clic en el botón 1
        botonCargarFoto.setOnClickListener {
            if (funcionGaleria != null) {
                funcionGaleria.invoke()
            }
            emergente.dismiss()
        }

        // Asignar la función que se ejecutará al hacer clic en el botón 2
        botonHacerFoto.setOnClickListener {
            if (funcionFoto != null) {
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