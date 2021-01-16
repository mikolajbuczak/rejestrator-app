package com.example.rejestrator.view.adapters.Employee

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskListViewModel
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.*
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmCancelButton
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmOkButton
import kotlinx.android.synthetic.main.confirm_with_pin_dialog.view.*
import kotlinx.android.synthetic.main.fragment_login_employee.*
import kotlinx.android.synthetic.main.one_row_available_list.view.*
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EmployeeTaskInProgressListAdapter(var taskList: LiveData<List<TaskInProgress>>, var taskViewModel: EmployeeTaskInProgressListViewModel) : RecyclerView.Adapter<EmployeeTaskInProgressListAdapter.Holder>() {

    class Holder(val view: View): RecyclerView.ViewHolder(view) {
        val textView1= view.findViewById<TextView>(R.id.row_inProgressTask)
    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_in_progress_list,parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size?:0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentItem = taskList.value?.get(position)

        holder.textView1.text=taskList.value?.get(position)?.task

        holder.view.row_endTaskButton.setOnClickListener {x->
            if (currentItem != null) {

                val mDialogView = LayoutInflater.from(x.context).inflate(R.layout.confirm_with_pin_dialog, null)
                val mBuilder = AlertDialog.Builder(x.context, R.style.CustomAlertDialog)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                var employeeLoginData : EmployeeLoginData

                mDialogView.confirmOkButton.setOnClickListener{
                    val passwordConfirm = mDialogView.confirmPin.text.toString()

                    if(!passwordConfirm.isNullOrEmpty()){
                        var loginCall = ApiRepository.canEmployeeLogin(State.currentEmployeeId, passwordConfirm)

                        loginCall.enqueue(object : Callback<EmployeeLoginData> {
                            override fun onFailure(call: Call<EmployeeLoginData>, t: Throwable) {
                                Toast.makeText(
                                    x.context,
                                    "Błąd! Nie połączono z bazą danych.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(call: Call<EmployeeLoginData>, response: Response<EmployeeLoginData>) {
                                if (response.code() == 200) {
                                    mAlertDialog.dismiss()
                                    taskViewModel.endTask(currentItem.id)

                                    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    val currentDate = sdf.format(Date())

                                    taskViewModel.addTaskDone(currentItem.employeeID, currentItem.task, currentItem.date, currentDate, "0")

                                    x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_self)

                                } else if (response.code() == 404) {
                                    Toast.makeText(x.context, "Niepoprawny pin.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                    }
                    else
                        Toast.makeText(x.context, "Nie wpisano hasła.", Toast.LENGTH_SHORT).show();
                }

                mDialogView.confirmCancelButton.setOnClickListener{
                    mAlertDialog.dismiss()
                }
            }
        }
    }
}