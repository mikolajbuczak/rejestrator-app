package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        fillLogsLists(id)
        fillTaskLists(id)
        fillTasksInProgressLists(id)
        fillTaskDoneLists(id)
        getAllLogsForEmployeeRaportToday(id, date)
        getAllTasksDoneForEmployeeRaportToday(id, date)
    }

    fun fillLogsLists(id : String)
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
                    if(log!!.employeeID == id)
                        listOfLogs.add(log!!)
                }
                _employeeLogs.value = listOfLogs
            }
        })
    }

    fun fillTaskLists(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("tasks","Error while getting tasks")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<Task> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    listOfTasks.add(task!!)
                }
                _employeeTasks.value = listOfTasks
            }
        })
    }

    fun fillTasksInProgressLists(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_in_progress").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("tasks","Error while getting tasks")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<TaskInProgress> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskInProgress::class.java)
                    listOfTasks.add(task!!)
                }
                _employeeTasksInProgress.value = listOfTasks
            }
        })
    }

    fun fillTaskDoneLists(id : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_done").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("tasks","Error while getting tasks")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<TaskDone> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskDone::class.java)
                    listOfTasks.add(task!!)
                }
                _employeeTasksDone.value = listOfTasks
            }
        })
    }

    fun getAllLogsForEmployeeRaportToday(id : String, date : String)
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
                    if(log!!.employeeID == id && log!!.date!!.split(" ").first() == date)
                        listOfLogs.add(log!!)
                }
                _employeeLogsToday.value = listOfLogs
            }
        })
    }

    fun getAllTasksDoneForEmployeeRaportToday(id : String, date : String)
    {
        FirebaseDatabase.getInstance().getReference().child("tasks_done").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("tasks","Error while getting tasks")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfTasks: ArrayList<TaskDone> = ArrayList()
                for(taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskDone::class.java)
                    if(task!!.enddate!!.split(" ").first() == date)
                        listOfTasks.add(task!!)
                }
                _employeeTasksDoneToday.value = listOfTasks
            }
        })
    }
}