package com.example.rejestrator.view.viewmodel.Employee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeTaskListViewModel(application: Application) : AndroidViewModel(application) {
    private val _allTasks: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<Task>>
        get()=_allTasks

    fun getTasksForEmployee(id : String)
    {
        viewModelScope.launch {
            _allTasks.value = ApiRepository.getTasksForEmployee(id)
        }
    }

    fun startTask(id : Int)
    {
        viewModelScope.launch {
            ApiRepository.startTask(id)
        }
    }

    fun addTaskInProgress(id : String, task : String, date : String)
    {
        viewModelScope.launch {
            ApiRepository.addTaskInProgress(id, task, date)
        }
    }
}