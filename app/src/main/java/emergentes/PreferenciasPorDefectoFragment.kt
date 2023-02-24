package emergentes

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

import com.example.startevent.R
import java.util.*


class PreferenciasPorDefectoFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val manager: androidx.preference.PreferenceManager? =this.preferenceManager
        if (manager != null) {
            manager.sharedPreferencesName="preferenciasPersonalizadas"
        }
        setPreferencesFromResource(R.xml.preferencias_por_defecto,rootKey)
        val modoOscuroPreference = findPreference<SwitchPreference>("modoOscuro")
        modoOscuroPreference?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

        val idiomaElegidoPreference = findPreference<ListPreference>("idiomaElegido")
        idiomaElegidoPreference?.setOnPreferenceChangeListener { _, newValue ->
            val idiomaSeleccionado = newValue as String
            val locale = Locale(idiomaSeleccionado)
            Locale.setDefault(locale)
            val resources = activity?.resources
            val configuration = resources?.configuration
            configuration?.setLocale(locale)
            resources?.updateConfiguration(configuration, resources.displayMetrics)
            activity?.recreate()
            true
        }
    }
    }



