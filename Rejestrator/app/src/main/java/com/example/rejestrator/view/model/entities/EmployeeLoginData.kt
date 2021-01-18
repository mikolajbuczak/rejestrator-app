package com.example.rejestrator.view.model.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeeLoginData {
    @Expose
    @SerializedName("employeeID")
    lateinit var employeeID : String

    @Expose
    @SerializedName("pin")
    lateinit var pin : String

    @Expose
    @SerializedName("name")
    lateinit var name : String

    @Expose
    @SerializedName("surname")
    lateinit var surname : String

    @Expose
    @SerializedName("shift")
    lateinit var shift : String
}