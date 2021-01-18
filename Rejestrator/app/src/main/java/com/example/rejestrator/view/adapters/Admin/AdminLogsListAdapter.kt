package com.example.rejestrator.view.adapters.Admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskListViewModel
import kotlinx.android.synthetic.main.one_row_available_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdminLogsListAdapter(var logsList: LiveData<ArrayList<LoginData>>, var logsViewModel: AdminLogsListViewModel) : RecyclerView.Adapter<AdminLogsListAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_idEmployee)
        val textView2= view.findViewById<TextView>(R.id.row_nameEmployee)
        val textView3= view.findViewById<TextView>(R.id.row_surnameEmployee)
        val textView4= view.findViewById<TextView>(R.id.row_dateEmployee)
    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_note_admin_logs_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return logsList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.textView1.text=logsList.value?.get(position)?.employeeID
        holder.textView2.text=logsList.value?.get(position)?.name
        holder.textView3.text=logsList.value?.get(position)?.surname
        holder.textView4.text=logsList.value?.get(position)?.date
    }
}