package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.EmployeeListData
import com.example.rejestrator.view.model.entities.LoginData
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
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("users","Error while getting users")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfUsers: ArrayList<EmployeeListData> = ArrayList()
                employeeList2.clear()
                for(userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(EmployeeListData::class.java)
                    listOfUsers.add(user!!)
                    employeeList2.add(user!!.toString())
                }
                employeeList1 = listOfUsers
            }
        })
    }
}
