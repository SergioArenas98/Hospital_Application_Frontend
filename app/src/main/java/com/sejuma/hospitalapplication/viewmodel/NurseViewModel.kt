package com.sejuma.hospitalapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sejuma.hospitalapplication.R
import com.sejuma.hospitalapplication.model.Nurse

class NurseViewModel : ViewModel() {

    // MutableLiveData que almacena la lista de enfermeros.
    private val _nurses = MutableLiveData<List<Nurse>>().apply {
        value = listOf(
            Nurse("Sergio", "sergio.nurse", "sergio123", R.drawable.sergio),
            Nurse("David", "david.nurse", "david123", R.drawable.david),
            Nurse("Jose", "jose.nurse", "jose123", R.drawable.jose),
            Nurse("Fiorella", "fiorella.nurse", "fiorella123", R.drawable.fio)
        )
    }
    // LiveData expuesto para que la UI pueda observar la lista de enfermeros.
    val nurses: LiveData<List<Nurse>> get() = _nurses
}
