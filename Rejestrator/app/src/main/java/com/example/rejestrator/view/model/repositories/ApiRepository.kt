package com.example.rejestrator.view.model.repositories

import com.example.rejestrator.view.model.api.ApiService
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.entities.Task
import com.example.rejestrator.view.model.entities.TaskDone
import com.example.rejestrator.view.model.entities.TaskInProgress
import okhttp3.ResponseBody
import retrofit2.*

class ApiRepository {

    companion object {

        fun canEmployeeLogin(id : String, pin : String): Call<EmployeeLoginData> {
            return ApiService.api.canEmployeeLogin(id, pin)
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

        suspend fun endTask(id : Int): ResponseBody? {
            return ApiService.api.endTask(id).awaitResponse().body()
        }

        suspend fun addTaskInProgress(id : String, task : String, date : String): ResponseBody? {
            return ApiService.api.addTaskInProgress(id, task, date).awaitResponse().body()
        }

        suspend fun addTaskDone(id : String, task : String, startdate : String, enddate : String, time : String): ResponseBody? {
            return ApiService.api.addTaskDone(id, task, startdate, enddate, time).awaitResponse().body()
        }

    }
}