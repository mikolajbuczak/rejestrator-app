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
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.adapters.Employee.EmployeeTaskInProgressListAdapter
import com.example.rejestrator.view.adapters.Employee.EmployeeTaskListAdapter
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskListViewModel
import kotlinx.android.synthetic.main.fragment_task_done_list_employee.*
import kotlinx.android.synthetic.main.fragment_task_in_progress_list_employee.*
import kotlinx.android.synthetic.main.fragment_task_list_employee.*
import kotlinx.android.synthetic.main.fragment_task_list_employee.AvailableList
import kotlinx.android.synthetic.main.fragment_task_list_employee.DoneListNav
import kotlinx.android.synthetic.main.fragment_task_list_employee.InProgressList
import kotlinx.android.synthetic.main.fragment_task_list_employee.logout

class DashboardTaskInProgressListEmployee : Fragment() {

    lateinit var taskViewModel: EmployeeTaskInProgressListViewModel
    lateinit var linearManager: LinearLayoutManager
    lateinit var adapterTask: EmployeeTaskInProgressListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskViewModel = ViewModelProvider(requireActivity()).get(EmployeeTaskInProgressListViewModel::class.java)
        linearManager = LinearLayoutManager(requireContext())

        taskViewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapterTask.notifyDataSetChanged()
        })

        adapterTask = EmployeeTaskInProgressListAdapter(taskViewModel.allTasks, taskViewModel)

        taskViewModel.getTasksInProgressForEmployee(State.currentEmployeeId)

        return inflater.inflate(R.layout.fragment_task_in_progress_list_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AvailableList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_dashboardTaskListEmployee) }
        InProgressList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_self) }
        DoneListNav.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_dashboardTaskDoneListEmployee) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_to_loginEmployee) }

        inProgressList_recycler_view.addItemDecoration(DividerItemDecoration(inProgressList_recycler_view.context, DividerItemDecoration.VERTICAL))
        inProgressList_recycler_view.apply {
            adapter = adapterTask
            layoutManager = linearManager
            taskViewModel.getTasksInProgressForEmployee(State.currentEmployeeId)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardTaskInProgressListEmployee {
            return DashboardTaskInProgressListEmployee()
        }
    }
}