package com.example.rejestrator.view.viewmodel.Employee

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EmployeeTaskDoneListViewModel(application: Application) : AndroidViewModel(application)  {
    private val _allTasks: MutableLiveData<ArrayList<TaskDone>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<TaskDone>>
        get()=_allTasks

    fun getTasksDoneForEmployee(id : String)
    {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        var currentDate = sdf.format(Date())

        viewModelScope.launch {
            _allTasks.value = ApiRepository.getTasksDoneForEmployee(id, currentDate)
        }
    }
}