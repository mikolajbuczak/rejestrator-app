package com.example.rejestrator.view.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.AdminLoginData
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.LoginAdminViewModel
import com.example.rejestrator.view.viewmodel.LoginEmployeeViewModel
import kotlinx.android.synthetic.main.fragment_login_administrator.*
import kotlinx.android.synthetic.main.fragment_login_employee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginAdministrator : Fragment() {

    lateinit var loginAdministratorViewModel: LoginAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_administrator, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginButtonAdmin.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_dashboardLogsListAdmin) }
        ChangeToEmployeeLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_loginEmployee) }

        inputUsername.addTextChangedListener{
            infoAboutLogin2.visibility = View.GONE
        }

        inputPassword.addTextChangedListener{
            infoAboutLogin2.visibility = View.GONE
        }

        loginAdministratorViewModel = ViewModelProvider(requireActivity()).get(LoginAdminViewModel::class.java)

        LoginButtonAdmin.setOnClickListener { x ->
            var username = "${inputUsername.text.toString()}@rejestrator.com"
            var password = inputPassword.text.toString()

            if (username.isNullOrEmpty() || password.isNullOrEmpty())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_data),
                    Toast.LENGTH_SHORT
                ).show()
            else {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {

                            FirebaseDatabase.getInstance().getReference().child("admins").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object:
                                ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.invalid_login),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    inputUsername.setText("")
                                    inputPassword.setText("")

                                    infoAboutLogin2.visibility = View.VISIBLE
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var adminLoginData = snapshot.getValue(AdminLoginData::class.java)
                                    State.currentAdminUsername = adminLoginData!!.administratorID!!
                                    State.currentAdminPassword = adminLoginData!!.password!!

                                    x.findNavController().navigate(R.id.action_loginAdmin_to_dashboardLogsListAdmin)
                                }
                            })

                        }
                        else {
                            inputUsername.setText("")
                            inputPassword.setText("")

                            infoAboutLogin2.visibility = View.VISIBLE
                        }
                    }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginAdministrator {
            return LoginAdministrator()
        }
    }
}