package com.example.startevent

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import clases.ActividadMadre
import com.bumptech.glide.Glide
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayout
import emergentes.AlertaExamen
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : ActividadMadre() {

    companion object{
        var upImage : String= ""
    }
    private lateinit var viewPager: ViewPager
    private lateinit var binding: ActivityProfileBinding
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Glide.with(this).load(usuarioLogado?.foto_perfil).into(binding.fotoPerfil)
        binding.tvUser.text=  usuarioLogado?.nombre
        //binding.fotoPerfil=usuarioLogado?.foto_perfil
        binding.tvCiudad.text= usuarioLogado?.provincia

        binding.tvEmail.text= usermail
       // binding.tvCiudad.text=usuarioLogado!!.nombre


        binding.btnDatosPersonales.setOnClickListener{
        //val intent:Intent=Intent(this,DatosPersonalesActivity::class.java)
        //this.startActivity(intent)
            this.cambiarAPantalla("DatosPersonalesActivity")
        }

        binding.btnDatosContacto.setOnClickListener {
            //val intent:Intent=Intent(this,DatosContactoActivity::class.java)
            //this.startActivity(intent)
            this.cambiarAPantalla("DatosContactoActivity")
        }

        binding.btnAlerta.setOnClickListener {
            val alertaExamen = AlertaExamen(this)
            alertaExamen.show()
        }

        binding.btnAjustes.setOnClickListener {
            this.cambiarAPantalla("ActividadPreferenciasPorDefecto")
        }


}
    /**
     * Función que nos llevará a la pantalla de la cámara y se llevará mediante un intent la fecha de cuando se realizó la foto y el usuario
     */
     fun takePicture(v: View){
        var dateRun = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val intent = Intent(this, CameraActivity::class.java)

        val inParameter = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        inParameter.putExtra("usuario", usermail)
        inParameter.putExtra("dateRun", dateRun)

        startActivity(intent)
    }

}
