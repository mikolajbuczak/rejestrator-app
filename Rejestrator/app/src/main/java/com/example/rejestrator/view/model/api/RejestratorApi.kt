package com.example.rejestrator.view.model.api

import com.example.rejestrator.view.model.entities.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RejestratorApi {

    @FormUrlEncoded
    @POST("loginEmployee")
    fun canEmployeeLogin(@Field("employeeId") employeeId :String, @Field("pin") pin :String) : Call<EmployeeLoginData>

    @FormUrlEncoded
    @POST("employees")
    fun insertEmployee(@Field("employeeID") employeeId :String, @Field("pin") pin :String, @Field("name") name :String, @Field("surname") surname :String, @Field("shift") shift :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("canAddEmployee")
    fun canAddEmployee(@Field("employeeID") employeeId :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("loginAdmin")
    fun canAdminLogin(@Field("username") employeeId :String, @Field("password") pin :String) : Call<AdminLoginData>

    @GET("tasksAvailable/{employeeId}")
    fun getTasksForEmployee(@Path("employeeId") employeeId :String): Call<List<Task>>

    @GET("logsData")
    fun getAllLogs(): Call<ArrayList<LoginData>>

    @GET("tasksInProgress/{employeeId}")
    fun getTasksInProgressForEmployee(@Path("employeeId") employeeId :String): Call<List<TaskInProgress>>

    @GET("tasksDone/{employeeId}/{enddate}")
    fun getTasksDoneForEmployee(@Path("employeeId") employeeId :String, @Path("enddate") enddate :String): Call<List<TaskDone>>

    @GET("tasksDone/{employeeId}/{date}")
    fun checkIfLoggedOnThisDay (@Path("employeeId") employeeId :String, @Path("date") date :String): Call<LoggedToday>

    @DELETE("startTask/{taskId}")
    fun startTask(@Path("taskId") taskId :Int) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("tasksInProgress")
    fun addTaskInProgress(@Field("employeeID") employeeId :String, @Field("task") task :String, @Field("date") date :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("tasksDone")
    fun addTaskDone(@Field("employeeID") employeeId :String, @Field("task") task :String, @Field("startdate") startdate :String, @Field("enddate") enddate :String, @Field("time") time :String) : Call<ResponseBody>

    @DELETE("endTask/{taskId}")
    fun endTask(@Path("taskId") taskId :Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("logs")
    fun insertLog(@Field("employeeID") employeeId :String, @Field("date") date :String) : Call<ResponseBody>



}