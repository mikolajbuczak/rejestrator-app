package com.example.rejestrator.view.model.repositories

import android.util.Log
import com.example.rejestrator.view.model.api.ApiService
import com.example.rejestrator.view.model.entities.*
import okhttp3.ResponseBody
import retrofit2.*

class ApiRepository {

    companion object {

        fun canEmployeeLogin(id : String, pin : String): Call<EmployeeLoginData> {
            return ApiService.api.canEmployeeLogin(id, pin)
        }

        fun canAdminLogin(username : String, password : String): Call<AdminLoginData> {
            return ApiService.api.canAdminLogin(username, password)
        }

        suspend fun getAllLogs(): ArrayList<LoginData> {
            return ApiService.api.getAllLogs().awaitResponse().body()?: arrayListOf()
        }

        suspend fun getTasksForEmployee(id : String): List<Task> {
            return ApiService.api.getTasksForEmployee(id).awaitResponse().body()?: listOf()
        }

        suspend fun getTasksInProgressForEmployee(id : String): List<TaskInProgress> {
            return ApiService.api.getTasksInProgressForEmployee(id).awaitResponse().body()?: listOf()
        }

        suspend fun getTasksDoneForEmployee(id : String, enddate: String): List<TaskDone> {
            return ApiService.api.getTasksDoneForEmployee(id, enddate).awaitResponse().body()?: listOf()
        }

        suspend fun startTask(id : Int): ResponseBody? {
            return ApiService.api.startTask(id).awaitResponse().body()
        }

        suspend fun insertEmployee(id : String, pin : String, name : String, surname : String, shift : String): ResponseBody? {
            return ApiService.api.insertEmployee(id, pin, name, surname, shift).awaitResponse().body()
        }

        fun canAddEmployee(id : String): Call<ResponseBody> {
            return ApiService.api.canAddEmployee(id)
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