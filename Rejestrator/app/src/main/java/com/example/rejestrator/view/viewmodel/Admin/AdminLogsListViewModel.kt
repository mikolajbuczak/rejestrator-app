package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch

class AdminLogsListViewModel(application: Application): AndroidViewModel(application)  {
    private val _allLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val allLogs: LiveData<ArrayList<LoginData>>
        get()=_allLogs

    private val _filteredAllLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val filteredAllLogs: LiveData<ArrayList<LoginData>>
        get()=_filteredAllLogs

    fun getAllLogs()
    {
        viewModelScope.launch {
            _allLogs.value = ApiRepository.getAllLogs()
            _filteredAllLogs.value = ApiRepository.getAllLogs()
        }
    }

    fun insertEmployee(id : String, pin : String, name : String, surname : String, shift : String)
    {
        viewModelScope.launch {
            ApiRepository.insertEmployee(id, pin, name, surname, shift)
        }
    }
}