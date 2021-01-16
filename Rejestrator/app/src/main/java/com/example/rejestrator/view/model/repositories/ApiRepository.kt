package com.example.rejestrator.view.model.repositories

import com.example.rejestrator.view.model.api.ApiService
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.entities.Task
import retrofit2.*

class ApiRepository {

    companion object {

        fun canEmployeeLogin(id : String, pin : String): Call<EmployeeLoginData> {
            return ApiService.api.canEmployeeLogin(id, pin)
        }

        suspend fun getTasksForEmployee(id : String): List<Task> {
            return ApiService.api.getTasksForEmployee(id).awaitResponse().body()?: listOf()
        }

    }
}