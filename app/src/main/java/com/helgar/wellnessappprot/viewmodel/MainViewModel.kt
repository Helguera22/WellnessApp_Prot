package com.helgar.wellnessappprot.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.helgar.wellnessappprot.Doctor
import com.helgar.wellnessappprot.domain.data.network.Repo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val repo = Repo(context)
    fun fetchDocData(): LiveData<MutableList<Doctor>> {
        val mutableData = MutableLiveData<MutableList<Doctor>>()
        repo.getDoctorData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}