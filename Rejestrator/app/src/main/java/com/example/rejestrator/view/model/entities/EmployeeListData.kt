package com.example.rejestrator.view.model.entities

class EmployeeListData(val employeeID : String, val name : String, val surname : String){
    override fun toString(): String = "${employeeID} ${name} ${surname}"
}