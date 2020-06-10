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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GlemtPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class GlemtPassword : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater!!.inflate(R.layout.fragment_glemt_password, container, false)

        val sendEmail: Button = view.findViewById(R.id.ResetPasswrodButton)
        sendEmail.setOnClickListener() {
            resetPasswordByEmail()

            //Switch to fragment Glemt password

            val tilbageknap: ImageButton = view.findViewById(R.id.TilbageKnap_GlemtPassword)
            tilbageknap.setOnClickListener() {
                val manager = fragmentManager
                if (manager != null) {
                    manager.popBackStack()
                }
            }



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
            Email_GlemtPassword.error = "Den indtastede email er ikke en email"
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