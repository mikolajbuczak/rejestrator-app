package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.EmployeeListData
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEmployeesViewModel(application: Application): AndroidViewModel(application){
    private val _employeeList: MutableLiveData<ArrayList<EmployeeListData>> = MutableLiveData()
    val employeeList: LiveData<ArrayList<EmployeeListData>>
        get()=_employeeList

    private val _filteredEmployeeList: MutableLiveData<ArrayList<EmployeeListData>> = MutableLiveData()
    val filteredEmployeeList: LiveData<ArrayList<EmployeeListData>>
        get()=_filteredEmployeeList

    var employeeList1: ArrayList<EmployeeListData> = arrayListOf()

    var employeeList2: java.util.ArrayList<String> = arrayListOf()


    fun getAllEmployees()
    {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("users","Error while getting users")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfUsers: ArrayList<EmployeeListData> = ArrayList()
                for(userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(EmployeeListData::class.java)
                    listOfUsers.add(user!!)
                }
                _employeeList.value = listOfUsers
                _filteredEmployeeList.value = listOfUsers
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
