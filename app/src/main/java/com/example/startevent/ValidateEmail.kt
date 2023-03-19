package com.example.startevent

import java.util.regex.Matcher
import java.util.regex.Pattern

class ValidateEmail {

    companion object {
        // Variables pat y mat para almacenar la expresión regular y el resultado del matcher
        private var pat: Pattern? = null
        private var mat: Matcher? = null

        /**
         * Función que valida si una cadena de texto es una dirección de correo electrónico válida.
         *
         * @param email Dirección de correo electrónico a validar.
         *
         * @return True si la dirección de correo es válida, false en caso contrario.
         */
        fun isEmail(email: String): Boolean {
            // Patrón para validar direcciones de correo electrónico
            pat =
                Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$")
            // Matcher para verificar si la cadena de texto coincide con el patrón
            mat = pat!!.matcher(email)
            // Retorna true si la cadena de texto coincide con el patrón, false en caso contrario.
            return mat!!.find()
        }
    }
}