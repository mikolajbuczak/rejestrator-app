package com.example.rejestrator.view.model.entities

data class EmployeeListData(val employeeID : String?=null,
                            val pin : String?=null,
                            val name : String?=null,
                            val surname : String?=null,
                            val shift : String?=null){
    override fun toString(): String = "${employeeID} ${name} ${surname}"

    fun makeString() : String{
        return "${name} ${surname}"
    }

    fun makeString2() : String{
        return "${employeeID} ${pin} ${name} ${surname} ${shift}"
    }
}