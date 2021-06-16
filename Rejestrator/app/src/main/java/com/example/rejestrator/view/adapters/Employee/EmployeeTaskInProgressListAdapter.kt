package com.example.rejestrator.view.adapters.Employee

import android.R.attr
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
import com.example.rejestrator.view.model.entities.LoggedToday
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmCancelButton
import kotlinx.android.synthetic.main.confirm_with_password_dialog.view.confirmOkButton
import kotlinx.android.synthetic.main.confirm_with_pin_dialog.view.*
import kotlinx.android.synthetic.main.one_row_in_progress_list.view.*
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EmployeeTaskInProgressListAdapter(var taskList: LiveData<ArrayList<TaskInProgress>>, var taskViewModel: EmployeeTaskInProgressListViewModel) : RecyclerView.Adapter<EmployeeTaskInProgressListAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView1 = view.findViewById<TextView>(R.id.row_inProgressTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_row_in_progress_list, parent, false) as View

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return taskList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentItem = taskList.value?.get(position)

        holder.textView1.text = taskList.value?.get(position)?.task

        holder.view.row_endTaskButton.setOnClickListener { x ->
            if (currentItem != null) {

                val mDialogView = LayoutInflater.from(x.context).inflate(R.layout.confirm_with_pin_dialog, null)
                val mBuilder = AlertDialog.Builder(x.context, R.style.CustomAlertDialog).setView(mDialogView)
                val mAlertDialog = mBuilder.show()

                mDialogView.confirmOkButton.setOnClickListener {
                    val pinConfirm = mDialogView.confirmPin.text.toString()

                    if (!pinConfirm.isNullOrEmpty()) {
                        if(pinConfirm == State.currentEmployeePin){
                            mAlertDialog.dismiss()

                            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                            val currentDate = sdf.format(Date())

                            removeItemAt(position)
                            taskViewModel.endTask(currentItem.id)

                            if (State.currentEmployeeShift == "Dzienny")
                                calcDay(DateTime.parse(currentItem.date, DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")), DateTime.now(), State.currentEmployeeId, currentItem.task!!, currentItem.date!!, currentDate)
                            else if(State.currentEmployeeShift == "Nocny")
                                calcNight(DateTime.parse(currentItem.date, DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")), DateTime.now(), State.currentEmployeeId, currentItem.task!!, currentItem.date!!, currentDate)
                            x.findNavController().navigate(R.id.action_dashboardTaskInProgressListEmployee_self)

                            taskViewModel.getTasksInProgressForEmployee(State.currentEmployeeId)
                        }
                        else
                            Toast.makeText(x.context, "Niepoprawny pin.", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(x.context, "Nie wpisano pinu.", Toast.LENGTH_SHORT).show();
                }

                mDialogView.confirmCancelButton.setOnClickListener {
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

    private fun checkIfLoggedOnThisDay(id: String, date: String): Boolean {
        var loggedOnThisDay = false
        FirebaseDatabase.getInstance().getReference().child("logs").addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("task_for_employee","Error while getting task_for_employee")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(taskSnapshot in snapshot.children) {
                    val log = taskSnapshot.getValue(LoginData::class.java)
                    if(log!!.employeeID!! == id && log!!.date!!.startsWith(date)) {
                        loggedOnThisDay = true
                        break
                    }
                }
            }
        })
        return loggedOnThisDay
    }

    private fun calcDay(start: DateTime, stop: DateTime, employeeID: String, task : String, currentDateStart : String, currentDateEnd : String) {
        taskViewModel.addTaskDone(task, currentDateStart, currentDateEnd, "0")
    }

    private fun calcNight(start: DateTime, stop: DateTime, employeeID: String, task : String, currentDateStart : String, currentDateEnd : String) {
        taskViewModel.addTaskDone(task, currentDateStart, currentDateEnd, "0")
    }
}