package com.example.rejestrator.view.adapters.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import java.util.ArrayList

class AdminEmployeeTasksInProgressAdapter(var taskList: LiveData<List<TaskInProgress>>, var taskViewModel: AdminEmployeeListViewModel) : RecyclerView.Adapter<AdminEmployeeTasksInProgressAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_taskInProgressEmployee)
        val textView2= view.findViewById<TextView>(R.id.row_dateEmployee)

    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_admin_employee_task_in_progress_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.textView1.text=taskList.value?.get(position)?.task
        holder.textView2.text=taskList.value?.get(position)?.date
    }
}