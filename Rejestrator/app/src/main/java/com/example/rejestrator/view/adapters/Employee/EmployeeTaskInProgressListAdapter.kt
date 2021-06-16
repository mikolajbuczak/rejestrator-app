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
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Employee.EmployeeTaskInProgressListViewModel
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
                val mBuilder = AlertDialog.Builder(x.context, R.style.CustomAlertDialog)
                        .setView(mDialogView)
                val mAlertDialog = mBuilder.show()

                mDialogView.confirmOkButton.setOnClickListener {
                    val pinConfirm = mDialogView.confirmPin.text.toString()

                    if (!pinConfirm.isNullOrEmpty()) {
                        if(pinConfirm == State.currentEmployeePin){
                            mAlertDialog.dismiss()

                            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                            val currentDate = sdf.format(Date())

                            removeItemAt(position)
                            //taskViewModel.endTask(currentItem.id)

                            /*if (State.currentEmployeeShift == "Dzienny")
                                calcDay(DateTime.parse(currentItem.date, DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")), DateTime.now(), State.currentEmployeeId, currentItem.task, currentItem.date, currentDate)
                            else if(State.currentEmployeeShift == "Nocny")
                                calcNight(DateTime.parse(currentItem.date, DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")), DateTime.now(), State.currentEmployeeId, currentItem.task, currentItem.date, currentDate)
*/
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

    private fun calcDay(start: DateTime, stop: DateTime, employeeID: String, task : String, currentDateStart : String, currentDateEnd : String) {

        var valueToReturn1: String = ""
        var valueToReturn2: String = ""
        var valueToReturn3: String = ""

        var time : String = ""

        if (start == stop)
            time = "-"

        var startNow = start
        var stopNow = stop

        var total: Double
        var totalTime: Long
        var hours: Int


        if (start > stop) {
            var temp: DateTime = startNow
            startNow = stopNow
            stopNow = temp
        }

        var startFloor: DateTime = DateTime(startNow.year, startNow.monthOfYear, startNow.dayOfMonth, 8, 0, 0)
        var startCeil: DateTime = DateTime(startNow.year, startNow.monthOfYear, startNow.dayOfMonth, 18, 0, 0)

        var stopFloor: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 8, 0, 0)
        var stopCeil: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 18, 0, 0)

        Log.d("startFloor", startFloor.toString())
        Log.d("startCeil", startCeil.toString())
        Log.d("stopNow", stopNow.toString())
        Log.d("stopFloor", stopFloor.toString())
        Log.d("startCeil", startCeil.toString())
        if (startNow < startFloor)
            startNow = startFloor;
        if (startNow > startCeil)
            startNow = startCeil;

        if (stopNow < stopFloor)
            stopNow = stopFloor;
        if (stopNow > stopCeil)
            stopNow = stopCeil;

        var firstDayTime: Duration = Duration.millis(startCeil.minus(startNow.millis).millis)
        Log.d("firstDayTime", firstDayTime.standardMinutes.toString())
        var lastDayTime: Duration = Duration.millis(stopNow.minus(stopFloor.millis).millis)
        Log.d("lastDayTime", lastDayTime.standardMinutes.toString())
        var loggedIn: Boolean = true

        //START
        var checkIfLoggedOnThisDayCall = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${startNow.dayOfMonth}.${startNow.monthOfYear}.${startNow.year}")

        if (startNow.monthOfYear in 1..9 && startNow.dayOfMonth in 1..9)
            checkIfLoggedOnThisDayCall = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${startNow.dayOfMonth}.0${startNow.monthOfYear}.${startNow.year}")
        else if (startNow.dayOfMonth in 1..9)
            checkIfLoggedOnThisDayCall = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${startNow.dayOfMonth}.${startNow.monthOfYear}.${startNow.year}")
        else if (startNow.monthOfYear in 1..9)
            checkIfLoggedOnThisDayCall = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${startNow.dayOfMonth}.0${startNow.monthOfYear}.${startNow.year}")

        //STOP
        var checkIfLoggedOnThisDayCall2 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${stopNow.dayOfMonth}.${stopNow.monthOfYear}.${stopNow.year}")

        if (stopNow.monthOfYear in 1..9 && stopNow.dayOfMonth in 1..9)
            checkIfLoggedOnThisDayCall2 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${stopNow.dayOfMonth}.0${stopNow.monthOfYear}.${stopNow.year}")
        else if (stopNow.dayOfMonth in 1..9)
            checkIfLoggedOnThisDayCall2 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${stopNow.dayOfMonth}.${stopNow.monthOfYear}.${stopNow.year}")
        else if (stopNow.monthOfYear in 1..9)
            checkIfLoggedOnThisDayCall2 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${stopNow.dayOfMonth}.0${stopNow.monthOfYear}.${stopNow.year}")

        checkIfLoggedOnThisDayCall.enqueue(object : Callback<LoggedToday> {
            override fun onFailure(call: Call<LoggedToday>, t: Throwable) {
            }

            override fun onResponse(call: Call<LoggedToday>, response: Response<LoggedToday>) {
                if (response.body() == null) {
                    loggedIn = false;
                    firstDayTime = Duration.ZERO
                }

                checkIfLoggedOnThisDayCall2.enqueue((object : Callback<LoggedToday> {
                    override fun onFailure(call: Call<LoggedToday>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<LoggedToday>, response: Response<LoggedToday>) {
                        if (response.body() == null) {
                            lastDayTime = Duration.ZERO
                        }

                        if (startNow.toDateTime().dayOfMonth() == stopNow.toDateTime().dayOfMonth() && startNow.toDateTime().monthOfYear() == stopNow.toDateTime().monthOfYear() && startNow.toDateTime().year() == stopNow.toDateTime().year()) {
                            if (!loggedIn)
                                valueToReturn1 = "0min.";

                            val InMilis: Duration = Duration.millis(stopNow.minus(startNow.millis).millis)
                            totalTime = InMilis.standardMinutes

                            var hours = 0

                            while (totalTime > 60) {
                                hours++
                                totalTime -= 60
                            }

                            var totalTimeInDouble = totalTime.toDouble()

                            if (hours != 0 && Math.floor(totalTimeInDouble) != 0.0) {
                                valueToReturn2 = "${hours}h ${Math.floor(totalTimeInDouble).toInt()}min."
                            } else if (hours != 0 && Math.floor(totalTimeInDouble) == 0.0) {
                                valueToReturn2 = "${hours}h"
                            } else if (Math.floor(totalTimeInDouble) != 0.0) {
                                valueToReturn2 = "${Math.floor(totalTimeInDouble).toInt()}min."
                            } else {
                                valueToReturn2 = "1min."
                            }
                        }

                        var timeInBetween: Duration = Duration.ZERO
                        var hoursInAWholeDay: Duration = Duration.millis((startCeil.minus(startFloor.millis)).millis)

                        var itr = startFloor.plusDays(1)

                        while (itr < stopFloor) {
                            Log.d("WHILE", "WHILE")
                            var checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${itr.dayOfMonth}.${itr.monthOfYear}.${itr.year}")

                            if (itr.monthOfYear in 1..9 && itr.dayOfMonth in 1..9)
                                checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${itr.dayOfMonth}.0${itr.monthOfYear}.${itr.year}")
                            else if (itr.dayOfMonth in 1..9)
                                checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${itr.dayOfMonth}.${itr.monthOfYear}.${itr.year}")
                            else if (itr.monthOfYear in 1..9)
                                checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${itr.dayOfMonth}.0${itr.monthOfYear}.${itr.year}")

                            checkIfLoggedOnThisDayCall3.enqueue(object : Callback<LoggedToday> {
                                override fun onFailure(call: Call<LoggedToday>, t: Throwable) {
                                }

                                override fun onResponse(call: Call<LoggedToday>, response: Response<LoggedToday>) {
                                    response.body()?.toString()?.let { Log.d("WESZLO", it) }
                                    if (response.body() != null) {
                                        Log.d("response.body()", response.body()!!.date)
                                        timeInBetween += hoursInAWholeDay;
                                    }
                                }

                            })
                            itr = itr.plusDays(1)
                        }

                        val InMilis: Duration = Duration.millis(timeInBetween.plus(firstDayTime.plus(lastDayTime.millis).millis).millis)
                        totalTime = InMilis.standardMinutes

                        Log.d("firstDayTime", firstDayTime.millis.toString())
                        Log.d("lastDayTime", lastDayTime.millis.toString())
                        Log.d("timeInBetween", timeInBetween.millis.toString())
                        Log.d("totalTime", totalTime.toString())

                        hours = 0;

                        while (totalTime > 60) {
                            hours++
                            totalTime -= 60
                        }

                        var totalTimeInDouble = totalTime.toDouble()
                        Log.d("totalTimeInDouble", totalTimeInDouble.toString())

                        if (hours != 0 && Math.floor(totalTimeInDouble) != 0.0) {
                            valueToReturn3 = "${hours}h ${Math.floor(totalTimeInDouble).toInt()}min."
                        } else if (hours != 0 && Math.floor(totalTimeInDouble) == 0.0) {
                            valueToReturn3 = "${hours}h"
                        } else if (Math.floor(totalTimeInDouble) != 0.0) {
                            valueToReturn3 = "${Math.floor(totalTimeInDouble).toInt()}min."
                        } else {
                            valueToReturn3 = "1min."
                        }

                        if (!valueToReturn1.isNullOrEmpty())
                            time = valueToReturn1
                        else if (!valueToReturn2.isNullOrEmpty())
                            time = valueToReturn2
                        else if (!valueToReturn3.isNullOrEmpty())
                            time = valueToReturn3

                        taskViewModel.addTaskDone(employeeID, task, currentDateStart, currentDateEnd, time)

                    }

                }))

            }

        })

    }

    private fun calcNight(start: DateTime, stop: DateTime, employeeID: String, task : String, currentDateStart : String, currentDateEnd : String) {

        var valueToReturn1: String = ""
        var valueToReturn2: String = ""
        var valueToReturn3: String = ""

        var time : String = ""

        var alreadyAdded = false

        var startNow = start
        var stopNow = stop

        var total: Double
        var totalTime: Long
        var hours: Int


        if (start > stop) {
            var temp: DateTime = startNow
            startNow = stopNow
            stopNow = temp
        }


        var shiftStart: DateTime = DateTime(startNow.year, startNow.monthOfYear, startNow.dayOfMonth, 18, 0, 0)
        var shiftEnd: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 4, 0, 0)

        if (startNow.toDateTime().dayOfMonth() == stopNow.toDateTime().dayOfMonth() && startNow.toDateTime().monthOfYear() == stopNow.toDateTime().monthOfYear() && startNow.toDateTime().year() == stopNow.toDateTime().year() && startNow.toLocalTime() >= shiftEnd.toLocalTime() && stopNow.toLocalTime() <= shiftStart.toLocalTime())
        {
            time = "-";

            if (!valueToReturn1.isNullOrEmpty())
                time = valueToReturn1
            else if (!valueToReturn2.isNullOrEmpty())
                time = valueToReturn2
            else if (!valueToReturn3.isNullOrEmpty())
                time = valueToReturn3

            Log.d("lol", valueToReturn1)
            Log.d("lol", valueToReturn2)
            Log.d("lol", valueToReturn3)
            if(!alreadyAdded)
            {
                taskViewModel.addTaskDone(employeeID, task, currentDateStart, currentDateEnd, time)
            }
            alreadyAdded = true
        }

        if(startNow.toDateTime().dayOfMonth() == stopNow.toDateTime().dayOfMonth() && startNow.toDateTime().monthOfYear() == stopNow.toDateTime().monthOfYear() && startNow.toDateTime().year() == stopNow.toDateTime().year()){
            if(startNow.toLocalTime() > shiftStart.toLocalTime() || stopNow.toLocalTime() < shiftEnd.toLocalTime()){
                val InMilis: Duration = Duration.millis(stopNow.minus(startNow.millis).millis)
                totalTime = InMilis.standardMinutes

                var hours = 0

                while (totalTime > 60) {
                    hours++
                    totalTime -= 60
                }

                var totalTimeInDouble = totalTime.toDouble()

                if (hours != 0 && Math.floor(totalTimeInDouble) != 0.0) {
                    valueToReturn1 = "${hours}h ${Math.floor(totalTimeInDouble).toInt()}min."
                } else if (hours != 0 && Math.floor(totalTimeInDouble) == 0.0) {
                    valueToReturn1 = "${hours}h"
                } else if (Math.floor(totalTimeInDouble) != 0.0) {
                    valueToReturn1 = "${Math.floor(totalTimeInDouble).toInt()}min."
                } else {
                    valueToReturn1 = "1min."
                }

                if (!valueToReturn1.isNullOrEmpty())
                    time = valueToReturn1
                else if (!valueToReturn2.isNullOrEmpty())
                    time = valueToReturn2
                else if (!valueToReturn3.isNullOrEmpty())
                    time = valueToReturn3

                Log.d("lol", valueToReturn1)
                Log.d("lol", valueToReturn2)
                Log.d("lol", valueToReturn3)
                if(!alreadyAdded)
                {
                    taskViewModel.addTaskDone(employeeID, task, currentDateStart, currentDateEnd, time)
                }
                alreadyAdded = true


            }

            totalTime = 0
            if (startNow.toLocalTime() < shiftEnd.toLocalTime())
            {
                val InMilis: Duration = Duration.millis(shiftEnd.minus(startNow.millis).millis)
                totalTime += InMilis.standardMinutes
            }
            if (stopNow.toLocalTime() > shiftStart.toLocalTime())
            {
                val InMilis: Duration = Duration.millis(stopNow.minus(shiftStart.millis).millis)
                totalTime += InMilis.standardMinutes
            }

            var hours = 0

            while (totalTime > 60) {
                hours++
                totalTime -= 60
            }

            var totalTimeInDouble = totalTime.toDouble()

            if (hours != 0 && Math.floor(totalTimeInDouble) != 0.0) {
                valueToReturn2 = "${hours}h ${Math.floor(totalTimeInDouble).toInt()}min."
            } else if (hours != 0 && Math.floor(totalTimeInDouble) == 0.0) {
                valueToReturn2 = "${hours}h"
            } else if (Math.floor(totalTimeInDouble) != 0.0) {
                valueToReturn2 = "${Math.floor(totalTimeInDouble).toInt()}min."
            } else {
                valueToReturn2 = "1min."
            }

            if (!valueToReturn1.isNullOrEmpty())
                time = valueToReturn1
            else if (!valueToReturn2.isNullOrEmpty())
                time = valueToReturn2
            else if (!valueToReturn3.isNullOrEmpty())
                time = valueToReturn3

            Log.d("lol", valueToReturn1)
            Log.d("lol", valueToReturn2)
            Log.d("lol", valueToReturn3)
            if(!alreadyAdded)
            {
                taskViewModel.addTaskDone(employeeID, task, currentDateStart, currentDateEnd, time)
            }
            alreadyAdded = true

        }
        else {
            totalTime = 0

            if (startNow.toLocalTime() < shiftEnd.toLocalTime()) {
                val InMilis: Duration = Duration.millis(shiftEnd.minus(start.millis).millis)
                totalTime += InMilis.standardMinutes
            }
            if (startNow.toLocalTime() < shiftStart.toLocalTime()) {
                var newTimeSpan: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 23, 59, 59)

                val InMilis: Duration = Duration.millis(newTimeSpan.minus(shiftStart.millis).millis)
                totalTime += InMilis.standardMinutes
            } else {
                var newTimeSpan: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 23, 59, 59)
                val InMilis: Duration = Duration.millis(newTimeSpan.minus(startNow.millis).millis)

                totalTime += InMilis.standardMinutes
            }

            if (stopNow.toLocalTime() > shiftStart.toLocalTime()) {
                val InMilis: Duration = Duration.millis(stopNow.minus(shiftStart.millis).millis)
                totalTime += InMilis.standardMinutes
            }
            if (stopNow.toLocalTime() > shiftEnd.toLocalTime()) {
                val InMilis: Duration = Duration.millis(shiftEnd.millis)
                totalTime += InMilis.standardMinutes
            } else {
                val InMilis: Duration = Duration.millis(stopNow.millis)
                totalTime += InMilis.standardMinutes
            }

            val InMilis: Duration = Duration.millis(stopNow.minus(startNow.millis).millis)
            var numberOfDays = InMilis.standardDays

            if (stopNow.toLocalTime() > startNow.toLocalTime()) {
                numberOfDays--
            }
            if (numberOfDays > 0) {
                var itr = 1
                while (itr < numberOfDays) {

                    var newTimeSpan: DateTime = DateTime(stopNow.year, stopNow.monthOfYear, stopNow.dayOfMonth, 23, 59, 59)

                    val InMilis: Duration = Duration.millis(newTimeSpan.minus(shiftStart.millis).millis)
                    val InMilisShiftEnd: Duration = Duration.millis(shiftEnd.millis)

                    var hoursInFullDay = 10

                    var nextDay = startNow.plusDays(itr);

                    var checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${nextDay.dayOfMonth}.${nextDay.monthOfYear}.${nextDay.year}")

                    if (nextDay.monthOfYear in 1..9 && nextDay.dayOfMonth in 1..9)
                        checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${nextDay.dayOfMonth}.0${nextDay.monthOfYear}.${nextDay.year}")
                    else if (nextDay.dayOfMonth in 1..9)
                        checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "0${nextDay.dayOfMonth}.${nextDay.monthOfYear}.${nextDay.year}")
                    else if (nextDay.monthOfYear in 1..9)
                        checkIfLoggedOnThisDayCall3 = ApiRepository.checkIfLoggedOnThisDay(employeeID, "${nextDay.dayOfMonth}.0${nextDay.monthOfYear}.${nextDay.year}")

                    checkIfLoggedOnThisDayCall3.enqueue(object : Callback<LoggedToday> {
                        override fun onFailure(call: Call<LoggedToday>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<LoggedToday>, response: Response<LoggedToday>) {
                            if (response.body()?.date != null) {
                                totalTime += hoursInFullDay
                            }
                        }

                    })

                    itr++
                }

                var hours = 0

                while (totalTime > 60) {
                    hours++
                    totalTime -= 60
                }

                var totalTimeInDouble = totalTime.toDouble()

                if (hours != 0 && Math.floor(totalTimeInDouble) != 0.0) {
                    valueToReturn3 = "${hours}h ${Math.floor(totalTimeInDouble).toInt()}min."
                } else if (hours != 0 && Math.floor(totalTimeInDouble) == 0.0) {
                    valueToReturn3 = "${hours}h"
                } else if (Math.floor(totalTimeInDouble) != 0.0) {
                    valueToReturn3 = "${Math.floor(totalTimeInDouble).toInt()}min."
                } else {
                    valueToReturn3 = "1min."
                }


                if (!valueToReturn1.isNullOrEmpty())
                    time = valueToReturn1
                else if (!valueToReturn2.isNullOrEmpty())
                    time = valueToReturn2
                else if (!valueToReturn3.isNullOrEmpty())
                    time = valueToReturn3

                if(!alreadyAdded)
                {
                    taskViewModel.addTaskDone(employeeID, task, currentDateStart, currentDateEnd, time)
                }
            }
        }

    }
}