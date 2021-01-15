package com.example.rejestrator.view.view.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.view.Employee.DashboardTaskDoneListEmployee
import com.example.rejestrator.view.view.Employee.DashboardTaskListEmployee
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
import kotlinx.android.synthetic.main.fragment_employee_list_admin.DoneList
import kotlinx.android.synthetic.main.fragment_employee_list_admin.employeeList
import kotlinx.android.synthetic.main.fragment_employee_list_admin.logsList
import kotlinx.android.synthetic.main.fragment_raport_list_admin.*

class DashboardRaportAdmin : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_raport_list_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_to_dashboardLogsListAdmin) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_to_dashboardEmployeeListAdmin) }
        DoneList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_self) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_to_loginAdmin) }

        //spinner populate, chart setup based on spinner choice - function on spinner selected change or something
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardRaportAdmin {
            return DashboardRaportAdmin()
        }
    }
}