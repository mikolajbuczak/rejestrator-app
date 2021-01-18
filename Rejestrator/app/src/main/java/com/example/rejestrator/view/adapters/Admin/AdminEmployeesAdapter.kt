package com.example.rejestrator.view.adapters.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.EmployeeListData
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeesViewModel
import kotlinx.android.synthetic.main.one_row_employees.view.*

class AdminEmployeesAdapter(var employeeList: LiveData<ArrayList<EmployeeListData>>, var taskViewModel: AdminEmployeesViewModel) : RecyclerView.Adapter<AdminEmployeesAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_employeeID)
        val textView2= view.findViewById<TextView>(R.id.row_employeeName)
        val textView3= view.findViewById<TextView>(R.id.row_employeeSurname)

    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_employees,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return employeeList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.textView1.text=employeeList.value?.get(position)?.employeeID
        holder.textView2.text=employeeList.value?.get(position)?.name
        holder.textView3.text=employeeList.value?.get(position)?.surname

        holder.itemView.setOnClickListener {x->
            State.selectedEmployeeWithId = employeeList.value?.get(position)?.toString().toString()
            State.selectedEmployee = employeeList.value?.get(position)?.makeString().toString()
            State.selectedEmployeeId = employeeList.value?.get(position)?.employeeID.toString()
            State.selectedEmployeePin = employeeList.value?.get(position)?.pin.toString()
            State.selectedEmployeeName = employeeList.value?.get(position)?.name.toString()
            State.selectedEmployeeSurname = employeeList.value?.get(position)?.surname.toString()
            State.selectedEmployeeShift= employeeList.value?.get(position)?.shift.toString()
            x.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_to_dashboardEmployeeListAdmin)
        }

        holder.view.row_chartButton.setOnClickListener {x->
            State.selectedEmployeeId = employeeList.value?.get(position)?.employeeID.toString()
            State.selectedEmployeeName = employeeList.value?.get(position)?.name.toString()
            State.selectedEmployeeSurname = employeeList.value?.get(position)?.surname.toString()
            x.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_to_dashboardRaportAdmin)
        }
    }
}