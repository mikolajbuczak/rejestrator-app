package com.example.rejestrator.view.adapters.Employee

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskListViewModel
import kotlinx.android.synthetic.main.one_row_available_list.view.*
import java.lang.Boolean
import java.text.SimpleDateFormat
import java.util.*

class EmployeeTaskListAdapter(var taskList: LiveData<ArrayList<Task>>, var taskViewModel: EmployeeTaskListViewModel) : RecyclerView.Adapter<EmployeeTaskListAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_availableTask)
    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_available_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentItem = taskList.value?.get(position)

        holder.textView1.text=taskList.value?.get(position)?.task

        holder.view.row_startTaskButton.setOnClickListener {
            if (currentItem != null) {
                removeItemAt(position)
                taskViewModel.startTask(currentItem.id)

                val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val currentDate = sdf.format(Date())

                taskViewModel.addTaskInProgress(currentItem.task!!, currentDate)
            }
        }
    }

    private fun removeItemAt(position: Int) {
        if (taskList.value?.size!! > 0) {
            taskList.value?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }
}
