package com.example.rejestrator.view.view.Employee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.adapters.Employee.EmployeeTaskListAdapter
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskListViewModel
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
import kotlinx.android.synthetic.main.fragment_task_in_progress_list_employee.*
import kotlinx.android.synthetic.main.fragment_task_list_employee.*
import kotlinx.android.synthetic.main.fragment_task_list_employee.AvailableList
import kotlinx.android.synthetic.main.fragment_task_list_employee.DoneListNav
import kotlinx.android.synthetic.main.fragment_task_list_employee.InProgressList
import kotlinx.android.synthetic.main.fragment_task_list_employee.logout

class DashboardTaskListEmployee : Fragment() {

    lateinit var taskViewModel: EmployeeTaskListViewModel
    lateinit var linearManager:LinearLayoutManager
    lateinit var adapterTask: EmployeeTaskListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        taskViewModel = ViewModelProvider(requireActivity()).get(EmployeeTaskListViewModel::class.java)
        linearManager = LinearLayoutManager(requireContext())

        taskViewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapterTask.notifyDataSetChanged()
        })

        adapterTask = EmployeeTaskListAdapter(taskViewModel.allTasks, taskViewModel)

        //taskViewModel.getTasksForEmployee(State.currentEmployeeId)

        return inflater.inflate(R.layout.fragment_task_list_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AvailableList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskListEmployee_self) }
        InProgressList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskListEmployee_to_dashboardTaskInProgressListEmployee) }
        DoneListNav.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskListEmployee_to_dashboardTaskDoneListEmployee) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskListEmployee_to_loginEmployee) }

        availableList_recycler_view.addItemDecoration(DividerItemDecoration(availableList_recycler_view.context, DividerItemDecoration.VERTICAL))
        availableList_recycler_view.apply {
            adapter = adapterTask
            layoutManager = linearManager
            //taskViewModel.getTasksForEmployee(State.currentEmployeeId)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardTaskListEmployee {
            return DashboardTaskListEmployee()
        }
    }
}