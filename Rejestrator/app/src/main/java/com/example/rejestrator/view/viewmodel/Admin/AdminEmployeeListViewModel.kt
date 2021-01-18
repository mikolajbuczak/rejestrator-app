package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.*
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEmployeeListViewModel(application: Application): AndroidViewModel(application)   {
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

    var employeeList: ArrayList<EmployeeListData> = arrayListOf()

    var employeeList2: java.util.ArrayList<String> = arrayListOf()

    fun getAllEmployeesForTaskAdding()
    {
        ApiRepository.getAllEmployees().enqueue(object : Callback<ArrayList<EmployeeListData>> {
            override fun onFailure(call: Call<ArrayList<EmployeeListData>>, t: Throwable) {
                Log.d("lol", "failure")
            }

            override fun onResponse(call: Call<ArrayList<EmployeeListData>>, response: Response<ArrayList<EmployeeListData>>) {
                employeeList = response.body()!!
                employeeList2.clear()
                employeeList.forEach(){
                    Log.d("lol", it.toString())
                    employeeList2.add(it.toString())
                }
            }

        })

    }

    fun deleteTask(id : Int){
        viewModelScope.launch {
            ApiRepository.deleteTask(id)
        }
    }

    fun deleteEmployee(id : String){
        viewModelScope.launch {
            ApiRepository.deleteEmployee(id)
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