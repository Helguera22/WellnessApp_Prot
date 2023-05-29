package com.helgar.wellnessappprot.domain.data.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.Cita
import com.helgar.wellnessappprot.Doctor
import org.json.JSONArray
import org.json.JSONException

class Repo2(private val context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun getDateData(): LiveData<MutableList<Cita>> {
        val mutableData = MutableLiveData<MutableList<Cita>>()

        val url = "http://192.168.15.124:8080/wellnessappDb/obtener_citas.php"

        // Crear una nueva solicitud Volley
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response: JSONArray ->
                val listData = mutableListOf<Cita>()
                try {
                    // Iterar sobre el array de resultados y crear objetos Doctor con los datos obtenidos
                    for (i in 0 until response.length()) {
                        val citaObject = response.getJSONObject(i)
                        val nombre = citaObject.getString("nombre_paciente")
                        val nota = citaObject.getString("nota")
                        val hora = citaObject.getString("hora")
                        val cita = Cita(nombre!!, nota!!, hora!!)
                        listData.add(cita)
                    }

                    // Actualizar el valor de la LiveData con los datos obtenidos
                    mutableData.value = listData
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })
        // Agregar la solicitud a la cola de Volley
        requestQueue.add(jsonArrayRequest)
        return mutableData
    }
}