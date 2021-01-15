package com.example.rejestrator.view.view.Employee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.rejestrator.R
import kotlinx.android.synthetic.main.fragment_task_list_employee.*

class DashboardTaskInProgressListEmployee : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_in_progress_list_employee, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AvailableList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_dashboardTaskListEmployee) }
        InProgressList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_self) }
        DoneListNav.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_dashboardTaskDoneListEmployee) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_loginEmployee) }

        //populate recycler view, view model etc.

    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardTaskInProgressListEmployee {
            return DashboardTaskInProgressListEmployee()
        }
    }
}