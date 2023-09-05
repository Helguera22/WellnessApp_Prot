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
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.ui.MainUser1
import java.util.HashMap
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec

class UserRegister : AppCompatActivity() {
    private val REQUEST_SELECT_IMAGE = 100
    private var selectedImageUri: Uri? = null

    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_user_register)
        // En el método onCreate

        val edt_nss = findViewById<EditText>(R.id.editTextTextNss)
        val edt_nombre = findViewById<EditText>(R.id.editTextTextPersonName)
        val edt_fec_nac = findViewById<EditText>(R.id.editTextDate)
        val edt_telefono = findViewById<EditText>(R.id.editTextNumber)
        val edt_correo = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val edt_password = findViewById<EditText>(R.id.editTextTextPassword2)
        val btn_registrar = findViewById<Button>(R.id.button)

        // Obtener las SharedPreferences
        val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        val btnSelectImage = findViewById<Button>(R.id.buttonAdd1)
        val imageView = findViewById<ImageView>(R.id.imageViewUser)

        btn_registrar.setOnClickListener {
            val nss = edt_nss.text.toString().trim()
            val nombre = edt_nombre.text.toString().trim()
            val fech = edt_fec_nac.text.toString().trim()
            val telefono = edt_telefono.text.toString().trim()
            val correo = edt_correo.text.toString().trim()
            val password = edt_password.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (nss.isEmpty() || nombre.isEmpty() || fech.isEmpty() || telefono.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar los datos al servidor utilizando Volley
                val queue = Volley.newRequestQueue(this)
                val url = "http://ictuscorps.atwebpages.com/anime-main/php/userLogin.php"

                val request = object : StringRequest(Request.Method.POST, url,
                    Response.Listener { response ->
                        Log.d("UserRegister", "Response: $response")
                        val intent = Intent(this, MainUser1::class.java)
                        // Guardar el valor de la cedula en las SharedPreferences
                        sharedPreferences.edit().putString("nss", nss).apply()
                        startActivity(intent)
                        Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    Response.ErrorListener { error ->
                        Log.e("UserRegister", "Error: ${error.message}")
                        Toast.makeText(this, "Error al registrar paciente: ${error.message}", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["nss"] = nss
                        params["nombre_completo"] = nombre
                        params["fecha_nacimiento"] = fech
                        params["telefono"] = telefono
                        params["correo_electronico"] = correo
                        params["psw"] = password
                        return params
                    }
                }

                queue.add(request)
            }
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            val imageView = findViewById<ImageView>(R.id.imageViewUser)
            imageView.setImageURI(selectedImageUri)
        }
    }
}