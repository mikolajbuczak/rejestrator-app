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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.rejestrator.view.State
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

    fun deleteTask(id: String){
        var taskKey = ""
        FirebaseDatabase.getInstance().getReference().child("tasks").child(State.selectedEmployeeId).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("users", "Error while getting users")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (taskSnapshot in snapshot.children) {
                    val oneTask = taskSnapshot.getValue(Task::class.java)
                    if (oneTask!!.id == id)
                        taskKey = taskSnapshot.getKey().toString()
                    Log.d("KEY", "key: $taskKey")
                    if (taskKey != "") {
                        FirebaseDatabase.getInstance().getReference().child("tasks").child(State.selectedEmployeeId).child(taskKey).setValue(null)
                    }
                }
            }
        })
    }

    fun deleteEmployee(id : String) {
        var userKey = ""
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("users", "Error while getting users")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(EmployeeListData::class.java)
                    if (user!!.employeeID == id)
                        userKey = userSnapshot.getKey().toString()
                    Log.d("KEY", "key: $userKey")
                    if (userKey != "")
                        FirebaseDatabase.getInstance().getReference().child("users").child(userKey)
                            .setValue(null)
                }
            }
        })
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
}