package com.example.rejestrator.view.adapters.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import java.util.ArrayList

class AdminEmployeeLogsAdapter(var logsList: LiveData<List<LoginData>>, var taskViewModel: AdminEmployeeListViewModel) : RecyclerView.Adapter<AdminEmployeeLogsAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_dateEmployee)

    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_admin_employee_log_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return logsList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.textView1.text=logsList.value?.get(position)?.date
    }
}