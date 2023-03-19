package clases

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

/**

Clase que representa a un usuario de la aplicación.
Contiene información personal del usuario, como nombre, apellidos, DNI, fecha de nacimiento, género,
nacionalidad, país, provincia, código postal, número de teléfono móvil, carnet de conducir, transporte propio,
movilidad geográfica y foto de perfil.
 */
class Usuario : Parcelable {

    var nombre: String? = null
    var apellidos: String? = null
    var dni: String? = null
    var fecha_nacimiento: Timestamp? = null
    var genero: String? = null
    var nacionalidad: String? = null
    var pais: String? = null
    var provincia: String? = null
    var cp: Int? = null
    var movil: String? = null
    var carnet_conducir: Boolean? = null
    var transporte_propio: Boolean? = null
    var movilidad_geografica: Boolean? = null
    var foto_perfil: String? = null

    /**

    Constructor primario vacío de la clase Usuario.
     */
    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        apellidos = parcel.readString()
        dni = parcel.readString()

        genero = parcel.readString()
        nacionalidad = parcel.readString()
        pais = parcel.readString()
        provincia = parcel.readString()
        cp = parcel.readByte().toInt()
        movil = parcel.readString()
        carnet_conducir = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
        transporte_propio = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
        foto_perfil = parcel.readString()
    }

    /**

    Constructor secundario de la clase Usuario.
    @param nombre El nombre del usuario.
    @param apellidos Los apellidos del usuario.
    @param dni El DNI del usuario.
    @param fecha_nacimiento La fecha de nacimiento del usuario en formato Timestamp.
    @param genero El género del usuario.
    @param nacionalidad La nacionalidad del usuario.
    @param pais El país de residencia del usuario.
    @param provincia La provincia de residencia del usuario.
    @param cp El código postal de residencia del usuario.
    @param movil El número de teléfono móvil del usuario.
    @param carnet_conducir Indica si el usuario tiene carnet de conducir o no.
    @param transporte_propio Indica si el usuario tiene transporte propio o no.
    @param movilidad_geografica Indica si el usuario tiene movilidad geográfica o no.
    @param foto_perfil La ruta de la foto de perfil del usuario.
     */

    constructor(
        nombre: String?,
        apellidos: String?,
        dni: String?,
        fecha_nacimiento: Timestamp?,
        genero: String?,
        nacionalidad: String?,
        pais: String?,
        provincia: String?,
        cp: Int?,
        movil: String?,
        carnet_conducir: Boolean?,
        transporte_propio: Boolean?,
        movilidad_geografica: Boolean?,
        foto_perfil: String?
    ) {
        this.nombre = nombre
        this.apellidos = apellidos
        this.dni = dni
        this.fecha_nacimiento = fecha_nacimiento
        this.genero = genero
        this.nacionalidad = nacionalidad
        this.pais = pais
        this.provincia = provincia
        this.cp = cp
        this.movil = movil
        this.carnet_conducir = carnet_conducir
        this.transporte_propio = transporte_propio
        this.movilidad_geografica = movilidad_geografica
        this.foto_perfil = foto_perfil
    }

    constructor(foto_perfil: String?) {
        this.foto_perfil = foto_perfil
    }

    constructor() {
        // constructor vacío
    }


    /**

    Función que se utiliza para escribir los datos del usuario en un objeto Parcel.
    @param parcel El objeto Parcel en el que se escribirán los datos.
    @param flags Los flags que indican cómo debe ser escrito el objeto.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(apellidos)
        parcel.writeString(dni)
        parcel.writeString(genero)
        parcel.writeString(nacionalidad)
        parcel.writeString(pais)
        parcel.writeString(provincia)
        cp?.let { parcel.writeByte(it.toByte()) }
        parcel.writeString(movil)
        parcel.writeValue(carnet_conducir)
        parcel.writeValue(transporte_propio)
        parcel.writeValue(movilidad_geografica)
        parcel.writeString(foto_perfil)
    }

    /**

    Función que devuelve el tipo de contenido de la clase.
    @return El tipo de contenido de la clase.
     */
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuario> {
        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }

        /**

        Método utilizado para crear un objeto de la clase Usuario a partir de un objeto DocumentSnapshot de Firebase.
        @param documentSnapshot El objeto DocumentSnapshot de Firebase del que se obtendrá la información.
        @return El objeto Usuario creado a partir del objeto DocumentSnapshot.
         */
        fun fromDocumentSnapshot(documentSnapshot: DocumentSnapshot): Usuario {
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

            return Usuario(
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
        }
    }

}