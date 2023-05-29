package com.helgar.wellnessappprot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helgar.wellnessappprot.Cita
import com.helgar.wellnessappprot.Doctor
import com.helgar.wellnessappprot.domain.data.network.Repo
import com.helgar.wellnessappprot.domain.data.network.Repo2

class DateViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val repo = Repo2(context)
    fun fetchDateData(): LiveData<MutableList<Cita>> {
        val mutableData = MutableLiveData<MutableList<Cita>>()
        repo.getDateData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}