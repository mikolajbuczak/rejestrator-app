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
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeeListViewModel
import com.example.rejestrator.view.viewmodel.Admin.AdminEmployeesViewModel
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
                    if(id.length != 4 && pin.length != 4)
                        Toast.makeText(requireContext(), "Id i pin muszą składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else if(id.length != 4 )
                        Toast.makeText(requireContext(), "Id musi składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else if(pin.length != 4 )
                        Toast.makeText(requireContext(), "Pin musi składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else{
                        var canAddEmployee = ApiRepository.canAddEmployee(id)

                        canAddEmployee.enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(requireContext(), "Błąd! Nie połączono z bazą danych.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.code() == 200) {
                                    adminEmployeeViewModel.insertEmployee(id, pin, name, surname, shift)
                                    Toast.makeText(requireContext(), "Dodano pracownika.", Toast.LENGTH_SHORT).show()
                                    mAlertDialog.dismiss()
                                    view.findNavController().navigate(R.id.action_dashboardEmployeesAdmin_self)
                                } else if (response.code() == 404) {
                                    Toast.makeText(requireContext(), "To id jest już przypisane.", Toast.LENGTH_SHORT).show()
                                }
                            }

                        })
                    }

                }
                else
                    Toast.makeText(requireContext(), "Pozostawiono puste pola.", Toast.LENGTH_SHORT).show();
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
                val id = mDialogView.addAdminId.text.toString()
                val username = mDialogView.addAdminUsername.text.toString()
                val password = mDialogView.addAdminPassword.text.toString()
                val name = mDialogView.addAdminName.text.toString()
                val surname = mDialogView.addAdminSurname.text.toString()

                if (!id.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty()) {
                    if (id.length != 4)
                        Toast.makeText(
                                requireContext(),
                                "Id musi składać się z 4 cyfr.",
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
                                var loginCall = ApiRepository.canAdminLogin(
                                        State.currentAdminUsername,
                                        passwordConfirm
                                )

                                loginCall.enqueue(object : Callback<AdminLoginData> {
                                    override fun onFailure(
                                            call: Call<AdminLoginData>,
                                            t: Throwable
                                    ) {
                                        Toast.makeText(
                                                requireContext(),
                                                "Błąd! Nie połączono z bazą danych.",
                                                Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onResponse(
                                            call: Call<AdminLoginData>,
                                            response: Response<AdminLoginData>
                                    ) {
                                        if (response.code() == 200) {
                                            var canAddAdmin =
                                                    ApiRepository.canAddAdmin(id, username)

                                            canAddAdmin.enqueue(object : Callback<ResponseBody> {
                                                override fun onFailure(
                                                        call: Call<ResponseBody>,
                                                        t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                            requireContext(),
                                                            "Błąd! Nie połączono z bazą danych.",
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
                                                                "Id oraz nazwa użytkownika zostały już przypisane.",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else if (response.code() == 402) {
                                                        mAlertDialog2.dismiss()
                                                        Toast.makeText(
                                                                requireContext(),
                                                                "Id zostało już przypisane.",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else if (response.code() == 401) {
                                                        mAlertDialog2.dismiss()
                                                        Toast.makeText(
                                                                requireContext(),
                                                                "Nazwa użytkownika została już przypisana.",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else if (response.code() == 200) {
                                                        adminEmployeeViewModel.insertAdmin(
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
                                                                "Dodano administratora.",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    }

                                                }

                                            })
                                        } else if (response.code() == 404) {
                                            Toast.makeText(
                                                    requireContext(),
                                                    "Niepoprawne hasło.",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }

                                })
                            } else
                                Toast.makeText(
                                        requireContext(),
                                        "Nie wpisano hasła.",
                                        Toast.LENGTH_SHORT
                                ).show();
                        }

                        mDialogView2.confirmCancelButton.setOnClickListener {
                            mAlertDialog2.dismiss()
                        }


                    }
                } else
                    Toast.makeText(requireContext(), "Pozostawiono puste pola.", Toast.LENGTH_SHORT)
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
                                    "Błąd! Nie połączono z bazą danych.",
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
                                                "Błąd! Nie połączono z bazą danych.",
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
                                                    "Dodano zadanie",
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                            mAlertDialog.dismiss()
                                        } else if (response.code() == 404) {
                                            Toast.makeText(
                                                    requireContext(),
                                                    "Nie dodano zadania",
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                            mAlertDialog.dismiss()
                                        }
                                    }

                                })
                            } else if (response.code() == 404) {
                                Toast.makeText(
                                        requireContext(),
                                        "To zadanie zostało już przydzielone temu pracownikowi.",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                } else
                    Toast.makeText(
                            requireContext(),
                            "Pozostawiono puste pola.",
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
                        if  (it.employeeID.toLowerCase(Locale.getDefault()).contains(search) || it.name.toLowerCase(Locale.getDefault()).contains(search) || it.surname.toLowerCase(Locale.getDefault()).contains(search))
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

