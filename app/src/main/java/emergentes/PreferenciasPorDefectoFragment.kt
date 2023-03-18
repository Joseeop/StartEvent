package emergentes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import clases.ActividadMadre
import com.example.startevent.R
import java.util.*

/**
 * Clase que representa el fragmento de preferencias de idioma y modo oscuro
  */

class PreferenciasPorDefectoFragment : PreferenceFragmentCompat() {

    // Método que se llama al crear las preferencias
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        // Establecer las preferencias desde el archivo XML
        setPreferencesFromResource(R.xml.preferencias_por_defecto, rootKey)

        // Obtener la preferencia de modo oscuro
        val modoOscuroPreference = findPreference<SwitchPreference>("modoOscuro")

        // Establecer un listener para cuando cambie el valor de la preferencia
        modoOscuroPreference?.setOnPreferenceChangeListener { _, newValue ->

            // Cambiar el modo oscuro de la aplicación según el valor seleccionado
            if (newValue as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

        // Obtener la preferencia de idioma elegido
        val idiomaElegidoPreference = findPreference<ListPreference>("idiomaElegido")

        // Establecer un listener para cuando cambie el valor de la preferencia
        idiomaElegidoPreference?.setOnPreferenceChangeListener { _, newValue ->

            // Obtener el idioma seleccionado y guardarlo en las preferencias compartidas
            val idiomaSeleccionado = newValue as String
            activity?.getSharedPreferences("preferenciasPersonalizadas", Context.MODE_PRIVATE)?.edit()?.apply {
                putString("idiomaElegido", idiomaSeleccionado)
                apply()
            }

            // Reiniciar la aplicación para que se apliquen los cambios de idioma
            if (activity is ActividadMadre) {
                (activity as ActividadMadre).reiniciarAplicacion()
            }

            true
        }
    }

    // Método que se llama para cambiar el idioma de la aplicación
    private fun cambiarIdioma(context: Context?, idioma: String) {

        // Establecer el idioma seleccionado como el idioma por defecto de la aplicación
        val locale = Locale(idioma)
        Locale.setDefault(locale)

        // Establecer la configuración con el idioma seleccionado
        val configuration = context?.resources?.configuration?.apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }

        // Actualizar la configuración de recursos de la aplicación
        context?.resources?.updateConfiguration(configuration, context.resources.displayMetrics)

        // Guardar el idioma seleccionado en las preferencias compartidas
        val sharedPreferences = context?.getSharedPreferences("preferenciasPersonalizadas", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("idiomaElegido", idioma)?.apply()

        // Registrar el idioma seleccionado en los logs
        Log.d("Preferencias", "Idioma guardado: $idioma")
    }
}