package com.example.rejestrator.view.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.rejestrator.R
import kotlinx.android.synthetic.main.fragment_login_administrator.*

class LoginAdministrator : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_administrator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginButtonAdmin.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_dashboardAdmin) }
        ChangeToEmployeeLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_loginEmployee) }
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginAdministrator {
            return LoginAdministrator()
        }
    }
}