package com.example.rejestrator.view.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.rejestrator.R
import kotlinx.android.synthetic.main.fragment_login_employee.*

class LoginEmployee : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginButtonEmployee.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginEmployee_to_dashboardTaskListEmployee) }
        ChangeToAdminLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginEmployee_to_loginAdmin) }

        //add logic to login
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginEmployee {
            return LoginEmployee()
        }
    }
}