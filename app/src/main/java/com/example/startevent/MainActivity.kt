package com.example.startevent

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import clases.ActividadMadre
import clases.Usuario
import com.bumptech.glide.Glide
import com.example.startevent.LoginActivity.Companion.usermail
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stripe.android.PaymentConfiguration
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ActividadMadre(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * variable que hace referencia a el id del drawer que hemos creado en activity_main.xml
     * Lo necesitamos ya que el activity main está dentro de una etiqueta drawerlayout
     */
    private lateinit var drawer: DrawerLayout
    lateinit var ivCreateEvent: ImageView
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    private var unloadeadedAd = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inicializo Firebase
        val auth = FirebaseFirestore.getInstance()

        // Obtengo el correo electrónico del usuario actual
        val userMail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        // Obtengo la referencia al documento del usuario actual en Firestore
        val userRef = auth.collection("users").document(userMail)

        // Obtengo los datos del usuario actual del documento correspondiente en Firestore
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Si existe un documento en Firestore para el usuario actual, obtiene los datos
                // correspondientes y crea un objeto Usuario
                val nombre = documentSnapshot.getString("nombre")
                val apellidos = documentSnapshot.getString("apellidos")
                val dni = documentSnapshot.getString("dni")
                val fecha_nacimiento = documentSnapshot.getTimestamp("fecha_nacimiento")
                val genero = documentSnapshot.getString("genero")
                val nacionalidad = documentSnapshot.getString("nacionalidad")
                val pais = documentSnapshot.getString("pais")
                val provincia = documentSnapshot.getString("provincia")
                val cp = documentSnapshot.getLong("cp")?.toInt()
                val movil = documentSnapshot.getString("movil")
                val carnet_conducir = documentSnapshot.getBoolean("carnet_conducir")
                val transporte_propio = documentSnapshot.getBoolean("transporte_propio")
                val movilidad_geografica = documentSnapshot.getBoolean("movilidad_geografica")
                val foto_perfil = documentSnapshot.getString("foto_perfil")

                val usuario = Usuario(
                    nombre,
                    apellidos,
                    dni,
                    fecha_nacimiento,
                    genero,
                    nacionalidad,
                    pais,
                    provincia,
                    cp,
                    movil,
                    carnet_conducir,
                    transporte_propio,
                    movilidad_geografica,
                    foto_perfil
                )
                // Asigno el objeto Usuario a la variable usuarioLogado y muestra un Toast con el nombre
                // del usuario
                usuarioLogado = usuario
                val tvUser: TextView = findViewById(R.id.tvUser)
                val tvUserVacio: TextView = findViewById(R.id.tvUserVacio)
                if ((usuarioLogado?.nombre?.isNotEmpty() ?: false) || (usuarioLogado?.apellidos?.isNotEmpty() ?: false)) {
                    tvUser.text = resources.getString(R.string.welcome) + usuarioLogado?.nombre+" "+usuarioLogado?.apellidos
                } else {
                    tvUser.text = resources.getString(R.string.welcome) + usermail
                    tvUserVacio.text=getString(R.string.ve_zona_personal)
                }
                if(usuarioLogado?.nombre==null || usuarioLogado?.dni ==null){
                    Toast.makeText(this,getString(R.string.welcome)+ usermail+getString(R.string.ve_zona_personal),
                        Toast.LENGTH_LONG).show()

                }


                initNavigationView()
            }else {
                Toast.makeText(this,getString( R.string.usuario_no_existe), Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,getString( R.string.error_datos ), Toast.LENGTH_SHORT).show()
        }

        initToolBar()
        //initNavigationView()
       // val tvUser: TextView = findViewById(R.id.tvUser)
        //TODO COMPROBAR PORQUÉ ESTOS CAMPOS NO SE RELLENAN EN EL PRIMER LOGIN Y APARECEN NULOS
       // tvUser.text = resources.getString(R.string.welcome) + usuarioLogado?.nombre+" "+usuarioLogado?.apellidos
        initAds()

        //Toast de bienvenida con el mail del usuario
        //Toast.makeText(this,"Hola "+usermail,Toast.LENGTH_SHORT).show()
    }

    /**
     * Función para implementar anuncio en layout inferior del activitymain.
     */
    private fun initAds() {
        MobileAds.initialize(this)
        val adView = AdView(this)
        adView.setAdSize(AdSize.LARGE_BANNER)
        //ID REAL, PROBAR CUANDO APRUEBEN LA APP EN ADMOB
        //adView.adUnitId = "ca-app-pub-7223235588589783/8296319035"
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        var lyAdsBanner = findViewById<LinearLayout>(R.id.lyAdsBanner)
        lyAdsBanner.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
// TODO: Add adView to your view hierarchy

    }
    /**

    Función que muestra el anuncio intersticial si está cargado y listo para mostrarse.

    Si el anuncio no está cargado, se muestra un mensaje en el Log indicando que no está listo todavía.
     */
    private fun showIntersitial(){
        if (mInterstitialAd != null) {
            unloadeadedAd = true
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }
            }

            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
    /**

    Función que carga el anuncio intersticial.

    Se crea un objeto AdRequest para solicitar el anuncio y se utiliza el ID de prueba de AdMob.

    Si la carga del anuncio falla, se muestra un mensaje en el Log indicando el error.

    Si la carga del anuncio es exitosa, se guarda el objeto InterstitialAd en la variable mInterstitialAd.
     */
    private fun getReadyAds(){
        var adRequest = AdRequest.Builder().build()
        unloadeadedAd = false
        //ID REAL, PROBAR CUANDO APRUEBEN LA APP EN ADMOB
        //InterstitialAd.load(this,"ca-app-pub-7223235588589783/2282123768", adRequest, object : InterstitialAdLoadCallback()
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

    }

    /**
     *Creamos una variable para administrar la toolbar el activitymain
     * con la variable toggle le indicamos: la actividad, que tendrá el layout"drawer", con la toolbar que hemos creado, con el nombre que le hemos indicado, y el texto que saldrá cuando el menú esté desplegado, para cerrarlo, saldrá "Cerrar"
     */
    private fun initToolBar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.menu, R.string.close)

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
    private fun initNavigationView() {
        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var headerView: View =
            LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false)
        navigationView.removeHeaderView(headerView)
        navigationView.addHeaderView(headerView)


        val tvUser2: TextView = headerView.findViewById(R.id.tvUserWelcome)
        val fotoPerfilHeader: ImageView = headerView.findViewById(R.id.fotoPerfilHeader)

        val nombreUsuario = usuarioLogado?.nombre
        val fotoPerfilUsuario = usuarioLogado?.foto_perfil

        if (nombreUsuario.isNullOrEmpty()) {
            tvUser2.text =getString(R.string.welcome) + " Haz click en la imagen superior y actualiza tus datos"
        } else {
            tvUser2.text = getString(R.string.welcome) + " " + nombreUsuario
        }

        if (fotoPerfilUsuario.isNullOrEmpty()) {
            fotoPerfilHeader.setImageResource(R.drawable.fotocv)
        } else {
            Glide.with(this).load(fotoPerfilUsuario).into(fotoPerfilHeader)
        }

        fotoPerfilHeader.setOnClickListener {
            this.cambiarAPantalla("DatosPersonalesActivity")
        }



    }

    /**
     * En esta función asignaremos las acciones a los botones del menú
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        /**
         * Hacemos un when para saber qué opción se está requiriendo del menú.
         */
        when (item.itemId) {
            R.id.nav_item_datos -> callProfileActivity()
            R.id.nav_item_signout -> logout()
            R.id.nav_item_job -> searchJobsActivity()
            R.id.nav_item_premium -> premiumActivity()
            R.id.nav_item_ad -> Toast.makeText(this,getString(R.string.contacta),Toast.LENGTH_LONG).show()
        }
        /**
         * Una vez seleccionamos una opción cerramos el menú, para ello los gestionamos con el drawer, y le ponemos de posición del inicio, de donde salió. Se ocultará en el inicio.
         */
        drawer.closeDrawer(GravityCompat.START)

        return true
    }



    fun callSearchJobsActivity(v: View) {
        searchJobsActivity()
    }

    fun callCreateEventActivity(v: View) {
        createEventActivity()
    }

    fun callCreateVirtualActivity(v: View) {
        createVirtualActivity()
    }
    fun callEventsCreated(v: View) {
        eventsCreated()
    }


    /**
     * Función que nos lleva a la pantalla "Zona personal"
     */
    private fun callProfileActivity() {
        //val intent = Intent(this, ProfileActivity::class.java)
        //startActivity(intent)
        this.cambiarAPantalla("ProfileActivity")
    }



    private fun premiumActivity() {
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_Dt4ZBItXSZT1EzmOd8yCxonL"
        )
        val intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
    }


    /**
     * Función que nos lleva a la pantalla "Busca trabajo"
     */
    private fun searchJobsActivity() {
        if(unloadeadedAd == true) getReadyAds()
        showIntersitial()
        //val intent = Intent(this, SearchJobsActivity::class.java)
        //startActivity(intent)
        this.cambiarAPantalla("SearchJobsActivity")
    }

    /**
     * Función que nos lleva a la pantalla "Crear clases.Evento"
     */
    private fun createEventActivity() {
        //val intent = Intent(this, CreateEventActivity::class.java)
        //startActivity(intent)
        this.cambiarAPantalla("CreateEventActivity")
    }

    private fun eventsCreated(){
        this.cambiarAPantalla("EventCreatedByUsermail")
    }
    private fun createVirtualActivity() {
        //val intent = Intent(this, CreateVirtualActivity::class.java)
        //startActivity(intent)
        this.cambiarAPantalla("CreateVirtualActivity")
    }


    fun callLogout(view: View) {
        logout()
    }

    /**
     * función que hace signOut de la cuenta y te devuelve a la pantalla de login.
     * restablecemos el usermal a campo vacío
     */
    private fun logout() {
        usermail = ""
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    /**
     * Función que nos llevará a la pantalla de la cámara y se llevará mediante un intent la fecha de cuando se realizó la foto y el usuario
     */
    fun takePicture(v: View) {
        //ESTAS DOS VARIABLES SERÁN LOS IDENTIFICADORES DE LA FOTO, DE AHÍ A PONERLE MINUTOS Y SEGUNDOS, PARA QUE NO PUEDA REPETIRSE
        var dateRun = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
        val intent = Intent(this, CameraActivity::class.java)

        val inParameter = intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        inParameter.putExtra("usuario", usermail)
        inParameter.putExtra("dateRun", dateRun)

        startActivity(intent)
    }
}