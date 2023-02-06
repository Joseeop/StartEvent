package com.example.startevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_activity_terms)

        val arrayAdapter:ArrayAdapter<String>


        val terminos = mutableListOf("COOKIES\n " +
                "Empleamos el usos de cookies. Al utilizar el sitio web de (Nombre de la tienda), usted acepta el uso de cookies de acuerdo con la política de privacidad de (Nombre de la tienda). La mayoría de los modernos sitios web interactivos de hoy en día usan cookies para permitirnos recuperar los detalles del usuario para cada visita. ",
            "LICENCIA \n No debes:\n" +
                    "\n" +
                    "Volver a publicar material desde (Añadir URL).\n" +
                    "Vender, alquilar u otorgar una sub-licencia de material desde (Agregar URL).\n" +
                    "Reproducir, duplicar o copiar material desde (Agregar URL).\n" +
                    "Redistribuir contenido de (Nombre de la tienda), a menos de que el contenido se haga específicamente para la redistribución. ",
        "AVISO LEGAL\n Limita o excluye nuestra o su responsabilidad por muerte o lesiones personales resultantes de negligencia.\n" +
                "Limita o excluye nuestra o su responsabilidad por fraude o tergiversación fraudulenta.\n" +
                "Limita cualquiera de nuestras o sus responsabilidades de cualquier manera que no esté permitida por la ley aplicable.\n" +
                "Excluye cualquiera de nuestras o sus responsabilidades que no pueden ser excluidas bajo la ley aplicable. ",
            "AVISO LEGAL\n Limita o excluye nuestra o su responsabilidad por muerte o lesiones personales resultantes de negligencia.\n" +
                    "Limita o excluye nuestra o su responsabilidad por fraude o tergiversación fraudulenta.\n" +
                    "Limita cualquiera de nuestras o sus responsabilidades de cualquier manera que no esté permitida por la ley aplicable.\n" +
                    "Excluye cualquiera de nuestras o sus responsabilidades que no pueden ser excluidas bajo la ley aplicable. ")

        val terms_list_view = findViewById<ListView>(R.id.terms_list_view)

        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,terminos)
        terms_list_view.adapter = arrayAdapter

        terms_list_view.setOnItemClickListener(){parent,view,position,id->
            Toast.makeText(this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show()
        }
    }
}