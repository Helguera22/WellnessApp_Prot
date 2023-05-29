package com.helgar.wellnessappprot

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.ui.MainUser1
import java.util.HashMap

class DateDetails : AppCompatActivity() {

    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var tvConsultorio: TextView
    private lateinit var tvMotivo: TextView
    private lateinit var tvCedula: TextView
    private lateinit var tvNss: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_details)

        tvFechaSeleccionada = findViewById(R.id.dateSelected)
        tvHoraSeleccionada = findViewById(R.id.timeSelected)
        tvConsultorio = findViewById(R.id.consultorioSelected)
        tvMotivo = findViewById(R.id.reasonSelected)
        tvCedula = findViewById(R.id.cedula)
        tvNss = findViewById(R.id.nss)
        val btn_confirmar = findViewById<Button>(R.id.confirm_button)

        val selectedDate = intent.getStringExtra("selected_date")
        val selectedTime = intent.getStringExtra("selected_time")
        val cedulaMedico = intent.getStringExtra("cedula")
        val nssPaciente = intent.getStringExtra("nss")
        val nombreMedico = intent.getStringExtra("nombre")
        //cedulaMedico = intent.getStringExtra("cedula")
        //nssPaciente = intent.getStringExtra("nss")
        val selectedReas = intent.getStringExtra("Razon")

        // Obtener las SharedPreferences
        val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        tvMotivo.text = "$selectedReas"
        tvConsultorio.text = "$nombreMedico"
        tvFechaSeleccionada.text = "$selectedDate"
        tvHoraSeleccionada.text = "$selectedTime"
        tvCedula.text = "$cedulaMedico"
        tvNss.text = "$nssPaciente"

        btn_confirmar.setOnClickListener {
            val fecha = tvFechaSeleccionada.text.toString().trim()
            val hora = tvHoraSeleccionada.text.toString().trim()
            val cedula = tvCedula.text.toString().trim()
            val nss = tvNss.text.toString().trim()
            val motivo = tvMotivo.text.toString().trim()

            val queue = Volley.newRequestQueue(this)
            val url = "http://192.168.15.124:8080/wellnessappDb/saveDate.php"

            val request = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Log.d("DateDetails", "Response: $response")
                    val intent = Intent(this, MainUser1::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Cita programada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                },
                Response.ErrorListener { error ->
                    Log.e("DateDetails", "Error: ${error.message}")
                    Toast.makeText(this, "Error al agendar cita: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["fecha"] = fecha
                    params["hora"] = hora
                    params["doctor_cedula"] = cedula
                    params["paciente_nss"] = nss
                    params["nota"] = motivo
                    return params
                }
            }

            queue.add(request)
        }
    }
}