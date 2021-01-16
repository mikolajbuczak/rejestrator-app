package com.example.rejestrator.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch

class LoginEmployeeViewModel(application: Application): AndroidViewModel(application) {
    fun insertLog(id : String, date : String)
    {
        viewModelScope.launch {
            ApiRepository.insertLog(id, date)
        }
    }
}