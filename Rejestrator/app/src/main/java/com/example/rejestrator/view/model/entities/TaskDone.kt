package com.example.rejestrator.view.model.entities

data class TaskDone(val id : String? = null,
                    val employeeID : String? = null,
                    val task: String? = null,
                    val startdate : String? = null,
                    val enddate : String? = null,
                    val time : String? = null)