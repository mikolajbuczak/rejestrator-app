package com.example.rejestrator.view.model.entities

class EmployeeListData(val employeeID : String, val pin : String, val name : String, val surname : String, val shift : String){
    override fun toString(): String = "${employeeID} ${name} ${surname}"

    fun makeString() : String{
        return "${name} ${surname}"
    }

    fun makeString2() : String{
        return "${employeeID} ${pin} ${name} ${surname} ${shift}"
    }
}