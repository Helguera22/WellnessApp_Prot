package com.helgar.wellnessappprot

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class DocInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_info)

        val nombreMedico = intent.getStringExtra("nombre")
        val especialidadMedico = intent.getStringExtra("especialidad")

        // Obtener las SharedPreferences
        val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        val docNom = findViewById<TextView>(R.id.docNameInfo)
        val docEsp = findViewById<TextView>(R.id.docSpecInfo)

        // Obtener el valor de la cedula y el nss desde las SharedPreferences
        val nss = sharedPreferences.getString("nss", "")
        val cedula = sharedPreferences.getString("cedula", "")

        docNom.text = "$nombreMedico"
        docEsp.text = "$especialidadMedico"

        val btn_agenda = findViewById<Button>(R.id.button2)

        btn_agenda.setOnClickListener{

            val intent = Intent(this, DoctorDate::class.java)
            intent.putExtra("cedula", cedula)
            intent.putExtra("nss", nss)
            intent.putExtra("nombre", nombreMedico)
            startActivity(intent)
        }
    }
}