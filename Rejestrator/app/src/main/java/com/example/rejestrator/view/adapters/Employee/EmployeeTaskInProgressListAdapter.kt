package com.example.rejestrator.view.adapters.Employee

import android.app.AlertDialog
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
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmCancelButton
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmOkButton
import kotlinx.android.synthetic.main.confirm_with_pin_dialog.view.*
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import okhttp3.ResponseBody
import org.joda.time.DateTimeComparator
import org.joda.time.format.DateTimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EmployeeTaskInProgressListAdapter(var taskList: LiveData<ArrayList<TaskInProgress>>, var taskViewModel: EmployeeTaskInProgressListViewModel) : RecyclerView.Adapter<EmployeeTaskInProgressListAdapter.Holder>() {

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

                mDialogView.confirmOkButton.setOnClickListener{
                    val pinConfirm = mDialogView.confirmPin.text.toString()

                    if(!pinConfirm.isNullOrEmpty()){
                        var loginCall = ApiRepository.canEmployeeLogin(State.currentEmployeeId, pinConfirm)

                        loginCall.enqueue(object : Callback<EmployeeLoginData> {
                            override fun onFailure(call: Call<EmployeeLoginData>, t: Throwable) {
                                Toast.makeText(x.context, "Błąd! Nie połączono z bazą danych.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<EmployeeLoginData>, response: Response<EmployeeLoginData>) {
                                if (response.code() == 200) {
                                    mAlertDialog.dismiss()

                                    removeItemAt(position)
                                    taskViewModel.endTask(currentItem.id)

                                    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    val currentDate = sdf.format(Date())

                                    //time if shift
                                    taskViewModel.addTaskDone(currentItem.employeeID, currentItem.task, currentItem.date, currentDate, "0")

                                    x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_self)

                                    taskViewModel.getTasksInProgressForEmployee(State.currentEmployeeId)
                                } else if (response.code() == 404) {
                                    Toast.makeText(x.context, "Niepoprawny pin.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                    }
                    else
                        Toast.makeText(x.context, "Nie wpisano pinu.", Toast.LENGTH_SHORT).show();
                }

                mDialogView.confirmCancelButton.setOnClickListener{
                    mAlertDialog.dismiss()
                }
            }
        }
    }

    private fun removeItemAt(position: Int) {
        if (taskList.value?.size!! > 0) {
            taskList.value?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }
}