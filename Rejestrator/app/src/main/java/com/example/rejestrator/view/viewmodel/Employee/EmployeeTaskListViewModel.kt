package com.example.rejestrator.view.viewmodel.Employee

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class EmployeeTaskListViewModel(application: Application) : AndroidViewModel(application) {
    private val _allTasks: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<Task>>
        get()=_allTasks

    fun getTasksForEmployee(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("Tasks","Error while getting tasks")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<Task> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    listOfTasks.add(task!!)
                }
                _allTasks.value = listOfTasks
            }
        })
    }

    fun startTask(id : String?)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks").child(State.currentEmployeeId).child(id!!).removeValue()
    }

    fun addTaskInProgress(task : String, date : String)
    {
        val id = UUID.randomUUID().toString()
        val taskInProgress = TaskInProgress(id, State.currentEmployeeId, task, date)
        FirebaseDatabase.getInstance().getReference().child("tasks_in_progress").child(State.currentEmployeeId).child(id).setValue(taskInProgress)
    }
}