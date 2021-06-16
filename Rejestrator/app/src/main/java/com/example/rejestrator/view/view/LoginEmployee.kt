package com.example.rejestrator.view.view

import android.annotation.SuppressLint
import android.os.Bundle
import java.util.UUID;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.viewmodel.LoginEmployeeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_login_employee.*
import java.text.SimpleDateFormat
import java.util.*

class LoginEmployee : Fragment() {

    lateinit var loginEmployeeViewModel: LoginEmployeeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_employee, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChangeToAdminLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginEmployee_to_loginAdmin) }

        inputID.addTextChangedListener{
            infoAboutLogin.visibility = View.VISIBLE
            infoAboutLogin1.visibility = View.GONE
        }

        inputPin.addTextChangedListener{
            infoAboutLogin.visibility = View.VISIBLE
            infoAboutLogin1.visibility = View.GONE
        }

        loginEmployeeViewModel = ViewModelProvider(requireActivity()).get(LoginEmployeeViewModel::class.java)

        LoginButtonEmployee.setOnClickListener{x ->
            var id = "${inputID.text}@rejestrator.com"
            var pin = inputPin.text.toString()

            if(id.isNullOrEmpty() || pin.isNullOrEmpty())
                Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            else{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(id, pin).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object: ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.invalid_login),
                                    Toast.LENGTH_LONG
                                ).show()
                                inputID.setText("")
                                inputPin.setText("")

                                infoAboutLogin1.visibility = View.VISIBLE
                                infoAboutLogin.visibility = View.GONE
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                var employeeLoginData = snapshot.getValue(EmployeeLoginData::class.java)
                                State.currentEmployeeId = employeeLoginData!!.employeeID!!
                                State.currentEmployeePin = employeeLoginData!!.pin!!
                                State.currentEmployeeShift = employeeLoginData!!.employeeID!!

                                val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                val currentDate = sdf.format(Date())

                                FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().currentUser!!.uid).child(UUID.randomUUID().toString()).setValue(currentDate)
                                x.findNavController().navigate(R.id.action_loginEmployee_to_dashboardTaskListEmployee)
                            }
                        })
                    }
                    else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.invalid_login),
                            Toast.LENGTH_LONG
                        ).show()
                        inputID.setText("")
                        inputPin.setText("")

                        infoAboutLogin1.visibility = View.VISIBLE
                        infoAboutLogin.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginEmployee {
            return LoginEmployee()
        }
    }
}