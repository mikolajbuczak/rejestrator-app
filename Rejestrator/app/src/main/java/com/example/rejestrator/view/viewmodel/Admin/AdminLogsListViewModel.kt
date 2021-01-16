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
    private val _allLogs: MutableLiveData<List<LoginData>> = MutableLiveData()
    val allLogs: LiveData<List<LoginData>>
        get()=_allLogs

    fun getAllLogs()
    {
        viewModelScope.launch {
            _allLogs.value = ApiRepository.getAllLogs()
        }
    }
}