package clases

import com.google.firebase.Timestamp
import java.time.LocalDate

class Evento (
    var tipoEvento:String,
    var ubicacion: String,
    var fechaEvento: LocalDate,
    var tipoEmpleado: String,
    var nVacantes: UShort,
    var resquisitos: String,
    var descripcion: String
    ){


}