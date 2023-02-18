package clases

import android.os.Parcel
import android.os.Parcelable

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Evento : Parcelable{

    /* VARIABLES QUE SE IMPLEMENTARÁN MÁS ADELANTE, SE HACE ESTO PARA EL EJERCICIO RECYCLERVIEW
    var empresaCreadora:String?,

    var resquisitos: String?,

    var descripcion: String?,
    var tipoEmpleado: String?,
    */
    var tipoEvento:String? =null

    var ubicacion: String? =null

    var fechaEvento: LocalDate? =null

    var nVacantes: Byte? =null

    constructor(parcel: Parcel) : this() {
        tipoEvento = parcel.readString()
        ubicacion = parcel.readString()

        nVacantes = parcel.readValue(Byte::class.java.classLoader) as? Byte
    }

    constructor(tipoEvento:String,ubicacion:String,fechaEvento:LocalDate,nVacantes:Byte):
            this(){
                this.tipoEvento=tipoEvento
                this.ubicacion=ubicacion
                this.nVacantes=nVacantes
                this.fechaEvento=fechaEvento
            }

    constructor(){

        val random:Random= Random()
        val eventosPosibles=arrayOf<String>("Camerero","Repartidor","Azafato","Coordinador","Azafata","Camarera","Coordinadora","Promotor","Promotora","Recepcionista","Seguridad","Logistica",)

        val vacantesPosibles=arrayOf<Byte>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)
        val ubicacionesPosibles = arrayOf("Barcelona, Catalonia", "Madrid, Castilla y León", "Sevilla, Andalusia", "Valencia, Valencia", "Zaragoza, Aragon", "Málaga, Andalusia", "Murcia, Murcia", "Palma de Mallorca, Balearic Islands", "Las Palmas de Gran Canaria, Canary Islands", "Bilbao, Basque Country")
        this.fechaEvento= LocalDate.of(LocalDate.now().year-100+random.nextInt(100),1+random.nextInt(12),1+random.nextInt(28))
        this.tipoEvento=eventosPosibles[random.nextInt(eventosPosibles.size)]
        this.nVacantes=vacantesPosibles[random.nextInt(vacantesPosibles.size)]
        this.ubicacion=ubicacionesPosibles[random.nextInt(ubicacionesPosibles.size)]


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tipoEvento)
        parcel.writeString(ubicacion)
        parcel.writeValue(nVacantes)
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