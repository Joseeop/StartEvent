package clases

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

import java.util.*

class Evento : Parcelable{

    var id: String? = null
    var tipo_empleado: String?=null
    var tipoEvento:String? =null
    var empresa:String? = null
    var ubicacion: String? =null
    var fecha_evento: Timestamp? =null
    var requisitos: String? = null
    var vacantes: String? =null
    var descripcion: String? = null
    var lista_inscritos: List<String>? = null  // Nuevo campo para la lista de inscritos

    constructor(parcel: Parcel) : this() {
        tipoEvento = parcel.readString()
        ubicacion = parcel.readString()
        vacantes = parcel.readString()
    }

    constructor(tipoEvento:String,ubicacion:String,nVacantes:String,
                descripcion:String,requisitos:String,fecha_evento:Timestamp,empresa:String,tipoEmpleado:String):
            this(){
                this.tipoEvento=tipoEvento
                this.ubicacion=ubicacion
                this.vacantes=nVacantes
                this.fecha_evento=fecha_evento
                this.descripcion=descripcion
                this.requisitos=requisitos
                this.empresa=empresa
                this.tipo_empleado=tipoEmpleado
            }

    constructor(){

        val random:Random= Random()
        val eventosPosibles=arrayOf<String>("Camerero","Repartidor","Azafato","Coordinador","Azafata","Camarera","Coordinadora","Promotor","Promotora","Recepcionista","Seguridad","Logistica",)

        val vacantesPosibles=arrayOf<Byte>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)
        val ubicacionesPosibles = arrayOf("Barcelona, Catalonia", "Madrid, Castilla y León", "Sevilla, Andalusia", "Valencia, Valencia", "Zaragoza, Aragon", "Málaga, Andalusia", "Murcia, Murcia", "Palma de Mallorca, Balearic Islands", "Las Palmas de Gran Canaria, Canary Islands", "Bilbao, Basque Country")
        this.fecha_evento= null
        this.tipoEvento=""
        this.vacantes=""+vacantesPosibles[random.nextInt(vacantesPosibles.size)]
        this.ubicacion=ubicacionesPosibles[random.nextInt(ubicacionesPosibles.size)]
        this.descripcion=""
        this.requisitos=""
        this.empresa=""
        this.tipo_empleado=""

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tipoEvento)
        parcel.writeString(ubicacion)
        parcel.writeValue(vacantes)
        parcel.writeString(descripcion)
        parcel.writeString(requisitos)
        parcel.writeString(tipo_empleado)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Evento> {
        override fun createFromParcel(parcel: Parcel): Evento {
            return Evento(parcel)
        }

        override fun newArray(size: Int): Array<Evento?> {
            return arrayOfNulls(size)
        }
    }


}