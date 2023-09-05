package com.helgar.wellnessappprot

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Declarar respuesta fuera del bloque if
        var respuesta: String? = null

        // Recupera los datos del Intent
        val intent = intent
        if (intent.hasExtra("respuestaAPI")) {
            respuesta = intent.getStringExtra("respuestaAPI")
        }

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Configurar el objeto animador
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 9000 // Duración en milisegundos
        animator.start()

        // Simulación de una tarea que lleva tiempo
        Handler().postDelayed({
            // Finalizar la actividad de carga y pasar a la siguiente actividad
            val intent = Intent(this, Diagnostico::class.java)
            intent.putExtra("respuestaAPI", respuesta) // Pasa la respuesta como extra
            startActivity(intent)
            finish()
        }, 9000) // Simulación de tiempo en milisegundos (por ejemplo, 3 segundos)
    }
}