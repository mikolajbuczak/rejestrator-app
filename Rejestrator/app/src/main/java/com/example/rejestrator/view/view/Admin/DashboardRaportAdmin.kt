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
import kotlinx.android.synthetic.main.fragment_employee_list_admin.*
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

    var chartData : ArrayList<BarEntry> = arrayListOf()
    var labelName : ArrayList<String> = arrayListOf()
    var chartDataList : ArrayList<ChartData> = arrayListOf()

    var i = 0

    val colors: ArrayList<Int> = ArrayList()

    lateinit var adminRaportViewModel: AdminRaportViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminRaportViewModel = ViewModelProvider(requireActivity()).get(AdminRaportViewModel::class.java)

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

        if(State.selectedEmployeeName.count() < State.selectedEmployeeSurname.count())
        {
            employeeLabelNameChart.setText(State.selectedEmployeeName)
            employeeLabelSurnameChart.setText(State.selectedEmployeeSurname)
        }
        else{
            employeeLabelNameChart.setText(State.selectedEmployeeSurname)
            employeeLabelSurnameChart.setText(State.selectedEmployeeName)
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        adminRaportViewModel.fillLists(State.selectedEmployeeId, currentDate)

        var description : Description = Description()
        description.text = getString(R.string.raport)
        chart.description = description

        fillChart()

        refreshAll.setOnClickListener {
            chartDataList.clear()
            chartData.clear()
            chartDataList.add(ChartData(getString(R.string.chart_label1), adminRaportViewModel.employeeLogs.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label2), adminRaportViewModel.employeeTasks.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label3), adminRaportViewModel.employeeTasksInProgress.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label4), adminRaportViewModel.employeeTasksDone.value?.count()) )

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
            chartDataList.add(ChartData(getString(R.string.chart_label1), adminRaportViewModel.employeeLogsToday.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label2), adminRaportViewModel.employeeTasks.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label3), adminRaportViewModel.employeeTasksInProgress.value?.count()) )
            chartDataList.add(ChartData(getString(R.string.chart_label4), adminRaportViewModel.employeeTasksDoneToday.value?.count()) )

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
        var barDataSet : BarDataSet = BarDataSet(chartData, getString(R.string.chart_label5))
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