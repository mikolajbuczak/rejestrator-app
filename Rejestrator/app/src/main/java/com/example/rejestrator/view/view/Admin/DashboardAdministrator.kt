package com.example.rejestrator.view.view.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.rejestrator.R
import kotlinx.android.synthetic.main.fragment_dashboard_administrator.*

class DashboardAdministrator : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard_administrator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogoutButtonAdmin.setOnClickListener { x -> x.findNavController().navigate(R.id.action_dashboardAdmin_to_loginEmployee) }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardAdministrator {
            return DashboardAdministrator()
        }
    }
}