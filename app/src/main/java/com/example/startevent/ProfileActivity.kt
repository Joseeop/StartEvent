package com.example.startevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.startevent.LoginActivity.Companion.usermail
import com.example.startevent.R
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager

    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val botonIrDatosPersonales:Button=findViewById<Button>(R.id.btnDatosPersonales)
        val botonIrDatosContacto:Button=findViewById(R.id.btnDatosContacto)
        val tvUser: TextView =findViewById(R.id.tvUser)
        tvUser.text=  usermail


    botonIrDatosPersonales.setOnClickListener{
        val intent:Intent=Intent(this,DatosPersonalesActivity::class.java)
        this.startActivity(intent)
        }

        botonIrDatosContacto.setOnClickListener {
            val intent:Intent=Intent(this,DatosContactoActivity::class.java)
            this.startActivity(intent)
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
