package clases

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot


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
        //TODO PREGUNTAR A MIGUEL EL CRASHEO
       // movilidad_geografica = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
        foto_perfil = parcel.readString()
    }

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

            return Usuario(nombre, apellidos, dni, fecha_nacimiento, genero, nacionalidad, pais, provincia, cp, movil, carnet_conducir, transporte_propio, movilidad_geografica, foto_perfil)
        }
    }

}