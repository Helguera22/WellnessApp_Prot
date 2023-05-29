package com.helgar.wellnessappprot

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.*
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.ui.MainDoctor1

class DoctorRegister : AppCompatActivity() {

    private val REQUEST_SELECT_IMAGE = 100
    private var selectedImageUri: Uri? = null

    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_doctor_register)
        // En el método onCreate

        val edt_cedula = findViewById<EditText>(R.id.editTextCedula)
        val edt_nombre = findViewById<EditText>(R.id.editTextTextdocName)
        val edt_especialidad = findViewById<EditText>(R.id.editTextTextEspec)
        val edt_correo = findViewById<EditText>(R.id.editTextCorreo)
        val edt_password = findViewById<EditText>(R.id.editTextdocPsw)
        val btn_registrar = findViewById<Button>(R.id.buttondocReg)

        // Obtener las SharedPreferences
        val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        btn_registrar.setOnClickListener {
            val cedula = edt_cedula.text.toString().trim()
            val nombre = edt_nombre.text.toString().trim()
            val especialidad = edt_especialidad.text.toString().trim()
            val correo = edt_correo.text.toString().trim()
            val password = edt_password.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (cedula.isEmpty() || nombre.isEmpty() || especialidad.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar los datos al servidor utilizando Volley
                val queue = Volley.newRequestQueue(this)
                val url = "http://192.168.15.124:8080/wellnessappDb/docRegister.php"

                val request = object : StringRequest(Request.Method.POST, url,
                    Response.Listener { response ->
                        Log.d("DoctorRegister", "Response: $response")
                        val intent = Intent(this, MainDoctor1::class.java)
                        // Guardar el valor de la cedula en las SharedPreferences
                        sharedPreferences.edit().putString("cedula", cedula).apply()
                        startActivity(intent)
                        Toast.makeText(this, "Doctor registrado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    Response.ErrorListener { error ->
                        Log.e("DoctorRegister", "Error: ${error.message}")
                        Toast.makeText(this, "Error al registrar doctor: ${error.message}", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["cedula"] = cedula
                        params["nombre_completo"] = nombre
                        params["especialidad"] = especialidad
                        params["correo_electronico"] = correo
                        params["psw"] = password
                        return params
                    }
                }

                queue.add(request)
            }
        }

        val btnSelectImage = findViewById<Button>(R.id.buttonAdd)
        val imageView = findViewById<ImageView>(R.id.imageView22)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            val imageView = findViewById<ImageView>(R.id.imageView22)
            imageView.setImageURI(selectedImageUri)
        }
    }
}