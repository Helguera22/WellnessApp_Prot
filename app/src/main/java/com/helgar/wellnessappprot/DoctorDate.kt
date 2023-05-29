package com.helgar.wellnessappprot

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.widget.EditText
import java.util.*
import kotlin.collections.ArrayList

class DoctorDate : AppCompatActivity() {

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedPaymentMethod: String = ""
    private var selectedReas: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_date)

        val btnSiguiente: Button = findViewById(R.id.nextButton)
        val spinnerHorarios: Spinner = findViewById(R.id.timeSpinner)
        val etFecha: CalendarView = findViewById(R.id.calendarView)
        val rbTarjeta: RadioButton = findViewById(R.id.cardButton)
        val Descr: TextView = findViewById(R.id.Description)
        val Reas: EditText = findViewById(R.id.etReason)

        // Obtener las SharedPreferences
        val sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        val nombreMedico = intent.getStringExtra("nombre")
        val times = resources.getStringArray(R.array.times)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        spinnerHorarios.adapter = adapter

        // Obtener el valor de la cedula y el nss desde las SharedPreferences
        val nss = sharedPreferences.getString("nss", "")
        val cedula = sharedPreferences.getString("cedula", "")

        Descr.text = "$nombreMedico"

        etFecha.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val monthCorrected = month + 1
            selectedDate = "$dayOfMonth/$monthCorrected/$year"
        }

        btnSiguiente.setOnClickListener {
            selectedReas = Reas.getText().toString()
            selectedTime = spinnerHorarios.selectedItem.toString()
            selectedPaymentMethod = if (rbTarjeta.isChecked) "Tarjeta" else "Efectivo"

            val intent = Intent(this, DateDetails::class.java)
            intent.putExtra("cedula", cedula)
            intent.putExtra("nss", nss)
            intent.putExtra("nombre", nombreMedico)
            intent.putExtra("Razon", selectedReas)
            intent.putExtra("selected_date", selectedDate)
            intent.putExtra("selected_time", selectedTime)
            intent.putExtra("selected_payment_method", selectedPaymentMethod)
            startActivity(intent)
        }
    }
}