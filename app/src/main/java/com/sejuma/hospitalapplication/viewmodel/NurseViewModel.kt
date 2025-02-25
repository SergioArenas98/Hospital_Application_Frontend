package com.sejuma.hospitalapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sejuma.hospitalapplication.R
import com.sejuma.hospitalapplication.model.Nurse

class NurseViewModel : ViewModel() {

    /*// MutableLiveData que almacena la lista de enfermeros.
    private val _nurses = MutableLiveData<List<Nurse>>().apply {
        value = listOf(
            Nurse("Sergio", "sergio.nurse", "sergio123", R.drawable.sergio),
            Nurse("David", "david.nurse", "david123", R.drawable.david),
            Nurse("Jose", "jose.nurse", "jose123", R.drawable.jose),
            Nurse("Fiorella", "fiorella.nurse", "fiorella123", R.drawable.fio)
        )
    }*/

    private val _nurses = MutableLiveData<List<Nurse>>().apply {
        value = listOf(
            Nurse(1, "Sergio", "sergio.nurse", "sergio123", ""),
            Nurse(2, "David", "david.nurse", "david123", ""),
            Nurse(3, "Jose", "jose.nurse", "jose123", ""),
            Nurse(4, "Fiorella", "fiorella.nurse", "fiorella123", "")
        )
    }


    // LiveData expuesto para que la UI pueda observar la lista de enfermeros.
    val nurses: LiveData<List<Nurse>> get() = _nurses
    fun addNurse(nurse: Nurse) {
        val updatedList = _nurses.value?.toMutableList() ?: mutableListOf()
        updatedList.add(nurse)
        _nurses.value = updatedList // Notifica a los observadores
        println("Added nurse: ${nurse.name}, total nurses: ${_nurses.value?.size}")
    }
}
