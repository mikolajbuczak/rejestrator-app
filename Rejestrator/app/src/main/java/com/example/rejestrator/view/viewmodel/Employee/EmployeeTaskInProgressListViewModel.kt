package com.example.rejestrator.view.viewmodel.Employee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch

class EmployeeTaskInProgressListViewModel(application: Application) : AndroidViewModel(application)  {
    private val _allTasks: MutableLiveData<ArrayList<TaskInProgress>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<TaskInProgress>>
        get()=_allTasks

    fun getTasksInProgressForEmployee(id : String)
    {
        viewModelScope.launch {
            _allTasks.value = ApiRepository.getTasksInProgressForEmployee(id)
        }
    }

    fun endTask(id : Int)
    {
        viewModelScope.launch {
            ApiRepository.endTask(id)
        }
    }

    fun addTaskDone(id : String, task : String, startdate : String, enddate : String, time : String)
    {
        viewModelScope.launch {
            ApiRepository.addTaskDone(id, task, startdate, enddate, time)
        }
    }
}