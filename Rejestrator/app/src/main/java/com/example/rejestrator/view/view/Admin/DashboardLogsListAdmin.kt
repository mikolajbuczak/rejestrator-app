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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.view.Employee.DashboardTaskDoneListEmployee
import com.example.rejestrator.view.view.Employee.DashboardTaskListEmployee
import kotlinx.android.synthetic.main.add_admin_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.*
import kotlinx.android.synthetic.main.add_employee_dialog.view.addCancelButton
import kotlinx.android.synthetic.main.add_employee_dialog.view.addOkButton
import kotlinx.android.synthetic.main.add_task_dialog.*
import kotlinx.android.synthetic.main.add_task_dialog.view.*
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.*
import kotlinx.android.synthetic.main.edit_employee_dialog.view.*
import kotlinx.android.synthetic.main.fragment_login_administrator.*
import kotlinx.android.synthetic.main.fragment_logs_list_admin.*
import kotlinx.android.synthetic.main.fragment_logs_list_admin.addTask

class DashboardLogsListAdmin : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logs_list_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_self) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_to_dashboardEmployeeListAdmin) }
        DoneList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_to_dashboardRaportAdmin) }

        logout.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardLogsListAdmin_to_loginAdmin) }

        //populate recycler view, and connect to searchView

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
                        Toast.makeText(requireContext(), "Id i pin muszą składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else if(id.length != 4 )
                        Toast.makeText(requireContext(), "Id musi składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else if(pin.length != 4 )
                        Toast.makeText(requireContext(), "Pin musi składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    //To do: add employee in database, check if id used etc.

                }
                else
                    Toast.makeText(requireContext(), "Pozostawiono puste pola.", Toast.LENGTH_SHORT).show();
            }

            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        addAdmin.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_admin_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.addOkButton.setOnClickListener {
                val id = mDialogView.addAdminId.text.toString()
                val username = mDialogView.addAdminUsername.text.toString()
                val password = mDialogView.addAdminPassword.text.toString()
                val name = mDialogView.addAdminName.text.toString()
                val surname = mDialogView.addAdminSurname.text.toString()

                if(!id.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty()) {
                    if(id.length != 4 )
                        Toast.makeText(requireContext(), "Id musi składać się z 4 cyfr.", Toast.LENGTH_SHORT).show()
                    else if(true){
                        //check if id used etc. change true to condition

                        val mDialogView2 = LayoutInflater.from(requireContext()).inflate(R.layout.confirm_with_password_dialog, null)
                        val mBuilder2 = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                            .setView(mDialogView2)
                        val mAlertDialog2 = mBuilder2.show()

                        mDialogView2.confirmOkButton.setOnClickListener{
                            val passwordConfirm = mDialogView2.confirmPassword.text.toString()

                            if(!passwordConfirm.isNullOrEmpty()){
                                if(true){
                                    //check if password correct then add admin here
                                }
                                else
                                    Toast.makeText(requireContext(), "Niepoprawne hasło.", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(requireContext(), "Nie wpisano hasła.", Toast.LENGTH_SHORT).show();
                        }

                        mDialogView2.confirmCancelButton.setOnClickListener{
                            mAlertDialog2.dismiss()
                        }

                    }

                }
                else
                    Toast.makeText(requireContext(), "Pozostawiono puste pola.", Toast.LENGTH_SHORT).show();
            }

            mDialogView.addCancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        addTask.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_task_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            /* SETUP OF SPINNER TO ADD TASK, FILL WITH EMPLOYEES FROM DATABASE
            val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.shifts_array, R.layout.spinner_item
            )

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            mDialogView.addTaskSelectedEmployee.adapter = adapter*/

            mDialogView.addOkButton.setOnClickListener {
                val selectedEmployee = mDialogView.addTaskSelectedEmployee.selectedItem.toString()
                val task = mDialogView.addTask.text.toString()
                if(!selectedEmployee.isNullOrEmpty() && !task.isNullOrEmpty()) {
                    //To do: add task to database, check if already added to useretc.

                }
                else
                    Toast.makeText(requireContext(), "Pozostawiono puste pola.", Toast.LENGTH_SHORT).show();
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