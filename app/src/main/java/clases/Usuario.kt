package clases

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

class Usuario : Parcelable {

    var nombre: String? = null
    var apellidos: String? = null
    var dni: String? = null
    var fecha_nacimiento: Timestamp? = null
    var genero: String? = null
    var nacionalidad: String? = null
    var pais: String? = null
    var provincia: String? = null
    var cp: Short? = null
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
        cp = parcel.readByte().toShort()
        movil = parcel.readString()
        carnet_conducir = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
        transporte_propio = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
        movilidad_geografica = parcel.readValue(Boolean::class.java.classLoader) as Boolean?
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
        cp: Short?,
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
    }
}