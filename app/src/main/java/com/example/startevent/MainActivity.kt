package com.example.startevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toast de bienvenida con el mail del usuario
        Toast.makeText(this,"Hola "+usermail,Toast.LENGTH_SHORT).show()
    }

    fun callLogout(view : View){
        logout()
    }

    /**
     * función que hace signOut de la cuenta y te devuelve a la pantalla de login.
     * restablecemos el usermal a campo vacío
     */
    private fun logout(){
        usermail= ""
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))
    }
}