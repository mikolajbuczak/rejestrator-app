package com.example.rejestrator.view.adapters.Employee

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskDoneListViewModel
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class EmployeeTaskDoneListAdapter(var taskList: LiveData<List<TaskDone>>, var taskViewModel: EmployeeTaskDoneListViewModel) : RecyclerView.Adapter<EmployeeTaskDoneListAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_doneTask)
    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_done_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentItem = taskList.value?.get(position)

        holder.textView1.text=taskList.value?.get(position)?.task
    }
}