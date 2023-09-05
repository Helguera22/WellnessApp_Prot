package com.helgar.wellnessappprot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Diagnostico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostico)

        // Recupera los datos del Intent
        val intent = intent
        if (intent.hasExtra("respuestaAPI")) {
            val respuestaJSON = intent.getStringExtra("respuestaAPI")

            // Verificar si la respuesta JSON no es nula
            if (respuestaJSON != null) {
                try {
                    // Convertir la respuesta JSON en un arreglo de arreglos
                    val respuestaArray = JSONArray(respuestaJSON)

                    // Verificar si hay al menos un elemento en el arreglo de respuesta
                    if (respuestaArray.length() > 0) {
                        // Obtener el primer elemento del arreglo de respuesta
                        val resultados = respuestaArray.getJSONArray(0)

                        // Definir los nombres de las enfermedades en el mismo orden que los valores
                        val nombresEnfermedades = arrayOf("Bronquitis", "Resfriado", "Infección de Oído", "Influenza", "Sinusitis")

                        // Inicializar la variable de enfermedad encontrada como vacía
                        var enfermedadEncontrada = ""

                        // Recorrer las posiciones del arreglo de valores
                        for (i in 0 until resultados.length()) {
                            try {
                                val valor = resultados.getDouble(i)

                                // Verificar si el valor es mayor o igual a 0.65
                                if (valor in 0.5..1.0) {
                                    // Asociar el valor encontrado con la enfermedad correspondiente
                                    enfermedadEncontrada = nombresEnfermedades[i]

                                    break // Salir del bucle si se encuentra un valor que cumple la condición
                                }
                            } catch (e: JSONException) {
                                // Manejar la excepción si el valor no es numérico (omitir el valor no numérico)
                                Log.e("Diagnostico", "Error al procesar valor en la posición $i: ${e.message}")
                            }
                        }

                        // Mostrar la enfermedad encontrada en un TextView
                        val textViewEnfermedad = findViewById<TextView>(R.id.textViewPred)
                        if (enfermedadEncontrada.isNotEmpty()) {
                            textViewEnfermedad.text = "$enfermedadEncontrada"
                        } else {
                            textViewEnfermedad.text = "No se encontró una enfermedad válida"
                        }
                    } else {
                        Log.e("Diagnostico", "El arreglo de resultados está vacío")
                    }
                } catch (e: JSONException) {
                    // Manejar la excepción si no se puede convertir la respuesta en un arreglo JSON
                    Log.e("Diagnostico", "Error al procesar la respuesta JSON: ${e.message}")
                }
            } else {
                Log.e("Diagnostico", "La respuesta JSON es nula")
            }

        }
    }
}