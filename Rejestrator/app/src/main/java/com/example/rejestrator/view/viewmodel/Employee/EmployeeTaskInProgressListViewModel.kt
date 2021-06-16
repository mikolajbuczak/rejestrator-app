package com.example.rejestrator.view.viewmodel.Employee

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class EmployeeTaskInProgressListViewModel(application: Application) : AndroidViewModel(application)  {
    private val _allTasks: MutableLiveData<ArrayList<TaskInProgress>> = MutableLiveData()
    val allTasks: LiveData<ArrayList<TaskInProgress>>
        get()=_allTasks

    fun getTasksInProgressForEmployee(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_in_progress").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("TasksInProgress","Error while getting tasksInProgress")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<TaskInProgress> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskInProgress::class.java)
                    listOfTasks.add(task!!)
                }
                _allTasks.value = listOfTasks
            }
        })
    }

    fun endTask(id : String?)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_in_progress").child(State.currentEmployeeId).child(id!!).removeValue()
    }

    fun addTaskDone(task : String, startdate : String, enddate : String, time : String)
    {
        val id = UUID.randomUUID().toString()
        val taskDone = TaskDone(id, State.currentEmployeeId, task, startdate, enddate, time)
        FirebaseDatabase.getInstance().getReference().child("tasks_done").child(State.currentEmployeeId).child(id).setValue(taskDone)
    }
}