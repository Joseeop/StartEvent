package com.example.startevent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import clases.ActividadMadre
import com.bumptech.glide.Glide
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

/**

Actividad que muestra el perfil del usuario logueado. Muestra los datos personales, de contacto y permite
cambiar ajustes. Además, permite tomar una foto y envía la fecha de realización y el usuario mediante un intent.
 */
class ProfileActivity : ActividadMadre() {

    companion object{
        var upImage : String= ""
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carga la foto de perfil del usuario logueado
        val auth = FirebaseFirestore.getInstance()
        val userRef = auth.collection("users").document(usermail)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val foto_perfil = documentSnapshot.getString("foto_perfil")
                    Glide.with(this).load(foto_perfil).into(binding.fotoPerfil)
                } else {
                    Toast.makeText(this, getString(R.string.usuario_no_existe), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al obtener datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Muestra los datos personales del usuario logueado
        Glide.with(this).load(usuarioLogado?.foto_perfil).into(binding.fotoPerfil)
        //Comprobamos antes de establecer los valores si los campos son nulos,
        //En caso de ser nulos dejamos espacio en blanco, si tenemos datos los asignamos a los campos.
        binding.tvUser.text = (usuarioLogado?.nombre ?: "") + " " + (usuarioLogado?.apellidos ?: "")
        binding.tvCiudad.text = usuarioLogado?.provincia ?: ""
        binding.tvEmail.text = usermail

        // Botones para acceder a las actividades de datos personales, datos de contacto y ajustes
        binding.btnDatosPersonales.setOnClickListener{
            this.cambiarAPantalla("DatosPersonalesActivity")
        }

        binding.btnDatosContacto.setOnClickListener {
            this.cambiarAPantalla("DatosContactoActivity")
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
