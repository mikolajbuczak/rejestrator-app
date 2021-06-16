package com.example.rejestrator.view.model.repositories

import android.util.Log
import com.example.rejestrator.view.model.api.ApiService
import com.example.rejestrator.view.model.entities.*
import okhttp3.ResponseBody
import retrofit2.*

class ApiRepository {
    companion object {
        fun updateEmployee(idToEdit : String, id : String, pin : String, name : String, surname : String, shift : String) : Call<ResponseBody>{
            return ApiService.api.updateEmployee(idToEdit, id, pin, name, surname, shift)
        }

        suspend fun getAllLogs(): ArrayList<LoginData> {
            return ApiService.api.getAllLogs().awaitResponse().body()?: arrayListOf()
        }

        suspend fun getTasksForEmployee(id : String): ArrayList<Task> {
            return ApiService.api.getTasksForEmployee(id).awaitResponse().body()?: arrayListOf()
        }

        suspend fun getTasksInProgressForEmployee(id : String): ArrayList<TaskInProgress> {
            return ApiService.api.getTasksInProgressForEmployee(id).awaitResponse().body()?: arrayListOf()
        }

        suspend fun getTasksDoneForEmployee(id : String, enddate: String): ArrayList<TaskDone> {
            return ApiService.api.getTasksDoneForEmployee(id, enddate).awaitResponse().body()?: arrayListOf()
        }

        suspend fun getAllLogsForEmployee(id : String): List<LoginData> {
            return ApiService.api.getAllLogsForEmployee(id).awaitResponse().body()?: listOf()
        }

        suspend fun getAllTasksDoneForEmployee(id : String): List<TaskDone> {
            return ApiService.api.getAllTasksDoneForEmployee(id).awaitResponse().body()?: listOf()
        }

        suspend fun getAllTasksInProgressForEmployee(id : String): List<TaskInProgress> {
            return ApiService.api.getAllTasksInProgressForEmployee(id).awaitResponse().body()?: listOf()
        }

        suspend fun getAllTasksForEmployee(id : String): ArrayList<Task> {
            return ApiService.api.getAllTasksForEmployee(id).awaitResponse().body()?: arrayListOf()
        }

        suspend fun getAllLogsForEmployeeRaportToday(id : String, date : String): ArrayList<LoginData> {
            return ApiService.api.getAllLogsForEmployeeRaportToday(id, date).awaitResponse().body()?: arrayListOf()
        }

        suspend fun getAllTasksDoneForEmployeeRaportToday(id : String, date : String): ArrayList<TaskDone> {
            return ApiService.api.getAllTasksDoneForEmployeeRaportToday(id, date).awaitResponse().body()?: arrayListOf()
        }

        fun getAllEmployees(): Call<ArrayList<EmployeeListData>> {
            return ApiService.api.getAllEmployees()
        }

        suspend fun getAllEmployeesList(): ArrayList<EmployeeListData> {
            return ApiService.api.getAllEmployeesList().awaitResponse().body()?: arrayListOf()
        }

        suspend fun startTask(id : Int): ResponseBody? {
            return ApiService.api.startTask(id).awaitResponse().body()
        }

        suspend fun deleteTask(id : Int): ResponseBody? {
            return ApiService.api.deleteTask(id).awaitResponse().body()
        }

        fun addTask(id : String, task : String): Call<ResponseBody> {
            return ApiService.api.addTask(id, task)
        }

        suspend fun deleteEmployee(id : String): ResponseBody? {
            return ApiService.api.deleteEmployee(id).awaitResponse().body()
        }

        fun canAddTask(id : String, task : String): Call<ResponseBody> {
            return ApiService.api.canAddTask(id, task)
        }

        suspend fun insertEmployee(id : String, pin : String, name : String, surname : String, shift : String): ResponseBody? {
            return ApiService.api.insertEmployee(id, pin, name, surname, shift).awaitResponse().body()
        }

        suspend fun insertAdmin(id : String, username: String, password: String, name : String, surname : String): ResponseBody? {
            return ApiService.api.insertAdmin(id, username, password, name, surname).awaitResponse().body()
        }

        fun canAddEmployee(id : String): Call<ResponseBody> {
            return ApiService.api.canAddEmployee(id)
        }

        fun canAddAdmin(id : String, username: String): Call<ResponseBody> {
            return ApiService.api.canAddAdmin(id, username)
        }

        suspend fun endTask(id : Int): ResponseBody? {
            return ApiService.api.endTask(id).awaitResponse().body()
        }

        suspend fun insertLog(id : String, date : String): ResponseBody? {
            return ApiService.api.insertLog(id, date).awaitResponse().body()
        }

        suspend fun addTaskInProgress(id : String, task : String, date : String): ResponseBody? {
            return ApiService.api.addTaskInProgress(id, task, date).awaitResponse().body()
        }

        suspend fun addTaskDone(id : String, task : String, startdate : String, enddate : String, time : String): ResponseBody? {
            return ApiService.api.addTaskDone(id, task, startdate, enddate, time).awaitResponse().body()
        }

        fun checkIfLoggedOnThisDay(id : String, date : String): Call<LoggedToday> {
            return ApiService.api.checkIfLoggedOnThisDay(id, date)
        }

    }
}