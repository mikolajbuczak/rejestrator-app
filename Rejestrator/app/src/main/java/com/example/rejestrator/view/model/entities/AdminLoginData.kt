package com.example.rejestrator.view.model.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AdminLoginData {
    @Expose
    @SerializedName("administratorID")
    lateinit var administratorID : String

    @Expose
    @SerializedName("password")
    lateinit var password : String

    @Expose
    @SerializedName("name")
    lateinit var name : String

    @Expose
    @SerializedName("surname")
    lateinit var surname : String
}