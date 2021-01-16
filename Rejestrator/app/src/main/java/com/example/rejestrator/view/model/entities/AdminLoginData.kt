package com.example.rejestrator.view.model.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AdminLoginData {
    @Expose
    @SerializedName("adminUsername")
    lateinit var adminUsername : String

    @Expose
    @SerializedName("adminPassword")
    lateinit var adminPassword : String

    @Expose
    @SerializedName("success")
    lateinit var success : String

    @Expose
    @SerializedName("message")
    lateinit var message : String
}