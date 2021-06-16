package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.AdminLoginData
import com.example.rejestrator.view.model.entities.EmployeeListData
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_login_administrator.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLogsListViewModel(application: Application): AndroidViewModel(application)  {
    private val _allLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val allLogs: LiveData<ArrayList<LoginData>>
        get()=_allLogs

    private val _filteredAllLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val filteredAllLogs: LiveData<ArrayList<LoginData>>
        get()=_filteredAllLogs

    var employeeList1: ArrayList<EmployeeListData> = arrayListOf()

    var employeeList2: java.util.ArrayList<String> = arrayListOf()

    fun getAllLogs()
    {
        FirebaseDatabase.getInstance().getReference().child("logs").addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("Logs","Error while getting logs")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfLogs: ArrayList<LoginData> = ArrayList()
                for(logSnapshot in snapshot.children) {
                    val log = logSnapshot.getValue(LoginData::class.java)
                    listOfLogs.add(log!!)
                }
                _allLogs.value = listOfLogs
                _filteredAllLogs.value = listOfLogs
            }
        })
    }

    fun getAllEmployeesForTaskAdding()
    {
        ApiRepository.getAllEmployees().enqueue(object : Callback<ArrayList<EmployeeListData>>{
            override fun onFailure(call: Call<ArrayList<EmployeeListData>>, t: Throwable) {
            }

            override fun onResponse(call: Call<ArrayList<EmployeeListData>>, response: Response<ArrayList<EmployeeListData>>) {
                employeeList1 = response.body()!!
                employeeList2.clear()
                employeeList1.forEach(){
                    employeeList2.add(it.toString())
                }

            }

        })

    }

    fun insertEmployee(id : String, pin : String, name : String, surname : String, shift : String)
    {
        viewModelScope.launch {
            ApiRepository.insertEmployee(id, pin, name, surname, shift)
        }
    }

    fun insertAdmin(id : String, username: String, password: String, name : String, surname : String)
    {
        viewModelScope.launch {
            ApiRepository.insertAdmin(id, username, password, name, surname)
        }
    }
}