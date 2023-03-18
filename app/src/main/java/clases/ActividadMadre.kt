package clases




import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.startevent.MainActivity
import java.util.Locale



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
    fun reiniciarAplicacion() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
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

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("preferenciasPersonalizadas", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("idiomaElegido", Locale.getDefault().language) ?: Locale.getDefault().language
        val newLocale = Locale(languageCode)
        val context = newBase.createConfigurationContext(newBase.resources.configuration.apply { setLocale(newLocale) })
        super.attachBaseContext(context)
    }


}