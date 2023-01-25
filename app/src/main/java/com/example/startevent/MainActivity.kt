package com.example.startevent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
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
    lateinit var ivCreateEvent: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      //  ivCreateEvent=findViewById(R.id.ivCreateEvent)
       // ivCreateEvent.visibility=View.GONE

        initToolBar()
        initNavigationView()
        val tvUser: TextView =findViewById(R.id.tvUser)
        tvUser.text= "Bienvenid@, "+usermail

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

    /**
     * En esta función asignaremos las acciones a los botones del menú
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        /**
         * Hacemos un when para saber qué opción se está requiriendo del menú.
         */
        when(item.itemId){
            R.id.nav_item_datos -> callProfileActivity()
            R.id.nav_item_signout -> logout()
            R.id.nav_item_job -> searchJobsActivity()
        }
        /**
         * Una vez seleccionamos una opción cerramos el menú, para ello los gestionamos con el drawer, y le ponemos de posición del inicio, de donde salió. Se ocultará en el inicio.
         */
        drawer.closeDrawer(GravityCompat.START)

        return true
    }


    //TODO IMPLEMENTAR LLAMADAS DE CREAR EVENTO Y CREAR EVENTO VIRTUAL
    fun callSearchJobsActivity (v:View){
        searchJobsActivity()
    }
    fun callCreateEventActivity (v:View){
        createEventActivity()
    }
    fun callCreateVirtualActivity (v:View){
        createVirtualActivity()
    }
    /**
     * Función que nos lleva a la pantalla "Zona personal"
     */
    private fun callProfileActivity(){
        val intent = Intent (this,ProfileActivity::class.java)
        startActivity(intent)
    }




    /**
     * Función que nos lleva a la pantalla "Busca trabajo"
     */
    private fun searchJobsActivity(){
        val intent = Intent (this,SearchJobsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Función que nos lleva a la pantalla "Crear clases.Evento"
     */
    private fun createEventActivity(){
        val intent = Intent (this,CreateEventActivity::class.java)
        startActivity(intent)
    }
    private fun createVirtualActivity(){
        val intent = Intent (this,CreateVirtualActivity::class.java)
        startActivity(intent)
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