package com.example.rejestrator.view.model.api

import com.example.rejestrator.view.model.entities.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RejestratorApi {

    @FormUrlEncoded
    @POST("loginEmployee")
    fun canEmployeeLogin(@Field("employeeID") employeeId :String, @Field("pin") pin :String) : Call<EmployeeLoginData>

    @FormUrlEncoded
    @POST("employees")
    fun insertEmployee(@Field("employeeID") employeeId :String, @Field("pin") pin :String, @Field("name") name :String, @Field("surname") surname :String, @Field("shift") shift :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("administrators")
    fun insertAdmin(@Field("administratorID") administratorID :String, @Field("username") username :String, @Field("password") password :String, @Field("name") name :String, @Field("surname") surname :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("canAddEmployee")
    fun canAddEmployee(@Field("employeeID") employeeId :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("canAddAdmin")
    fun canAddAdmin(@Field("adminID") adminID :String, @Field("username") username :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("loginAdmin")
    fun canAdminLogin(@Field("username") employeeId :String, @Field("password") pin :String) : Call<AdminLoginData>

    @FormUrlEncoded
    @POST("canAddTask")
    fun canAddTask(@Field("employeeID") employeeID :String, @Field("task") task :String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("tasksAvailable")
    fun addTask(@Field("employeeID") employeeID :String, @Field("task") task :String) : Call<ResponseBody>

    @FormUrlEncoded
    @PUT("employees/{employeeID}")
    fun updateEmployee(@Path("employeeID") employeeIDToEdit : String, @Field("employeeID") employeeID :String, @Field("pin") pin :String, @Field("name") name :String, @Field("surname") surname :String, @Field("shift") shift :String): Call<ResponseBody>

    @GET("tasksAvailable/{employeeId}")
    fun getTasksForEmployee(@Path("employeeId") employeeId :String): Call<ArrayList<Task>>

    @GET("employees")
    fun getAllEmployees(): Call<ArrayList<EmployeeListData>>

    @GET("employees")
    fun getAllEmployeesList(): Call<ArrayList<EmployeeListData>>

    @GET("logsData")
    fun getAllLogs(): Call<ArrayList<LoginData>>

    @GET("tasksInProgress/{employeeId}")
    fun getTasksInProgressForEmployee(@Path("employeeId") employeeId :String): Call<ArrayList<TaskInProgress>>

    @GET("tasksDone/{employeeId}/{enddate}")
    fun getTasksDoneForEmployee(@Path("employeeId") employeeId :String, @Path("enddate") enddate :String): Call<ArrayList<TaskDone>>

    @GET("logs/{employeeId}")
    fun getAllLogsForEmployee(@Path("employeeId") employeeId :String): Call<List<LoginData>>

    @GET("tasksDone/{employeeId}")
    fun getAllTasksDoneForEmployee(@Path("employeeId") employeeId :String): Call<List<TaskDone>>

    @GET("tasksInProgress/{employeeId}")
    fun getAllTasksInProgressForEmployee(@Path("employeeId") employeeId :String): Call<List<TaskInProgress>>

    @GET("tasksAvailable/{employeeId}")
    fun getAllTasksForEmployee(@Path("employeeId") employeeId :String): Call<ArrayList<Task>>

    @GET("allLogsToday/{employeeId}/{date}")
    fun getAllLogsForEmployeeRaportToday(@Path("employeeId") employeeId :String, @Path("date") date :String): Call<ArrayList<LoginData>>

    @GET("tasksDone/{employeeId}/{date}")
    fun getAllTasksDoneForEmployeeRaportToday(@Path("employeeId") employeeId :String, @Path("date") date :String): Call<ArrayList<TaskDone>>

    @GET("logs/{employeeId}/{date}")
    fun checkIfLoggedOnThisDay (@Path("employeeId") employeeId :String, @Path("date") date :String): Call<LoggedToday>

    @DELETE("startTask/{taskId}")
    fun startTask(@Path("taskId") taskId :Int) : Call<ResponseBody>

    @DELETE("startTask/{taskId}")
    fun deleteTask(@Path("taskId") taskId :Int) : Call<ResponseBody>

    @DELETE("employees/{employeeID}")
    fun deleteEmployee(@Path("employeeID") employeeID :String) : Call<ResponseBody>

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