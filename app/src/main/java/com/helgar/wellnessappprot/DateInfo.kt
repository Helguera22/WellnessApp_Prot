package com.helgar.wellnessappprot

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView

class DateInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_info)

        val nombrePac = intent.getStringExtra("nombre")
        // val notaPac = intent.getStringExtra("nota")
        val horaPac = intent.getStringExtra("hora")

        val pacNom = findViewById<TextView>(R.id.pacientNameInfo)
        // val pacNota = findViewById<TextView>(R.id.pacientNoteInfo)
        val pacHora = findViewById<TextView>(R.id.txtHora)
        val btn_card = findViewById<Button>(R.id.buttonWellcard)
        val btn_atender = findViewById<Button>(R.id.buttonAtender)

        pacNom.text = "$nombrePac"
        // pacNota.text = "Motivo: $notaPac"
        pacHora.text = "$horaPac"

        btn_card.setOnClickListener {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = sharedPreferences.edit()
            editor.putString("pacNom", nombrePac)
            editor.apply()

            val intent = Intent(this, WellCard::class.java)
            startActivity(intent)
        }

        btn_atender.setOnClickListener {
            val intent1 = Intent(this, Consulta::class.java)
            startActivity(intent1)
        }
    }
}