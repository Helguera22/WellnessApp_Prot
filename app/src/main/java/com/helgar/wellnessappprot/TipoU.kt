package com.helgar.wellnessappprot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TipoU : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipo_u)

        val btnpaciente: Button = findViewById(R.id.paciente)

        btnpaciente.setOnClickListener {
            val intent = Intent(this, UserLog::class.java)
            startActivity(intent)
        }

        val btnmedico: Button = findViewById(R.id.medico)

        btnmedico.setOnClickListener {
            val intent = Intent(this, Doctorlogin::class.java)
            startActivity(intent)
        }
    }
}