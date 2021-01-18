package com.example.rejestrator.view.view.Admin

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.api.ApiService
import com.example.rejestrator.view.model.entities.ChartData
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskInProgress
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.Admin.AdminRaportViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_employee_list_admin.employeeList
import kotlinx.android.synthetic.main.fragment_employee_list_admin.logsList
import kotlinx.android.synthetic.main.fragment_raport_list_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardRaportAdmin : Fragment() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }
    private var clicked = false

    var chartData : ArrayList<BarEntry> = arrayListOf()
    var labelName : ArrayList<String> = arrayListOf()
    var chartDataList : ArrayList<ChartData> = arrayListOf()

    var i = 0

    val colors: ArrayList<Int> = ArrayList()
    var LogsCount : Int = 0
    var TasksCount : Int = 0
    var TasksInProgressCount : Int = 0
    var TasksDoneCount : Int = 0

    lateinit var adminRaportViewModel: AdminRaportViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminRaportViewModel = ViewModelProvider(requireActivity()).get(AdminRaportViewModel::class.java)

        /*adminRaportViewModel.fillLogsLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTaskLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTasksInProgressLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTaskDoneLists(State.selectedEmployeeId)*/

        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        adminRaportViewModel.fillLists(State.selectedEmployeeId, currentDate)

        colors.add(Color.parseColor("#554252"))
        colors.add(Color.parseColor("#995D6C"))
        colors.add(Color.parseColor("#D8816F"))
        colors.add(Color.parseColor("#FCB667"))

        return inflater.inflate(R.layout.fragment_raport_list_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_to_dashboardLogsListAdmin) }
        employeeList.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardRaportAdmin_to_dashboardEmployeesAdmin) }

        employeeLabelNameChart.setText(State.selectedEmployeeName)
        employeeLabelSurnameChart.setText(State.selectedEmployeeSurname)

        /*adminRaportViewModel.fillLogsLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTaskLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTasksInProgressLists(State.selectedEmployeeId)
        adminRaportViewModel.fillTaskDoneLists(State.selectedEmployeeId)*/

        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        adminRaportViewModel.fillLists(State.selectedEmployeeId, currentDate)

        var description : Description = Description()
        description.text = "Raport"
        chart.description = description

        fillChart()

        refreshAll.setOnClickListener {
            chartDataList.clear()
            chartData.clear()
            chartDataList.add(ChartData("Logowania", adminRaportViewModel.employeeLogs.value?.count()) )
            chartDataList.add(ChartData("Przydzielone", adminRaportViewModel.employeeTasks.value?.count()) )
            chartDataList.add(ChartData("Zadania w toku", adminRaportViewModel.employeeTasksInProgress.value?.count()) )
            chartDataList.add(ChartData("Wykonane", adminRaportViewModel.employeeTasksDone.value?.count()) )

            chartDataList.forEach(){
                var label = it.label
                var value = it.count
                if (value != null) {
                    chartData.add(BarEntry(i.toFloat(), value.toFloat()))
                }
                labelName.add(label)
                i++
            }

            fillChart()
        }

        calendarAll.setOnClickListener {
            chartDataList.clear()
            chartData.clear()
            chartDataList.add(ChartData("Logowania", adminRaportViewModel.employeeLogsToday.value?.count()) )
            chartDataList.add(ChartData("Przydzielone", adminRaportViewModel.employeeTasks.value?.count()) )
            chartDataList.add(ChartData("Zadania w toku", adminRaportViewModel.employeeTasksInProgress.value?.count()) )
            chartDataList.add(ChartData("Wykonane", adminRaportViewModel.employeeTasksDoneToday.value?.count()) )

            chartDataList.forEach(){
                var label = it.label
                var value = it.count
                if (value != null) {
                    chartData.add(BarEntry(i.toFloat(), value.toFloat()))
                }
                labelName.add(label)
                i++
            }

            fillChart()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardRaportAdmin {
            return DashboardRaportAdmin()
        }
    }

    private fun fillChart(){
        var barDataSet : BarDataSet = BarDataSet(chartData, "Ilość")
        barDataSet.colors = colors
        barDataSet.axisDependency = YAxis.AxisDependency.LEFT
        chart.axisLeft.granularity = 1f
        chart.axisLeft.isGranularityEnabled = true
        chart.axisRight.granularity = 1f
        chart.axisRight.isGranularityEnabled = true

        var barData : BarData = BarData(barDataSet)

        chart.data = barData

        var xAxis : XAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labelName)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelName.count()
        xAxis.labelRotationAngle = 270f
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}