package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import clases.ActividadMadre

class DatosContactoActivity : ActividadMadre(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_datos_contacto)
    }
}