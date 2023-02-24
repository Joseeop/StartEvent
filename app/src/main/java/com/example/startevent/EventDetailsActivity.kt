package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import clases.ActividadMadre
import clases.Evento
import clases.Usuario
import com.example.startevent.databinding.ActivityDatosPersonalesBinding
import com.example.startevent.databinding.LayoutEventDetailsBinding
import com.google.android.gms.actions.ItemListIntents

class EventDetailsActivity : ActividadMadre() {
    lateinit var binding: LayoutEventDetailsBinding
    lateinit var itemDetail : ItemListIntents
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_details)


        binding = LayoutEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.intent.extras
        val bundleRecibido:Bundle?=this.intent.extras


        val usuarioLogado = bundleRecibido?.getParcelable<Usuario>("usuarioLogado")

        binding.vacanciesEditText.text=""+bundleRecibido?.getString("vacantes")
        binding.locationEditText.text=""+bundleRecibido?.getString("ubicacion")
        binding.tvFecha.text=""+bundleRecibido?.getString("fecha")
        binding.tvTitulo.text=""+bundleRecibido?.getString("tipoEmpleado")
        binding.descriptionEditText.text=""+bundleRecibido?.getString("descripcion")
        binding.requirementsEditText.text=""+bundleRecibido?.getString("requisitos")
        binding.etNombreEmpresa.text=""+bundleRecibido?.getString("empresa")


        /**
         * Comprobación que obligará al usuario a rellenar los campos de su zona personal antes de poder
         * inscribirse en cualquier oferta.
         * Lo haremos mediante un alert y pondremos un botón que nos llevará a la zona personal y otro que sea cancelar
         */
        binding.btnInscripcion.setOnClickListener {
            if(usuarioLogado?.nombre==null){
                Toast.makeText(this,"Debes rellenar el campo nombre",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"YA ESTÁ! "+usuarioLogado.nombre,Toast.LENGTH_SHORT).show()
            }
        }
    }

}