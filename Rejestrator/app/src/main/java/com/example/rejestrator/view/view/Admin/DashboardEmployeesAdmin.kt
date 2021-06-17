package com.example.rejestrator.view.view.Admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.adapters.Admin.AdminEmployeeLogsAdapter
import com.example.rejestrator.view.adapters.Admin.AdminEmployeesAdapter
import com.example.rejestrator.view.model.entities.AdminLoginData
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_admin_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.addCancelButton
import kotlinx.android.synthetic.main.add_employee_dialog.view.addOkButton
import kotlinx.android.synthetic.main.add_task_dialog.view.*
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.*
import kotlinx.android.synthetic.main.fragment_admin_employee_list.*
import kotlinx.android.synthetic.main.fragment_admin_employee_list.logout
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
import kotlinx.android.synthetic.main.fragment_employee_list_admin.employeeList
import kotlinx.android.synthetic.main.fragment_employee_list_admin.logsList
import kotlinx.android.synthetic.main.fragment_logs_list_admin.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DashboardEmployeesAdmin : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    lateinit var adminEmployeeViewModel: AdminEmployeesViewModel
    lateinit var linearManager: LinearLayoutManager
    lateinit var adapterEmployees: AdminEmployeesAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        adminEmployeeViewModel = ViewModelProvider(requireActivity()).get(AdminEmployeesViewModel::class.java)
        linearManager = LinearLayoutManager(requireContext())

        adminEmployeeViewModel.filteredEmployeeList.observe(viewLifecycleOwner, Observer {
            adapterEmployees.notifyDataSetChanged()
        })

        adminEmployeeViewModel.getAllEmployees()
        adminEmployeeViewModel.getAllEmployeesForTaskAdding()

        return inflater.inflate(R.layout.fragment_admin_employee_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_to_dashboardLogsListAdmin) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_self) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_to_loginAdmin) }

        adapterEmployees = AdminEmployeesAdapter(adminEmployeeViewModel.filteredEmployeeList, adminEmployeeViewModel)

        employeeList_recycler_view.addItemDecoration(DividerItemDecoration(employeeList_recycler_view.context, DividerItemDecoration.VERTICAL))

        employeeList_recycler_view.apply {
            adapter = adapterEmployees
            layoutManager = linearManager
            adminEmployeeViewModel.getAllEmployees()
        }

        var searchItem = search_view_Employees as SearchView
        searching((searchItem))

        add2.setOnClickListener {
            onAddButtonClicked()
        }

        addEmployee2.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_employee_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                    .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.shifts_array, R.layout.spinner_item
            )

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            mDialogView.addEmployeeShift.adapter = adapter

            mDialogView.addOkButton.setOnClickListener {
                val id = mDialogView.addEmployeeId.text.toString()
                val pin = mDialogView.addEmployeePin.text.toString()
                val name = mDialogView.addEmployeeName.text.toString()
                val surname = mDialogView.addEmployeeSurname.text.toString()
                val shift = mDialogView.addEmployeeShift.selectedItem.toString()
                if(!id.isNullOrEmpty() && !pin.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty() && !shift.isNullOrEmpty()) {
                    if(id.length != 4 && pin.length != 6)
                        Toast.makeText(requireContext(), getString(R.string.id_pin_4), Toast.LENGTH_SHORT).show()
                    else if(id.length != 4 )
                        Toast.makeText(requireContext(), getString(R.string.id_4), Toast.LENGTH_SHORT).show()
                    else if(pin.length != 6 )
                        Toast.makeText(requireContext(), getString(R.string.pin_4), Toast.LENGTH_SHORT).show()
                    else{
                        val email = "${id}@rejestrator.com"
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pin)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful) {
                                    val uid = task.result!!.user!!.uid
                                    val employee = EmployeeLoginData(id, pin, name, surname, shift)
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(employee)
                                    Toast.makeText(requireContext(), getString(R.string.employee_added), Toast.LENGTH_SHORT).show()
                                    mAlertDialog.dismiss()
                                }
                                else {
                                    Toast.makeText(requireContext(), getString(R.string.id_assigned), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }

                }
                else
                    Toast.makeText(requireContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            }

            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        addAdmin2.setOnClickListener {
            val mDialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.add_admin_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                    .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.addOkButton.setOnClickListener {
                val username = mDialogView.addAdminUsername.text.toString()
                val password = mDialogView.addAdminPassword.text.toString()
                val name = mDialogView.addAdminName.text.toString()
                val surname = mDialogView.addAdminSurname.text.toString()

                if (!username.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty()) {
                    if (false)
                        Toast.makeText(
                                requireContext(),
                                getString(R.string.id_4),
                                Toast.LENGTH_SHORT
                        ).show()
                    else {
                        val mDialogView2 = LayoutInflater.from(requireContext())
                                .inflate(R.layout.confirm_with_password_dialog, null)
                        val mBuilder2 =
                                AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                                        .setView(mDialogView2)
                        val mAlertDialog2 = mBuilder2.show()

                        mDialogView2.confirmOkButton.setOnClickListener {
                            val passwordConfirm = mDialogView2.confirmPassword.text.toString()

                            if (!passwordConfirm.isNullOrEmpty()) {
                                if(passwordConfirm == State.currentAdminPassword){
                                    mAlertDialog2.dismiss()
                                    val email = "${username}@rejestrator.com"
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if(task.isSuccessful) {
                                                val uid = task.result!!.user!!.uid
                                                val admin = AdminLoginData(username, password, name, surname)
                                                FirebaseDatabase.getInstance().getReference().child("admins").child(uid).setValue(admin)
                                                Toast.makeText(requireContext(), getString(R.string.admin_added), Toast.LENGTH_SHORT).show()
                                                mAlertDialog.dismiss()
                                            }
                                            else {
                                                Toast.makeText(requireContext(), getString(R.string.username_assigned), Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                                else
                                    Toast.makeText(requireContext(), getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(
                                        requireContext(),
                                        getString(R.string.no_password),
                                        Toast.LENGTH_SHORT
                                ).show();
                        }

                        mDialogView2.confirmCancelButton.setOnClickListener {
                            mAlertDialog2.dismiss()
                        }


                    }
                } else
                    Toast.makeText(requireContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT)
                            .show();

            }
            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }


        addTask2.setOnClickListener {
            adminEmployeeViewModel.getAllEmployeesForTaskAdding()
            val mDialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.add_task_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                    .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            val adapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.spinner_item, adminEmployeeViewModel.employeeList2)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            mDialogView.addTaskSelectedEmployee.adapter = adapter


            mDialogView.addOkButton.setOnClickListener {
                val selectedEmployee = mDialogView.addTaskSelectedEmployee.selectedItem.toString()
                val task = mDialogView.addTask.text.toString()
                val selectedEmployeeID = selectedEmployee.split(" ").first()
                if (!selectedEmployee.isNullOrEmpty() && !task.isNullOrEmpty()) {
                    val taskUUID = UUID.randomUUID()
                    val newTask = Task(taskUUID.toString() ,selectedEmployeeID, task)
                    FirebaseDatabase.getInstance().getReference().child("tasks").child(selectedEmployeeID).child(taskUUID).setValue(newTask).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.task_added),
                                Toast.LENGTH_SHORT
                            ).show()
                            mAlertDialog.dismiss()
                        }
                        else{
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.task_not_added),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_fields),
                            Toast.LENGTH_SHORT
                    ).show();
            }
            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardEmployeesAdmin {
            return DashboardEmployeesAdmin()
        }
    }

    private fun searching(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText!!.isNotEmpty()){
                    adminEmployeeViewModel.filteredEmployeeList.value?.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    adminEmployeeViewModel.employeeList.value?.forEach({
                        if  (it.employeeID!!.toLowerCase(Locale.getDefault()).contains(search) || it.name!!.toLowerCase(Locale.getDefault()).contains(search) || it.surname!!.toLowerCase(Locale.getDefault()).contains(search))
                            adminEmployeeViewModel.filteredEmployeeList.value?.add(it)
                    })
                    employeeList_recycler_view.adapter!!.notifyDataSetChanged()
                }
                else{
                    adminEmployeeViewModel.filteredEmployeeList.value?.clear()
                    adminEmployeeViewModel.employeeList.value?.forEach {
                        adminEmployeeViewModel.filteredEmployeeList.value?.add(it)
                    }
                    employeeList_recycler_view.adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            addEmployee2.visibility = View.VISIBLE
            addTask2.visibility = View.VISIBLE
            addAdmin2.visibility = View.VISIBLE
        }else{
            addEmployee2.visibility = View.GONE
            addTask2.visibility = View.GONE
            addAdmin2.visibility = View.GONE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            addEmployee2.startAnimation(fromBottom)
            addTask2.startAnimation(fromBottom)
            addAdmin2.startAnimation(fromBottom)

            add2.startAnimation(rotateOpen)
        }else{
            addEmployee2.startAnimation(toBottom)
            addTask2.startAnimation(toBottom)
            addAdmin2.startAnimation(toBottom)

            add2.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            addEmployee2.isClickable = true
            addTask2.isClickable = true
            addAdmin2.isClickable = true
        }else{
            addEmployee2.isClickable = false
            addTask2.isClickable = false
            addAdmin2.isClickable = false
        }
    }
}

