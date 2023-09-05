package com.helgar.wellnessappprot

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.helgar.wellnessappprot.databinding.ActivityConsultaBinding
import com.helgar.wellnessappprot.dataclasses.Enfermedad
import com.helgar.wellnessappprot.dataclasses.Sintoma
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Consulta : AppCompatActivity() {

    private lateinit var binding: ActivityConsultaBinding
    private lateinit var selectedSintomaIds: ArrayList<Int>
    private lateinit var selectedEnfermedadIds: ArrayList<Int>
    private lateinit var sintomas: List<Sintoma>
    private lateinit var enfermedades: List<Enfermedad>

    // Variable para almacenar los síntomas y sus valores
    val sintomasConValores = ArrayList<Pair<String, Int>>()

    // Después de seleccionar y asignar valores a los síntomas
    /*fun createSymptomsArray() {
        sintomasConValores.clear()

        for (sintoma in sintomas) {
            val valor = if (selectedSintomaIds.contains(sintoma.id)) 1 else 0
            sintomasConValores.add(Pair(sintoma.nombre, valor))
        }

        // Muestra los datos en un Toast (para verificar)
        val jsonString = sintomasConValores.joinToString("\n") { "${it.first}: ${it.second}" }
        Toast.makeText(this, jsonString, Toast.LENGTH_LONG).show()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nss = binding.txtNSS
        val btn_diag = binding.buttonDiag
        val temperatura = binding.editTextTemperatura
        val presion = binding.editTextPresion
        val oxi = binding.editTextOxigenacion

        val AutoCom_S = binding.autoCompleteTextViewSint
        val chipGroupS = binding.chipGroupSint
        selectedSintomaIds = ArrayList()
        selectedEnfermedadIds = ArrayList()

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val url = "http://ictuscorps.atwebpages.com/anime-main/php/get_Sintoma.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val data = ArrayList<Sintoma>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("Sintoma")
                    data.add(Sintoma(id, name))
                }

                sintomas = data

                val nombresSintomasArray: Array<String> = data.map { it.nombre }.toTypedArray()

                /*binding.autoCompleteTextViewSint.setOnItemClickListener { parent, _, position, _ ->
                    val selectedSintoma = sintomas[position]
                    if (selectedSintomaIds.contains(selectedSintoma.id)) {
                        Toast.makeText(this@Consulta, "Ya seleccionó este síntoma", Toast.LENGTH_SHORT).show()
                    } else {
                        addSintomaChip(selectedSintoma)
                    }
                }*/

                binding.autoCompleteTextViewSint.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        if (s != null) {
                            val filteredSintomas = sintomas.filter { it.nombre.contains(s.toString(), ignoreCase = true) }
                            val nombresSintomasArray: Array<String> = filteredSintomas.map { it.nombre }.toTypedArray()

                            val adapter = ArrayAdapter(this@Consulta, R.layout.item_drop_down, nombresSintomasArray)
                            binding.autoCompleteTextViewSint.setAdapter(adapter)
                            binding.autoCompleteTextViewSint.threshold = 1

                            binding.autoCompleteTextViewSint.setOnItemClickListener { parent, _, position, _ ->
                                val selectedSintoma = filteredSintomas[0]
                                if (selectedSintomaIds.contains(selectedSintoma.id)) {
                                    Toast.makeText(this@Consulta, "Ya seleccionó este síntoma", Toast.LENGTH_SHORT).show()
                                } else {
                                    addSintomaChip(selectedSintoma)
                                }
                            }
                        }
                    }
                })
            },
            { error ->
                // Manejar el error de Volley aquí
            }
        )

        requestQueue.add(jsonArrayRequest)

        val requestQueue2: RequestQueue = Volley.newRequestQueue(this)
        val urlEnfermedades = "http://ictuscorps.atwebpages.com/anime-main/php/get_Enfermedad.php"
        val jsonArrayRequestEnfermedades = JsonArrayRequest(
            Request.Method.GET, urlEnfermedades, null,
            { response ->
                val data2 = ArrayList<Enfermedad>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("Enfermedad")
                    data2.add(Enfermedad(id, name))
                }

                enfermedades = data2

                binding.autoCompleteTextViewEnf.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        if (s != null) {
                            val filteredEnfermedades = data2.filter { it.nombre.contains(s.toString(), ignoreCase = true) }
                            val nombresEnfermedadesArray: Array<String> = filteredEnfermedades.map { it.nombre}.toTypedArray()

                            val adapter = ArrayAdapter(this@Consulta, R.layout.item_drop_down, nombresEnfermedadesArray)
                            binding.autoCompleteTextViewEnf.setAdapter(adapter)
                            binding.autoCompleteTextViewEnf.threshold = 1

                            binding.autoCompleteTextViewEnf.setOnItemClickListener { parent, _, position, _ ->
                                val selectedEnfermedad = filteredEnfermedades[0]
                                if (selectedEnfermedadIds.contains(selectedEnfermedad.id)) {
                                    Toast.makeText(this@Consulta, "Ya seleccionó esta enfermedad", Toast.LENGTH_SHORT).show()
                                } else {
                                    addEnfermedadChip(selectedEnfermedad)
                                }
                            }
                        }
                    }
                })

            },
            { error ->
                // Manejar el error de Volley aquí
            }
        )

        requestQueue2.add(jsonArrayRequestEnfermedades)

        btn_diag.setOnClickListener {
            // Obtener el NSS del paciente
            /*val nss = binding.txtNSS.text.toString()
            val fecha = binding.editTextFecha.text.toString()
            val temp = temperatura.text.toString().trim()
            val pres = presion.text.toString().trim()
            val oxig = oxi.text.toString().trim()

            val queryString = sintomasConValores.joinToString("&") { "${it.first}=${it.second}" }*/

            val urlpred = "http://192.168.0.111:8080/clasificador"
            val requestQueuepred = Volley.newRequestQueue(this)

            val jsonObject = JSONObject()
            for (param in sintomasConValores) {
                jsonObject.put(param.first, param.second)
            }

            val stringRequest = object : JsonObjectRequest(
                Method.POST, urlpred, jsonObject,
                { response ->
                    // Manejar la respuesta del servidor aquí
                    try {
                        // Intenta analizar la respuesta como un objeto JSON
                        val jsonResponse = JSONObject(response.toString())
                        val resultado = jsonResponse.getString("resultado")

                        // Crea un Intent para abrir la siguiente actividad
                        val intent = Intent(this, LoadingActivity::class.java)
                        intent.putExtra("respuestaAPI", resultado) // Pasa la respuesta como extra

                        // Inicia la siguiente actividad
                        startActivity(intent)
                    } catch (e: JSONException) {
                        // Si hay un error al analizar la respuesta JSON
                        Toast.makeText(this, "Error al analizar la respuesta JSON", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Captura cualquier otra excepción no esperada
                        Toast.makeText(this, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    // Manejar errores de la solicitud aquí
                    Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }

            requestQueuepred.add(stringRequest)

            // createSymptomsArray()

            /*// Validar que los campos no estén vacíos
            if (nss.isEmpty() || fecha.isEmpty() || temp.isEmpty() || pres.isEmpty() || oxig.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar los datos al servidor utilizando Volley
                val queue = Volley.newRequestQueue(this)
                val url1 = "http://ictuscorps.atwebpages.com/anime-main/php/saveConsulta.php"

                val request = object : StringRequest(Request.Method.POST, url1,
                    Response.Listener { response ->
                        Log.d("Consulta", "Response: $response")
                    },
                    Response.ErrorListener { error ->
                        Log.e("Consulta", "Error: ${error.message}")
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["PacienteID"] = nss
                        params["Fecha"] = fecha
                        params["temperatura"] = temp
                        params["Presion"] = pres
                        params["Oxigenacion"] = oxig
                        return params
                    }
                }

                queue.add(request)
            }*/

            // Crear una lista de cadenas para combinar NSS y cada ID de síntoma con su nombre
            /*val datosCombinados = selectedSintomaIds.map { id ->
                val nombreSintoma = sintomas.find { it.id == id }?.nombre ?: ""
                "$nss - $id - $nombreSintoma - ${1}"
            }

            // Convertir la lista a una cadena JSON
            val datosJsonArray = JSONArray(datosCombinados)

            // Crear una solicitud POST utilizando Volley
            val urlServidor1 = "http://ictuscorps.atwebpages.com/anime-main/php/saveSintoma1.php"
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
            requestQueue.add(request1)*/

            /*// Crear una lista de cadenas para combinar NSS y cada ID de enfermedad con su nombre
            val datosCombinados2 = selectedEnfermedadIds.map { id ->
                val nombreEnfermedad = enfermedades.find { it.id == id }?.nombre ?: ""
                "$nss - $id - $nombreEnfermedad - ${1}"
            }

            // Convertir la lista a una cadena JSON
            val datosJsonArray2 = JSONArray(datosCombinados2)

            // Crear una solicitud POST utilizando Volley
            val urlServidor2 = "http://ictuscorps.atwebpages.com/anime-main/php/saveEnfermedad.php"
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
            requestQueue.add(request2)*/
        }
    }

    private fun addSintomaChip(sintoma: Sintoma) {
        selectedSintomaIds.add(sintoma.id)
        binding.chipGroupSint.addView(getChip(sintoma.nombre, sintoma.id))
        updateSintomasConValores()
    }

    private fun getChip(name: String, id: Int): Chip {
        val chip = Chip(this@Consulta).apply {
            text = name
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                val removedChip = it as Chip
                val removedSintoma = sintomas.find { it.nombre == removedChip.text }
                if (removedSintoma != null) {
                    selectedSintomaIds.remove(removedSintoma.id)
                }
                (it.parent as ChipGroup).removeView(it)
                updateSintomasConValores()
            }
        }

        return chip
    }

    private fun addEnfermedadChip(enfermedad: Enfermedad) {
        selectedEnfermedadIds.add(enfermedad.id)
        binding.chipGroupEnf.addView(getChipEnf(enfermedad.nombre, enfermedad.id))
    }

    private fun getChipEnf(name: String, id: Int): Chip {
        val chip = Chip(this@Consulta).apply {
            text = name
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                val removedChip = it as Chip
                val removedEnfermedad = enfermedades.find { it.nombre == removedChip.text }
                if (removedEnfermedad != null) {
                    selectedEnfermedadIds.remove(removedEnfermedad.id)
                }
                (it.parent as ChipGroup).removeView(it)
            }
        }

        return chip
    }

    private fun updateSintomasConValores() {
        sintomasConValores.clear()

        for (sintoma in sintomas) {
            val valor = if (selectedSintomaIds.contains(sintoma.id)) 1 else 0
            sintomasConValores.add(Pair(sintoma.nombre, valor))
        }
    }

}