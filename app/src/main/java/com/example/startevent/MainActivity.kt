package com.example.startevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    /**
     * variable que hace referencia a el id del drawer que hemos creado en activity_main.xml
     * Lo necesitamos ya que el activity main está dentro de una etiqueta drawerlayout
     */
    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initToolBar()
        initNavigationView()


    //Toast de bienvenida con el mail del usuario
        //Toast.makeText(this,"Hola "+usermail,Toast.LENGTH_SHORT).show()
    }

    /**
     *Creamos una variable para administrar la toolbar el activitymain
     * con la variable toggle le indicamos: la actividad, que tendrá el layout"drawer", con la toolbar que hemos creado, con el nombre que le hemos indicado, y el texto que saldrá cuando el menú esté desplegado, para cerrarlo, saldrá "Cerrar"
     */
    private fun initToolBar(){
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.menu,R.string.close)

        drawer.addDrawerListener(toggle)

        toggle.syncState()
    }

    /**
     * Creamos una variable para manejar nuestro nav_view que hemos creado en activity_main
     * Le ponemos listener a los botones del menú
     * creamos una variable para inflar el headerView mediante el layoutInflater y la función inflate.
     * cuando cargue la ventana si el usuario tiene registros anteriores, para garantizar que son los suyos
     * borraremos el header y lo volveremos a cargar(removeheader y addheader)
     * por otra parte, recogemos el textview de user y lo asociamos al mail de registro del usuario.
     */
    private fun initNavigationView(){
        var navigationView: NavigationView =findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var headerView: View = LayoutInflater.from(this).inflate(R.layout.nav_header_main,navigationView,false)
        navigationView.removeHeaderView(headerView)
        navigationView.addHeaderView(headerView)

        val tvUser: TextView =headerView.findViewById(R.id.tvUser)
        tvUser.text= usermail


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

    /**
     * En esta función asignaremos las acciones a los botones del menú
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}