package com.example.startevent


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var btnGoogle : Button
    //Este linear layout al principio estará oculto.
    private lateinit var lyTerms: LinearLayout

    private lateinit var mAuth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //Código para comprobar que todo ha ido bien en el inicio de sesión
    private var RESULT_CODE_GOOGLE_SIGN_IN =50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)






        //Ponemos layaout por defecto en invisible.
        lyTerms=findViewById(R.id.lyTerms)
        lyTerms.visibility = android.view.View.INVISIBLE
        btnGoogle=findViewById(R.id.btnGoogle)
        etEmail=findViewById(R.id.etEmail)
        etPass=findViewById(R.id.etPass)
        //Aquí tenemos la instancia con la que podremos operar.
        mAuth=FirebaseAuth.getInstance()

        //llamada a la función

        manageButtonLogin()
        etEmail.doOnTextChanged { text, start, before, count -> manageButtonLogin()  }
        etPass.doOnTextChanged { text, start, before, count -> manageButtonLogin()  }



    }


    /**
     * Sobrescribimos la función onStart para manipular su flujo.
     * 1.Si el usuario ya está logueado lo mandamos directamente a la pantalla home.
     */
    public override fun onStart(){
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser

        //Comprobamos si el usuario no es null, en caso que no lo sea le mandamos a la pantalla de inicio
        //Con su email y proveedor
        if (currentUser != null) goHome(currentUser.email.toString(),currentUser.providerId)
       // updateUI(currentUser)

    }



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

    private fun manageButtonLogin(){
        var tvLogin = findViewById<TextView>(R.id.tvLogin)
        email = etEmail.text.toString()
        password = etPass.text.toString()

        if(TextUtils.isEmpty(password) ||ValidateEmail.isEmail(email)==false){
            tvLogin.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
            tvLogin.isEnabled = false

        }else{
            tvLogin.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
            tvLogin.isEnabled = true
        }
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
                    //Comprobamos exactamente el error de registro
                    //Mi Login: 12345678
                    Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
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
        //startActivity(Intent(this,ForgotPasswordActivity::class.java))
        resetPassword()
    }

    /**
     * Función para restablecer contraseña. No será necesario que el usuario vuelva a escribir su correo al que se le enviará la contraseña.
     * Pues lo capturaremos y se lo enviaremos directamente, tratando así de hacer menos tedioso el proceso.
     */
    private fun resetPassword(){
        var e = etEmail.text.toString()
        //COMPROBAMOS QUE EL CAMPO EMAIL NO ESTÁ VACÍO PARA: Enviar un correo de reseteo de pass y comprobar si el correo existe.
        if(!TextUtils.isEmpty(e)){
            mAuth.sendPasswordResetEmail(e)
                .addOnCompleteListener{
                    //SI QUEREMOS HACER ESTO EN OTRA VENTANA, DEBEREMOS MANDARLE EN UN INTENT A LA PANTALLA DE LOGIN
                    if(it.isSuccessful)Toast.makeText(this, "Email enviado a "+e,Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "No se encontró usuario con el correo: "+e,Toast.LENGTH_SHORT).show()

                }
        }else Toast.makeText(this, "Indica un email.",Toast.LENGTH_SHORT).show()
    }



     /** LOGIN CON GOOGLE.
      * Antes de nada, debemos irnos a Firebase para ingresar nuestra huella digital de la app. Posteriormente:
      * View->Tools Windows->Gradle->signingReport, de ahí cogemos el código SHA1 para pegar en nuestra app en Firebase.
      * También debemos estar logueados en la cuenta de google en el dispositivo
      * sincroniza las dependencias que ofrece la documentación de google en gradle module
      */

    fun callSignInGoogle (view:View){
        signInGoogle()
    }
    private fun signInGoogle(){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)
        //Cerramos sesión por si hubiese alguna ya logueada.
        googleSignInClient.signOut()

        startActivityForResult(googleSignInClient.signInIntent, RESULT_CODE_GOOGLE_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_CODE_GOOGLE_SIGN_IN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Si la cuenta no es nula marcamos el email
                //guardamos las credenciales con el token que hemos redibido
                //y hacemos el signin con nuestros credenciales del firebase
                //Si es exitoso el login lo mandamos a la pantalla home.
                if (account != null){
                    email = account.email!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    mAuth.signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful) goHome(email, "Google")
                        else Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT)

                    }
                }


            } catch (e: ApiException) {
                Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT)
            }
        }

    }
    //FINAL DEL LOGIN CON GOOGLE.


}