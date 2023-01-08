package com.example.startevent


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    companion object{
        lateinit var usermail: String
        lateinit var providerSession: String
    }

    //Poniendo Delegates.notNull nos aseguramos de que ese dato no pueda ser null
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail :EditText
    private lateinit var etPass : EditText
    //Este linear layout al principio estará oculto.
    private lateinit var lyTerms: LinearLayout

    private lateinit var mAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Ponemos layaout por defecto en invisible.
        lyTerms=findViewById(R.id.lyTerms)
        lyTerms.visibility = android.view.View.INVISIBLE

        etEmail=findViewById(R.id.etEmail)
        etPass=findViewById(R.id.etPass)
        //Aquí tenemos la instancia con la que podremos operar.
        mAuth=FirebaseAuth.getInstance()


    }


    /**
     * Sobrescribimos la función onStart para manipular su flujo.
     * 1.Si el usuario ya está logueado lo mandamos directamente a la pantalla home.
     */
    /*public override fun onStart(){
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser

        //Comprobamos si el usuario no es null, en caso que no lo sea le mandamos a la pantalla de inicio
        //Con su email y proveedor
        if (currentUser != null) goHome(currentUser.email.toString(),currentUser.providerId)

    }*/

    /**
     * Sobrescribimos la función onBackPressed. Para cuando el usuario pulse el botón
     * "Atrás" se le lleve a ventana de inicio de su dispositivo.
     */
    override fun onBackPressed() {

        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
    /**
     * Función pública login a la que llamaremos desde el botón "Iniciar sesión".
     * llamaremos a la función privada loginUser
     */
    fun login (view:View){
        loginUser()
    }
    private fun loginUser(){
        email = etEmail.text.toString()
        password = etPass.text.toString()


        mAuth.signInWithEmailAndPassword(email,password)
            //Cambiamos el it de la lamda por "task" para utilizarla más adelante.
            .addOnCompleteListener(this){task ->
                //Si existe el email y la contraseña lo mandaremos a la pantalla principal
                if(task.isSuccessful){
                    goHome(email, "email")
                    //En el else caben dos opciones: Que haya introducido mal los datos o que ese usuario sea nuevo
                }else{
                    //Ponemos términos en visible para que los acepte en caso de que el usuario no exista.

                    if(lyTerms.visibility == android.view.View.INVISIBLE) {
                        Toast.makeText(this,"Tienes que aceptar los términos y condiciones",Toast.LENGTH_LONG).show()
                        lyTerms.visibility = View.VISIBLE
                    }
                    else{

                        //Si ya está marcado el checkBox de aceptar terminos lo mandaremos a la función register.
                        var cbAcept = findViewById<CheckBox>(R.id.cbAcept)
                        if(cbAcept.isChecked) register()
                    }
                }
            }
    }

    /**
     * Función que llevará a la pantalla de inicio de la aplicación una vez el usuario se haya registrado
     * Recibirá por parámetros email y provedor de registro que se utilizó para iniciar sesión.
     */
    private fun goHome(email:String , provider: String){

        usermail = email
        providerSession = provider
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Función que hará dos cosas:
     * 1.creará un nuevo usuario con la contraseña que haya indicado.
     * 2.añadir un nuevo documento a la colección "users" de la base de datos.
     * No recibirá parámetros porque capturaremos los valores de los EditText correspondientes.
     */
    private fun register(){
        email = etEmail.text.toString()
        password = etPass.text.toString()

        //Le pasamos email y pass para crear un nuevo usuario.
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){

                    //Guardamos su fecha de registro.
                    var dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    var dbRegister = FirebaseFirestore.getInstance()
                    dbRegister.collection("users").document(email).set(hashMapOf(
                         "user" to email,
                         "DateRegister" to dateRegister
                    ))
                    //Lo mandamos a la pantalla de inicio.
                    goHome(email,"email")
                }else{
                    Toast.makeText(this,"Error. Algo ha ido mal.",Toast.LENGTH_SHORT).show()
                }

            }
    }

    /**
     * Función goTerms que llevará al usuario a los términos y condiciones de uso al pulsar sobre el evento.
     */
    fun goTerms (v:View){

        val intent = Intent (this, TermsActivity::class.java)
        startActivity(intent)
    }

    fun forgotPassword(view : View){
        startActivity(Intent(this,ForgotPasswordActivity::class.java))
    }


}