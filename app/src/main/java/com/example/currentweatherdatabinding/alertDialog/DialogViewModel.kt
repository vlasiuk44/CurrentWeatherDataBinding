package com.example.currentweatherdatabinding.alertDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogViewModel: ViewModel() {
    val displayingMode: MutableLiveData<DisplayingMode> by lazy {
        MutableLiveData()
    }
}