package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import clases.ActividadMadre
import emergentes.PreferenciasPorDefectoFragment
/**

Actividad encargada de mostrar la pantalla de preferencias por defecto, la cual utiliza un fragmento
para mostrar las opciones de configuración al usuario.
 */
class ActividadPreferenciasPorDefecto : ActividadMadre() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_preferencias_por_defecto)
        // Inicializa el fragmento y lo agrega al contenedor
        supportFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, PreferenciasPorDefectoFragment())
            .commit()
    }
}