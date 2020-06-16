package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_glemt_password.*

class GlemtPassword : Fragment() {
    private lateinit var activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater!!.inflate(R.layout.fragment_glemt_password, container, false)

        val sendEmail: Button = view.findViewById(R.id.ResetPasswrodButton)
        sendEmail.setOnClickListener() {
            resetPasswordByEmail()
        }
        //Switch to fragment logind
        val tilbageknap: ImageButton = view.findViewById(R.id.TilbageKnap_GlemtPassword)
        tilbageknap.setOnClickListener() {
            val manager = fragmentManager
            manager?.popBackStack()

        }
        return view
    }


    fun resetPasswordByEmail(){

        if (Email_GlemtPassword.text.toString().isEmpty()) {
            Email_GlemtPassword.error = "Indtast email"
            Email_GlemtPassword.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email_GlemtPassword.text.toString()).matches()) {
            Email_GlemtPassword.error = "Email skal indeholde @"
            Email_GlemtPassword.requestFocus()
            return
        }
        val auth=FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(Email_GlemtPassword.text.toString())
            .addOnCompleteListener(){task ->
                if (task.isSuccessful()){
                    Toast.makeText(
                        activity,
                        "Emailen er sendt",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        activity,
                        "Emailen er ikke tilknyttet nogen bruger",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }


}