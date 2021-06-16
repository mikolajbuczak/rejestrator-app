package com.example.rejestrator.view.adapters.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import kotlinx.android.synthetic.main.fragment_admin_employee_list.*
import kotlinx.android.synthetic.main.fragment_employee_list_admin.view.*
import kotlinx.android.synthetic.main.one_row_admin_employee_task_list.view.*
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AdminEmployeeTasksAdapter(var taskList: LiveData<ArrayList<Task>>, var taskViewModel: AdminEmployeeListViewModel) : RecyclerView.Adapter<AdminEmployeeTasksAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_taskEmployee)

    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_admin_employee_task_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentItem = taskList.value?.get(position)

        holder.textView1.text=taskList.value?.get(position)?.task

        holder.view.row_deleteTaskButton.setOnClickListener{ x->
            if (currentItem != null) {
                taskViewModel.deleteTask(currentItem.id.toString())
                taskList.value?.remove(currentItem)
                notifyItemRemoved(position)
            }
        }
    }
}