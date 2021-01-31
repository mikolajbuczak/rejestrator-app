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
import com.example.rejestrator.view.adapters.Admin.AdminLogsListAdapter
import com.example.rejestrator.view.model.entities.AdminLoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Admin.AdminLogsListViewModel
import kotlinx.android.synthetic.main.add_admin_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.addCancelButton
import kotlinx.android.synthetic.main.add_employee_dialog.view.addOkButton
import kotlinx.android.synthetic.main.add_task_dialog.view.*
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.*
import kotlinx.android.synthetic.main.fragment_logs_list_admin.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DashboardLogsListAdmin : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    lateinit var logsViewModel: AdminLogsListViewModel
    lateinit var linearManager: LinearLayoutManager
    lateinit var adapterLogs: AdminLogsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        logsViewModel = ViewModelProvider(requireActivity()).get(AdminLogsListViewModel::class.java)
        linearManager = LinearLayoutManager(requireContext())

        logsViewModel.filteredAllLogs.observe(viewLifecycleOwner, Observer {
            adapterLogs.notifyDataSetChanged()
        })

        logsViewModel.getAllLogs()
        logsViewModel.getAllEmployeesForTaskAdding()

        return inflater.inflate(R.layout.fragment_logs_list_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_self) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_to_dashboardEmployeesAdmin) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_to_loginAdmin) }

        adapterLogs = AdminLogsListAdapter(logsViewModel.filteredAllLogs, logsViewModel)

        logsList_recycler_view.addItemDecoration(DividerItemDecoration(logsList_recycler_view.context, DividerItemDecoration.VERTICAL))

        logsList_recycler_view.apply {
            adapter = adapterLogs
            layoutManager = linearManager
            logsViewModel.getAllLogs()
        }

        var searchItem = search_view as SearchView
        searching((searchItem))

        add.setOnClickListener {
            onAddButtonClicked()
        }

        addEmployee.setOnClickListener {
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
                    if(id.length != 4 && pin.length != 4)
                        Toast.makeText(requireContext(), getString(R.string.id_pin_4), Toast.LENGTH_SHORT).show()
                    else if(id.length != 4 )
                        Toast.makeText(requireContext(), getString(R.string.id_4), Toast.LENGTH_SHORT).show()
                    else if(pin.length != 4 )
                        Toast.makeText(requireContext(), getString(R.string.pin_4), Toast.LENGTH_SHORT).show()
                    else{
                        var canAddEmployee = ApiRepository.canAddEmployee(id)

                        canAddEmployee.enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(requireContext(), getString(R.string.no_conn), Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.code() == 200) {
                                    logsViewModel.insertEmployee(id, pin, name, surname, shift)
                                    Toast.makeText(requireContext(), getString(R.string.employee_added), Toast.LENGTH_SHORT).show()
                                    mAlertDialog.dismiss()
                                } else if (response.code() == 404) {
                                    Toast.makeText(requireContext(), getString(R.string.id_assigned), Toast.LENGTH_SHORT).show()
                                }
                            }

                        })
                    }

                }
                else
                    Toast.makeText(requireContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            }

            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        addAdmin.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_admin_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.addOkButton.setOnClickListener {
                val id = mDialogView.addAdminId.text.toString()
                val username = mDialogView.addAdminUsername.text.toString()
                val password = mDialogView.addAdminPassword.text.toString()
                val name = mDialogView.addAdminName.text.toString()
                val surname = mDialogView.addAdminSurname.text.toString()

                if (!id.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty()) {
                    if (id.length != 4)
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
                                    var canAddAdmin =
                                            ApiRepository.canAddAdmin(id, username)

                                    canAddAdmin.enqueue(object : Callback<ResponseBody> {
                                        override fun onFailure(
                                                call: Call<ResponseBody>,
                                                t: Throwable
                                        ) {
                                            Toast.makeText(
                                                    requireContext(),
                                                    getString(R.string.no_conn),
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onResponse(
                                                call: Call<ResponseBody>,
                                                response: Response<ResponseBody>
                                        ) {
                                            if (response.code() == 404) {
                                                mAlertDialog2.dismiss()
                                                Toast.makeText(
                                                        requireContext(),
                                                        getString(R.string.id_username_assigned),
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (response.code() == 402) {
                                                mAlertDialog2.dismiss()
                                                Toast.makeText(
                                                        requireContext(),
                                                        getString(R.string.id_assigned),
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (response.code() == 401) {
                                                mAlertDialog2.dismiss()
                                                Toast.makeText(
                                                        requireContext(),
                                                        getString(R.string.username_assigned),
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (response.code() == 200) {
                                                logsViewModel.insertAdmin(
                                                        id,
                                                        username,
                                                        password,
                                                        name,
                                                        surname
                                                )
                                                mAlertDialog2.dismiss()
                                                mAlertDialog.dismiss()
                                                Toast.makeText(
                                                        requireContext(),
                                                        getString(R.string.admin_added),
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }

                                    })
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


            addTask.setOnClickListener {
                logsViewModel.getAllEmployeesForTaskAdding()
                val mDialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.add_task_dialog, null)
                val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()

                val adapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.spinner_item, logsViewModel.employeeList2)

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                mDialogView.addTaskSelectedEmployee.adapter = adapter


                mDialogView.addOkButton.setOnClickListener {
                    val selectedEmployee =
                        mDialogView.addTaskSelectedEmployee.selectedItem.toString()
                    val task = mDialogView.addTask.text.toString()
                    if (!selectedEmployee.isNullOrEmpty() && !task.isNullOrEmpty()) {
                        var canAddTask =
                            ApiRepository.canAddTask(selectedEmployee.split(" ").first(), task)

                        canAddTask.enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.no_conn),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.code() == 200) {
                                    var addTask = ApiRepository.addTask(
                                        selectedEmployee.split(" ").first(),
                                        task
                                    )

                                    addTask.enqueue(object : Callback<ResponseBody> {
                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                requireContext(),
                                                getString(R.string.no_conn),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {
                                            if (response.code() == 200) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    getString(R.string.task_added),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                mAlertDialog.dismiss()
                                            } else if (response.code() == 404) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    getString(R.string.task_not_added),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                mAlertDialog.dismiss()
                                            }
                                        }

                                    })
                                } else if (response.code() == 404) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.task_assigned),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        })
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
        fun newInstance(): DashboardLogsListAdmin {
            return DashboardLogsListAdmin()
        }
    }

    private fun searching(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText!!.isNotEmpty()){
                    logsViewModel.filteredAllLogs.value?.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    logsViewModel.allLogs.value?.forEach({
                        if  (it.employeeID.toLowerCase(Locale.getDefault()).contains(search) || it.name.toLowerCase(Locale.getDefault()).contains(search) || it.surname.toLowerCase(Locale.getDefault()).contains(search) || it.date.toLowerCase(Locale.getDefault()).contains(search))
                            logsViewModel.filteredAllLogs.value?.add(it)
                    })
                    logsList_recycler_view.adapter!!.notifyDataSetChanged()
                }
                else{
                    logsViewModel.filteredAllLogs.value?.clear()
                    logsViewModel.allLogs.value?.forEach {
                        logsViewModel.filteredAllLogs.value?.add(it)
                    }
                    logsList_recycler_view.adapter!!.notifyDataSetChanged()
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
            addEmployee.visibility = View.VISIBLE
            addTask.visibility = View.VISIBLE
            addAdmin.visibility = View.VISIBLE
        }else{
            addEmployee.visibility = View.GONE
            addTask.visibility = View.GONE
            addAdmin.visibility = View.GONE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            addEmployee.startAnimation(fromBottom)
            addTask.startAnimation(fromBottom)
            addAdmin.startAnimation(fromBottom)

            add.startAnimation(rotateOpen)
        }else{
            addEmployee.startAnimation(toBottom)
            addTask.startAnimation(toBottom)
            addAdmin.startAnimation(toBottom)

            add.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            addEmployee.isClickable = true
            addTask.isClickable = true
            addAdmin.isClickable = true
        }else{
            addEmployee.isClickable = false
            addTask.isClickable = false
            addAdmin.isClickable = false
        }
    }

}
