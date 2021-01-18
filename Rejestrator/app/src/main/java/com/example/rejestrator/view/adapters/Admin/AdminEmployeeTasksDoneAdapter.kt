package com.example.rejestrator.view.adapters.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import java.util.ArrayList

class AdminEmployeeTasksDoneAdapter(var taskList: LiveData<List<TaskDone>>, var taskViewModel: AdminEmployeeListViewModel) : RecyclerView.Adapter<AdminEmployeeTasksDoneAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_taskDoneEmployee)
        val textView2= view.findViewById<TextView>(R.id.row_taskDoneTime)
        val textView3= view.findViewById<TextView>(R.id.row_taskDoneStartDate)
        val textView4= view.findViewById<TextView>(R.id.row_taskDoneEndDate)

    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_admin_employee_task_done_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.textView1.text=taskList.value?.get(position)?.task
        holder.textView2.text=taskList.value?.get(position)?.time
        holder.textView3.text=taskList.value?.get(position)?.startdate
        holder.textView4.text=taskList.value?.get(position)?.enddate
    }
}