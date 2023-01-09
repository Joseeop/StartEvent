package com.example.startevent

import java.util.regex.Matcher
import java.util.regex.Pattern

class ValidateEmail {

    companion object{
        var pat : Pattern?=null
        var mat: Matcher?= null

        //Función para comprobar si el email introducido es válido.
        //(Tremenda fumada)
        fun isEmail(email:String): Boolean{
            pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$")
            mat = pat!!.matcher(email)
            return mat!!.find()
        }
    }

}