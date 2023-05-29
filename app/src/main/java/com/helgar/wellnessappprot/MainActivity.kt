package com.helgar.wellnessappprot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val correo = sharedPref.getString("correo_electronico", "")
        val password = sharedPref.getString("psw", "")

        // Validar que las credenciales no están vacías
        if (correo.isEmpty() && password.isEmpty()) {
            // Enviar las credenciales al servidor utilizando Volley
            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.0.111:8080/wellnessappDb/docLogin.php"

            val request = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    Log.d("MainActivity", "Response: $response")
                    // Si la autenticación es exitosa, continuar con la actividad principal
                    val intent = Intent(this, MainDoctor1::class.java)
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener { error ->
                    Log.e("MainActivity", "Error: ${error.message}")
                    // Si la autenticación falla, redirigir al usuario a la actividad de inicio de sesión
                    val intent = Intent(this, TipoU::class.java)
                    startActivity(intent)
                    finish()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["correo_electronico"] = correo
                    params["psw"] = password
                    return params
                }
            }

            queue.add(request)
        } else {
            // Si las credenciales están vacías, redirigir al usuario a la actividad de inicio de sesión
            val intent = Intent(this, TipoU::class.java)
            startActivity(intent)
            finish()
        }*/


        val btn_comenzar: Button = findViewById(R.id.comenzar)
        btn_comenzar.setOnClickListener {

            val intent = Intent(this, TipoU:: class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }
}