package com.example.rejestrator.view.view.Admin

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import retrofit2.Callback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.adapters.Admin.*
import com.example.rejestrator.view.model.entities.*
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.add_admin_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.addCancelButton
import kotlinx.android.synthetic.main.add_employee_dialog.view.addOkButton
import kotlinx.android.synthetic.main.add_task_dialog.view.*
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.*
import kotlinx.android.synthetic.main.edit_employee_dialog.view.*
import kotlinx.android.synthetic.main.fragment_admin_employee_list.*
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
import kotlinx.android.synthetic.main.fragment_employee_list_admin.employeeList
import kotlinx.android.synthetic.main.fragment_employee_list_admin.logsList
import kotlinx.android.synthetic.main.fragment_logs_list_admin.*
import kotlinx.android.synthetic.main.fragment_logs_list_admin.logout
import kotlinx.android.synthetic.main.fragment_raport_list_admin.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class DashboardEmployeeListAdmin : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    lateinit var adminEmployeeViewModel: AdminEmployeeListViewModel
    lateinit var linearManager: LinearLayoutManager
    lateinit var linearManager1: LinearLayoutManager
    lateinit var linearManager2: LinearLayoutManager
    lateinit var linearManager3: LinearLayoutManager
    lateinit var adapterEmployeeLogs: AdminEmployeeLogsAdapter
    lateinit var adapterEmployeeTasks: AdminEmployeeTasksAdapter
    lateinit var adapterEmployeeTasksInProgress: AdminEmployeeTasksInProgressAdapter
    lateinit var adapterEmployeeTasksDone: AdminEmployeeTasksDoneAdapter
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        adminEmployeeViewModel = ViewModelProvider(requireActivity()).get(AdminEmployeeListViewModel::class.java)
        linearManager = LinearLayoutManager(requireContext())
        linearManager1 = LinearLayoutManager(requireContext())
        linearManager2 = LinearLayoutManager(requireContext())
        linearManager3 = LinearLayoutManager(requireContext())

        adminEmployeeViewModel.employeeLogs.observe(viewLifecycleOwner, Observer {
            adapterEmployeeLogs.notifyDataSetChanged()
        })

        adminEmployeeViewModel.employeeTasks.observe(viewLifecycleOwner, Observer {
            adapterEmployeeTasks.notifyDataSetChanged()
        })

        adminEmployeeViewModel.employeeTasksInProgress.observe(viewLifecycleOwner, Observer {
            adapterEmployeeTasksInProgress.notifyDataSetChanged()
        })

        adminEmployeeViewModel.employeeTasksDone.observe(viewLifecycleOwner, Observer {
            adapterEmployeeTasksDone.notifyDataSetChanged()
        })

        adminEmployeeViewModel.fillLogsLists(State.selectedEmployeeId)
        adminEmployeeViewModel.fillTaskLists(State.selectedEmployeeId)
        adminEmployeeViewModel.fillTasksInProgressLists(State.selectedEmployeeId)
        adminEmployeeViewModel.fillTaskDoneLists(State.selectedEmployeeId)

        return inflater.inflate(R.layout.fragment_employee_list_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardEmployeeListAdmin_to_dashboardLogsListAdmin) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardEmployeeListAdmin_to_dashboardEmployeesAdmin) }

        if(State.selectedEmployeeName.count() < State.selectedEmployeeSurname.count())
        {
            employeeLabelName.setText(State.selectedEmployeeName)
            employeeLabelSurname.setText(State.selectedEmployeeSurname)
        }
        else{
            employeeLabelName.setText(State.selectedEmployeeSurname)
            employeeLabelSurname.setText(State.selectedEmployeeName)
        }

        adapterEmployeeLogs = AdminEmployeeLogsAdapter(adminEmployeeViewModel.employeeLogs, adminEmployeeViewModel)

        datesList_recycler_view.addItemDecoration(DividerItemDecoration(datesList_recycler_view.context, DividerItemDecoration.VERTICAL))

        datesList_recycler_view.apply {
            adapter = adapterEmployeeLogs
            layoutManager = linearManager
            adminEmployeeViewModel.fillLogsLists(State.selectedEmployeeWithId.split(" ").first())
        }

        adapterEmployeeTasks = AdminEmployeeTasksAdapter(adminEmployeeViewModel.employeeTasks, adminEmployeeViewModel)

        taskList_recycler_view.addItemDecoration(DividerItemDecoration(taskList_recycler_view.context, DividerItemDecoration.VERTICAL))

        taskList_recycler_view.apply {
            adapter = adapterEmployeeTasks
            layoutManager = linearManager1
            adminEmployeeViewModel.fillTaskLists(State.selectedEmployeeWithId.split(" ").first())
        }

        adapterEmployeeTasksInProgress = AdminEmployeeTasksInProgressAdapter(adminEmployeeViewModel.employeeTasksInProgress, adminEmployeeViewModel)

        inProgressTaskList_recycler_view.addItemDecoration(DividerItemDecoration(inProgressTaskList_recycler_view.context, DividerItemDecoration.VERTICAL))

        inProgressTaskList_recycler_view.apply {
            adapter = adapterEmployeeTasksInProgress
            layoutManager = linearManager2
            adminEmployeeViewModel.fillTasksInProgressLists(State.selectedEmployeeWithId.split(" ").first())
        }

        adapterEmployeeTasksDone = AdminEmployeeTasksDoneAdapter(adminEmployeeViewModel.employeeTasksDone, adminEmployeeViewModel)

        doneTasksList_recycler_view.addItemDecoration(DividerItemDecoration(doneTasksList_recycler_view.context, DividerItemDecoration.VERTICAL))

        doneTasksList_recycler_view.apply {
            adapter = adapterEmployeeTasksDone
            layoutManager = linearManager3
            adminEmployeeViewModel.fillTaskDoneLists(State.selectedEmployeeWithId.split(" ").first())
        }

        editEmployeeButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_employee_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(mDialogView)

            val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.shifts_array, R.layout.spinner_item
            )

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            mDialogView.editEmployeeShift.adapter = adapter

            mDialogView.editEmployeePin.setText(State.selectedEmployeePin)
            mDialogView.editEmployeeName.setText(State.selectedEmployeeName)
            mDialogView.editEmployeeSurname.setText(State.selectedEmployeeSurname)
            if(State.selectedEmployeeShift == "Dzienny"){
                mDialogView.editEmployeeShift.setSelection(0)
            }
            else
                mDialogView.editEmployeeShift.setSelection(1)


            val mAlertDialog = mBuilder.show()

            mDialogView.editOkButton.setOnClickListener {
                val id = State.selectedEmployeeId
                val pin = mDialogView.editEmployeePin.text.toString()
                val name = mDialogView.editEmployeeName.text.toString()
                val surname = mDialogView.editEmployeeSurname.text.toString()
                val shift = mDialogView.editEmployeeShift.selectedItem.toString()
                if(!pin.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty() && !shift.isNullOrEmpty()) {
                    if(id.length != 4 || pin.length != 6)
                        Toast.makeText(requireContext(), getString(R.string.id_pin_4), Toast.LENGTH_SHORT).show()
                    else if(id.length != 4 )
                        Toast.makeText(requireContext(), getString(R.string.id_4), Toast.LENGTH_SHORT).show()
                    else if(pin.length != 6 )
                        Toast.makeText(requireContext(), getString(R.string.pin_4), Toast.LENGTH_SHORT).show()
                    else {
                        val employee = EmployeeLoginData(id, pin, name, surname, shift)
                        var userKey = ""
                        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Log.d("users", "Error while getting users")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (userSnapshot in snapshot.children) {
                                    val user = userSnapshot.getValue(EmployeeListData::class.java)
                                    if (user!!.employeeID == id)
                                        userKey = userSnapshot.getKey().toString()
                                    Log.d("KEY", "key: $userKey")
                                    if (userKey != "") {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(userKey).setValue(employee)
                                        Toast.makeText(requireContext(), getString(R.string.employee_edited), Toast.LENGTH_SHORT).show()
                                        State.selectedEmployeeId = id
                                        State.selectedEmployeePin = pin
                                        State.selectedEmployeeName = name
                                        State.selectedEmployeeSurname = surname
                                        State.selectedEmployeeShift = shift
                                        if(name.count() < surname.count())
                                        {
                                            employeeLabelName.setText(name)
                                            employeeLabelSurname.setText(surname)
                                        }
                                        else{
                                            employeeLabelName.setText(surname)
                                            employeeLabelSurname.setText(name)
                                        }
                                        mAlertDialog.dismiss()
                                    }
                                }
                            }
                        })
                    }
                }
                else
                    Toast.makeText(requireContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            }

            mDialogView.editCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        deleteEmployeeButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_with_password_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(mDialogView)
            val mAlertDialog2 = mBuilder.show()

            mDialogView.confirmOkButton.setOnClickListener{
                val passwordConfirm = mDialogView.confirmPassword.text.toString()

                if(!passwordConfirm.isNullOrEmpty()){
                    if(passwordConfirm == State.currentAdminPassword){
                        adminEmployeeViewModel.deleteEmployee(State.selectedEmployeeWithId.split(" ").first())
                        mAlertDialog2.dismiss()
                        view.findNavController().navigate(R.id.action_dashboardEmployeeListAdmin_to_dashboardEmployeesAdmin)
                    }
                    else {
                        Toast.makeText(requireContext(), getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(requireContext(), getString(R.string.no_password), Toast.LENGTH_SHORT).show();
            }

            mDialogView.confirmCancelButton.setOnClickListener{
                mAlertDialog2.dismiss()
            }
        }

        changeRecyclerRight.setOnClickListener {
            if(textLogs2.visibility == View.VISIBLE){
                textLogs2.visibility = View.INVISIBLE
                datesList_recycler_view.visibility = View.INVISIBLE

                textTask.visibility = View.VISIBLE
                taskList_recycler_view.visibility = View.VISIBLE
            }
            else if(textTask.visibility == View.VISIBLE){
                textTask.visibility = View.INVISIBLE
                taskList_recycler_view.visibility = View.INVISIBLE

                textTaskInProgress.visibility = View.VISIBLE
                inProgressTaskList_recycler_view.visibility = View.VISIBLE
            }
            else if(textTaskInProgress.visibility == View.VISIBLE){
                textTaskInProgress.visibility = View.INVISIBLE
                inProgressTaskList_recycler_view.visibility = View.INVISIBLE

                textTaskDone.visibility = View.VISIBLE
                doneTasksList_recycler_view.visibility = View.VISIBLE
            }
        }

        changeRecyclerLeft.setOnClickListener {
            if(textTaskDone.visibility == View.VISIBLE){
                textTaskDone.visibility = View.INVISIBLE
                doneTasksList_recycler_view.visibility = View.INVISIBLE

                textTaskInProgress.visibility = View.VISIBLE
                inProgressTaskList_recycler_view.visibility = View.VISIBLE
            }
            else if(textTaskInProgress.visibility == View.VISIBLE){
                textTaskInProgress.visibility = View.INVISIBLE
                inProgressTaskList_recycler_view.visibility = View.INVISIBLE

                textTask.visibility = View.VISIBLE
                taskList_recycler_view.visibility = View.VISIBLE
            }
            else if(textTask.visibility == View.VISIBLE){
                textTask.visibility = View.INVISIBLE
                taskList_recycler_view.visibility = View.INVISIBLE

                textLogs2.visibility = View.VISIBLE
                datesList_recycler_view.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardEmployeeListAdmin {
            return DashboardEmployeeListAdmin()
        }
    }

}