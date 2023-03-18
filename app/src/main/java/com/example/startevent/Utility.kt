package com.example.startevent

import android.widget.LinearLayout

// Objeto de utilidad que proporciona funciones útiles para el manejo de diseños
object Utility {

    // Función para establecer la altura de un LinearLayout
    fun setHeightLinearLayout(ly: LinearLayout, value: Int){
        // Obtener los parámetros de diseño actuales del LinearLayout
        val params: LinearLayout.LayoutParams = ly.layoutParams as LinearLayout.LayoutParams
        // Establecer el valor de altura pasado como argumento
        params.height = value
        // Aplicar los nuevos parámetros de diseño al LinearLayout
        ly.layoutParams = params
    }
}