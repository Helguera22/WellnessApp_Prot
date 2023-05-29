package com.helgar.wellnessappprot

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DateInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_info)

        val nombrePac = intent.getStringExtra("nombre")
        val notaPac = intent.getStringExtra("nota")
        val horaPac = intent.getStringExtra("hora")

        val pacNom = findViewById<TextView>(R.id.pacientNameInfo)
        val pacNota = findViewById<TextView>(R.id.pacientNoteInfo)
        val pacHora = findViewById<TextView>(R.id.pacientTimeInfo)

        pacNom.text = "$nombrePac"
        pacNota.text = "Motivo: $notaPac"
        pacHora.text = "Hora: $horaPac"
    }
}