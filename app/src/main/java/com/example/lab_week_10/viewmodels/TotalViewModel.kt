package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    init {
        _total.value = 0 // sinkron, tidak pakai postValue agar tidak flicker
    }

    fun incrementTotal() {
        _total.value = (_total.value ?: 0) + 1
    }

    // optional: setter jika butuh set langsung dari luar
    fun setTotal(newTotal: Int) {
        _total.value = newTotal
    }
}
