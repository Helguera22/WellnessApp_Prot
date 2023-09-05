package com.helgar.wellnessappprot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.ui.MainUser1
import java.util.*
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
import javax.crypto.spec.SecretKeySpec

class UserLog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_log)

        val edt_correo = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val edt_password = findViewById<EditText>(R.id.editTextTextPassword)
        val btn_entrar = findViewById<Button>(R.id.button_entrar)
        val btnregister = findViewById<TextView>(R.id.textViewreg)

        // Verificar si hay una sesión activa antes de iniciar sesión
        val sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn == true) {
            val intent = Intent(this, MainUser1::class.java)
            startActivity(intent)
            finish()
        }

        btn_entrar.setOnClickListener {
            val correo = edt_correo.text.toString().trim()
            val password = edt_password.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar los datos al servidor utilizando Volley
                val queue = Volley.newRequestQueue(this)
                val url = "http://ictuscorps.atwebpages.com/anime-main/php/userLogin.php"

                val request = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { response ->
                        Log.d("RESPONSE", response)

                        if (response == "Todo está correctoInicio de sesión exitoso") {
                            // Inicio de sesión exitoso

                            // Guardar el estado de la sesión en las SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()

                            // Redirigir a la actividad principal
                            val intent = Intent(this, MainUser1::class.java)
                            intent.putExtra("user_email", correo) // Pasamos el correo a la siguiente actividad
                            startActivity(intent)
                            finish()
                        } else {
                            // Credenciales inválidas
                            Toast.makeText(this, "Correo o contraseña inválidos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("ERROR", error.toString())
                        Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["correo_electronico"] = correo
                        params["psw"] = password
                        return params
                    }
                }
                queue.add(request)
            }
        }

        btnregister.setOnClickListener {
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
        }
    }
}