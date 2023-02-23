package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import clases.ActividadMadre

class CreateVirtualActivity : ActividadMadre() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_virtual)
    }
}