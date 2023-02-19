package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import clases.Evento
import com.example.startevent.databinding.ActivityDatosPersonalesBinding
import com.example.startevent.databinding.LayoutEventDetailsBinding
import com.google.android.gms.actions.ItemListIntents

class EventDetailsActivity : AppCompatActivity() {
    lateinit var binding: LayoutEventDetailsBinding
    lateinit var itemDetail : ItemListIntents
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_details)


        binding = LayoutEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.intent.extras
        val bundleRecibido:Bundle?=this.intent.extras


        binding.vacanciesEditText.text=""+bundleRecibido?.getString("vacantes")
        binding.locationEditText.text=""+bundleRecibido?.getString("ubicacion")
        binding.tvFecha.text=""+bundleRecibido?.getString("fecha")
        binding.tvTitulo.text=""+bundleRecibido?.getString("titulo")
        binding.descriptionEditText.text=""+bundleRecibido?.getString("descripcion")
        binding.requirementsEditText.text=""+bundleRecibido?.getString("requisitos")
        binding.etNombreEmpresa.text=""+bundleRecibido?.getString("empresa")


    }
    /*private fun initViews(){
        binding.etTipoEvento
        binding.etNombreEmpresa
        binding.vacanciesEditText
        binding.locationEditText
    }*/
    /*private fun initValues(){
        if (intent != null && intent.extras != null) {
            itemDetail = intent.extras!!.getSerializable("detalles") as ItemListIntents
            binding.vacanciesEditText.setText(itemDetail.getText)
        }
    }*/
}