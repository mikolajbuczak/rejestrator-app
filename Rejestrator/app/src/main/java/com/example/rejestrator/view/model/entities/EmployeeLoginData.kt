package com.example.rejestrator.view.model.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeeLoginData {
    @Expose
    @SerializedName("employeeID")
    lateinit var employeeID : String

    @Expose
    @SerializedName("employeePin")
    lateinit var employeePin : String

    @Expose
    @SerializedName("success")
    lateinit var success : String

    @Expose
    @SerializedName("message")
    lateinit var message : String
}