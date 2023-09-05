package com.helgar.wellnessappprot.domain.data.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.helgar.wellnessappprot.dataclasses.Doctor
import org.json.JSONArray
import org.json.JSONException

class Repo(private val context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun getDoctorData(): LiveData<MutableList<Doctor>> {
        val mutableData = MutableLiveData<MutableList<Doctor>>()

        val url = "http://ictuscorps.atwebpages.com/anime-main/php/obtener_doctores.php"

        // Crear una nueva solicitud Volley
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response: JSONArray ->
                val listData = mutableListOf<Doctor>()
                try {
                     // Iterar sobre el array de resultados y crear objetos Doctor con los datos obtenidos
                    for (i in 0 until response.length()) {
                        val doctorObject = response.getJSONObject(i)
                        val imageUrl = doctorObject.getString("imagen")
                        val nombre = doctorObject.getString("nombre_completo")
                        val espec = doctorObject.getString("especialidad")
                        val doctor = Doctor(imageUrl!!, nombre!!, espec!!)
                        listData.add(doctor)

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