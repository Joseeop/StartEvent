package clases


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity


open abstract class ActividadMadre : AppCompatActivity() {
    var usuarioLogado: Usuario? = null

    companion object {

        val REQUEST_ALMACENAMIENTO_EXTERNO: Int = 654567543
    }

    public fun cambiarAPantalla(nombreActividad:String): Unit {
        val intent: Intent = Intent(this,Class.forName("com.example.startevent."+nombreActividad))
        val bundle: Bundle = Bundle()
        bundle.putParcelable("usuarioLogado", usuarioLogado)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.usuarioLogado = bundle.getParcelable("usuarioLogado", Usuario::class.java)
            } else {
                this.usuarioLogado = bundle.getParcelable("usuarioLogado")
            }


        }

    }


}