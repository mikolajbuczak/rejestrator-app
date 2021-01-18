package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch

class AdminRaportViewModel(application: Application): AndroidViewModel(application)  {
    private val _employeeLogs: MutableLiveData<List<LoginData>> = MutableLiveData()
    val employeeLogs: LiveData<List<LoginData>>
        get()=_employeeLogs

    private var _employeeTasks: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    val employeeTasks: LiveData<ArrayList<Task>>
        get()=_employeeTasks

    private val _employeeTasksInProgress: MutableLiveData<List<TaskInProgress>> = MutableLiveData()
    val employeeTasksInProgress: LiveData<List<TaskInProgress>>
        get()=_employeeTasksInProgress

    private val _employeeTasksDone: MutableLiveData<List<TaskDone>> = MutableLiveData()
    val employeeTasksDone: LiveData<List<TaskDone>>
        get()=_employeeTasksDone

    private val _employeeLogsToday: MutableLiveData<List<LoginData>> = MutableLiveData()
    val employeeLogsToday: LiveData<List<LoginData>>
        get()=_employeeLogsToday

    private val _employeeTasksDoneToday: MutableLiveData<List<TaskDone>> = MutableLiveData()
    val employeeTasksDoneToday: LiveData<List<TaskDone>>
        get()=_employeeTasksDoneToday

    fun fillLists(id : String, date : String)
    {
        viewModelScope.launch {
            _employeeLogs.value = ApiRepository.getAllLogsForEmployee(id)
            _employeeTasks.value = ApiRepository.getAllTasksForEmployee(id)
            _employeeTasksInProgress.value = ApiRepository.getAllTasksInProgressForEmployee(id)
            _employeeTasksDone.value = ApiRepository.getAllTasksDoneForEmployee(id)
            _employeeLogsToday.value = ApiRepository.getAllLogsForEmployeeRaportToday(id, date)
            _employeeTasksDoneToday.value = ApiRepository.getAllTasksDoneForEmployeeRaportToday(id, date)
        }
    }

    fun fillLogsLists(id : String)
    {
        viewModelScope.launch {
            _employeeLogs.value = ApiRepository.getAllLogsForEmployee(id)
        }
    }

    fun fillTaskLists(id : String)
    {
        viewModelScope.launch {
            _employeeTasks.value = ApiRepository.getAllTasksForEmployee(id)
        }
    }

    fun fillTasksInProgressLists(id : String)
    {
        viewModelScope.launch {
            _employeeTasksInProgress.value = ApiRepository.getAllTasksInProgressForEmployee(id)
        }
    }

    fun fillTaskDoneLists(id : String)
    {
        viewModelScope.launch {
            _employeeTasksDone.value = ApiRepository.getAllTasksDoneForEmployee(id)
        }
    }
}