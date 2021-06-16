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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EmployeeTaskDoneListViewModel(application: Application) : AndroidViewModel(application)  {
    private val _allTasks: MutableLiveData<ArrayList<TaskDone>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<TaskDone>>
        get()=_allTasks

    fun getTasksDoneForEmployee(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_done").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("TasksDone","Error while getting tasksDone")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<TaskDone> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskDone::class.java)
                    listOfTasks.add(task!!)
                }
                _allTasks.value = listOfTasks
            }
        })
    }
}