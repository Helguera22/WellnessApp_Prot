package com.helgar.wellnessappprot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.helgar.wellnessappprot.dataclasses.Padecimiento
import com.helgar.wellnessappprot.dataclasses.Alergia
import com.helgar.wellnessappprot.dataclasses.Discapacidad
import com.helgar.wellnessappprot.ui.MainUser1
import org.json.JSONArray
import java.util.HashMap

class WellCard : AppCompatActivity() {
    /*lateinit var adapter1: ArrayAdapter<String>
    lateinit var spinnerEC: Spinner*/
    val selectedAlergiaIds = ArrayList<Int>()
    val selectedPadecimientoIds = ArrayList<Int>()
    val selectedDiscapacidadIds = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_well_card)

        /*adapter1 = ArrayAdapter(this@WellCard, android.R.layout.simple_spinner_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEC = findViewById(R.id.spinner4)

        spinnerEC.adapter = adapter1

        adapter1.add("hola")
        adapter1.add("mundo")

        adapter1.notifyDataSetChanged()*/


        val nss = findViewById<TextView>(R.id.textViewNSS)
        val pacNom = findViewById<TextView>(R.id.patientName)
        val pacNss = findViewById<TextView>(R.id.textViewNSS)
        // val GrupoSanguineo = findViewById<Spinner>(R.id.spinner)
        val edt_altura = findViewById<EditText>(R.id.editTextAltura)
        val edt_peso = findViewById<EditText>(R.id.editTextPeso)
        /* val alergia = findViewById<Spinner>(R.id.spinner2)
        val alergia2 = findViewById<Spinner>(R.id.spinner3)
        val EnfermedadC = findViewById<Spinner>(R.id.spinner4)
        val Disc = findViewById<Spinner>(R.id.spinner5)
        val Disc2 = findViewById<Spinner>(R.id.spinner6) */
        val btn_guardar = findViewById<Button>(R.id.button3)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nombrePac = sharedPreferences.getString("pacNom", "nombrePac")
        pacNom.text = "$nombrePac"

        val spinner_GS: Spinner = findViewById(R.id.spinner)
        val adapterGS: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.spinner_GS_array, android.R.layout.simple_spinner_item
        )
        adapterGS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_GS.adapter = adapterGS

        val spinner_Al: Spinner = findViewById(R.id.spinner2)
        val chipGroupAl = findViewById<ChipGroup>(R.id.chipGroup)
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val url = "http://ictuscorps.atwebpages.com/anime-main/php/get_alergia.php"

        val selectedAlergies = ArrayList<String>() // Para rastrear las alergias seleccionadas
        var manualSelectionDone = false // Bandera para indicar selección manual

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val data = ArrayList<Alergia>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("ID")
                    val name = jsonObject.getString("Alergia")
                    data.add(Alergia(id, name))
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data.map { it.nombre })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_Al.adapter = adapter

                spinner_Al.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedAlergia = data[position]

                        if (!selectedAlergiaIds.contains(selectedAlergia.id)) {
                            selectedAlergiaIds.add(selectedAlergia.id)

                            val chip = LayoutInflater.from(this@WellCard).inflate(
                                R.layout.chip_layout,
                                chipGroupAl,
                                false
                            ) as Chip

                            chip.text = "${selectedAlergia.nombre}"
                            chip.isCloseIconVisible = true

                            chip.setOnCloseIconClickListener {
                                selectedAlergiaIds.remove(selectedAlergia.id)
                                chipGroupAl.removeView(chip)
                            }

                            chipGroupAl.addView(chip)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            },
            { error ->
                // Manejar el error de Volley aquí
            }
        )

        requestQueue.add(jsonArrayRequest)

        // Spinner que muestra los tipos de padecimientos:
        val spinner_EC: Spinner = findViewById(R.id.spinner4)
        val chipGroupEC = findViewById<ChipGroup>(R.id.chipGroup2)
        val requestQueue1: RequestQueue = Volley.newRequestQueue(this)
        val url1 = "http://ictuscorps.atwebpages.com/anime-main/php/get_padecim.php"

        val selectedPadec = ArrayList<String>() // Para rastrear las alergias seleccionadas
        var manualSelectionDone1 = false // Bandera para indicar selección manual

        val jsonArrayRequest1 = JsonArrayRequest(
            Request.Method.GET, url1, null,
            { response ->
                val data1 = ArrayList<Padecimiento>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("ID")
                    val name = jsonObject.getString("Padecimiento")
                    data1.add(Padecimiento(id, name))
                }


                val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data1.map { it.nombre })
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_EC.adapter = adapter1

                spinner_EC.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedPadecim = data1[position]

                        if (!selectedPadecimientoIds.contains(selectedPadecim.id)) {
                            selectedPadecimientoIds.add(selectedPadecim.id)

                            val chip1 = LayoutInflater.from(this@WellCard).inflate(
                                R.layout.chip_layout,
                                chipGroupEC,
                                false
                            ) as Chip

                            chip1.text = "${selectedPadecim.nombre}"
                            chip1.isCloseIconVisible = true

                            chip1.setOnCloseIconClickListener {
                                selectedPadecimientoIds.remove(selectedPadecim.id)
                                chipGroupEC.removeView(chip1)
                            }

                            chipGroupEC.addView(chip1)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            },
            { error ->
                // Manejar el error de Volley aquí
            }
        )

        requestQueue1.add(jsonArrayRequest1)

        // Spinner que muestra los tipos de discapacidades:
        val spinner_Dis: Spinner = findViewById(R.id.spinner5)
        val chipGroupDis = findViewById<ChipGroup>(R.id.chipGroup3)
        val requestQueue2: RequestQueue = Volley.newRequestQueue(this)
        val url2 = "http://ictuscorps.atwebpages.com/anime-main/php/get_discap.php"

        val selectedDis = ArrayList<String>() // Para rastrear las alergias seleccionadas
        var manualSelectionDone2 = false // Bandera para indicar selección manual

        val jsonArrayRequest2 = JsonArrayRequest(
            Request.Method.GET, url2, null,
            { response ->
                val data2 = ArrayList<Discapacidad>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("ID")
                    val name = jsonObject.getString("Discapacidad")
                    data2.add(Discapacidad(id, name))
                }

                val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2.map {it.nombre})
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_Dis.adapter = adapter2

                spinner_Dis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedOption2 = data2[position]

                        if (!selectedDiscapacidadIds.contains(selectedOption2.id)) {
                            selectedDiscapacidadIds.add(selectedOption2.id)

                            val chip2 = LayoutInflater.from(this@WellCard).inflate(
                                R.layout.chip_layout,
                                chipGroupDis,
                                false
                            ) as Chip

                            chip2.text = "${selectedOption2.nombre}"
                            chip2.isCloseIconVisible = true

                            chip2.setOnCloseIconClickListener {
                                selectedDiscapacidadIds.remove(selectedOption2.id)
                                chipGroupDis.removeView(chip2)
                            }

                            chipGroupDis.addView(chip2)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            },
            { error ->
                // Manejar el error de Volley aquí
            }
        )

        requestQueue2.add(jsonArrayRequest2)

        btn_guardar.setOnClickListener {

            // Obtener el NSS del TextView y los valores de los editText
            val nssPaciente = nss.text.toString()
            val grupo_san = spinner_GS.selectedItem.toString()
            val altura = edt_altura.text.toString().trim()
            val peso = edt_peso.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (nssPaciente.isEmpty() || grupo_san.isEmpty() || altura.isEmpty() || peso.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar los datos al servidor utilizando Volley
                val queue = Volley.newRequestQueue(this)
                val urlWell = "http://ictuscorps.atwebpages.com/anime-main/php/saveWellcard.php"

                val request = object : StringRequest(Request.Method.POST, urlWell,
                    Response.Listener { response ->
                        Log.d("WellCard", "Response: $response")
                    },
                    Response.ErrorListener { error ->
                        Log.e("WellCard", "Error: ${error.message}")
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["nss"] = nssPaciente
                        params["grupo_sanguineo"] = grupo_san
                        params["altura"] = altura
                        params["peso"] = peso
                        return params
                    }
                }

                queue.add(request)
            }

            // Crear una lista de cadenas para combinar NSS y cada ID de alergia
            val datosCombinados = selectedAlergiaIds.map { id -> "$nssPaciente - $id" }

            // Convertir la lista a una cadena JSON
            val datosJsonArray = JSONArray(datosCombinados)

            // Crear una solicitud POST utilizando Volley
            val urlServidor1 = "http://ictuscorps.atwebpages.com/anime-main/php/saveAlergia.php"
            val request1 = object : StringRequest(
                Method.POST, urlServidor1,
                Response.Listener { response ->
                    // Mostrar el resultado de la respuesta del servidor
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    // Manejar el error de Volley aquí
                    Toast.makeText(this, "Error al enviar los datos al servidor", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getBody(): ByteArray {
                    return datosJsonArray.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            // Agregar la solicitud a la cola de Volley
            requestQueue.add(request1)

            // Crear una lista de cadenas para combinar NSS y cada ID de alergia
            val datosCombinados2 = selectedPadecimientoIds.map { id -> "$nssPaciente - $id" }

            // Convertir la lista a una cadena JSON
            val datosJsonArray2 = JSONArray(datosCombinados2)

            // Crear una solicitud POST utilizando Volley
            val urlServidor2 = "http://ictuscorps.atwebpages.com/anime-main/php/savePadecimiento.php"
            val request2 = object : StringRequest(
                Method.POST, urlServidor2,
                Response.Listener { response ->
                    // Mostrar el resultado de la respuesta del servidor
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    // Manejar el error de Volley aquí
                    Toast.makeText(this, "Error al enviar los datos al servidor", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getBody(): ByteArray {
                    return datosJsonArray2.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            // Agregar la solicitud a la cola de Volley
            requestQueue.add(request2)

            // Crear una lista de cadenas para combinar NSS y cada ID de alergia
            val datosCombinados3 = selectedDiscapacidadIds.map { id -> "$nssPaciente - $id" }

            // Convertir la lista a una cadena JSON
            val datosJsonArray3 = JSONArray(datosCombinados3)

            // Crear una solicitud POST utilizando Volley
            val urlServidor3 = "http://ictuscorps.atwebpages.com/anime-main/php/saveDiscapacidad.php"
            val request3 = object : StringRequest(
                Method.POST, urlServidor3,
                Response.Listener { response ->
                    // Mostrar el resultado de la respuesta del servidor
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    // Manejar el error de Volley aquí
                    Toast.makeText(this, "Error al enviar los datos al servidor", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getBody(): ByteArray {
                    return datosJsonArray3.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            // Agregar la solicitud a la cola de Volley
            requestQueue.add(request3)
        }


        obtenerNSS("$nombrePac")
    }

    private fun obtenerNSS(nombrePac: String?) {
        val url = "http://ictuscorps.atwebpages.com/anime-main/php/getNss.php?nombre_completo=$nombrePac"

        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                val nssTextView = findViewById<TextView>(R.id.textViewNSS)
                nssTextView.text = response
            },
            Response.ErrorListener { error ->
                val nssTextView = findViewById<TextView>(R.id.textViewNSS)
                nssTextView.text = "Error al obtener el NSS"
            })

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}