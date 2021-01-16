package com.example.rejestrator.view.model.api

import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.entities.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RejestratorApi {

    @FormUrlEncoded
    @POST("loginEmployee")
    fun canEmployeeLogin(@Field("employeeId") employeeId :String, @Field("employeePin") employeePin :String) : Call<EmployeeLoginData>

    @GET("tasksAvailable/{employeeId}")
    fun getTasksForEmployee(@Path("employeeId") employeeId :String): Call<List<Task>>


}