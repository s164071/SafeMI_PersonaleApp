package com.example.myapplication

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_log_ind.*


/**
 * A simple [Fragment] subclass.
 * Use the [GlemtPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class GlemtPassword : Fragment() {
    val TAG= "MainActivity"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        val view: View = inflater!!.inflate(R.layout.fragment_glemt_password, container, false)
        //Log.d(TAG, "Kommet hertil fra login")
        val sendEmail: Button = view.findViewById(R.id.ResetPasswrodButton)


        sendEmail.setOnClickListener() {
            resetPasswordByEmail()
            //Log.d(TAG, "Klik registeret og metode kaldt")
        }
        val tilbageknap: ImageButton = view.findViewById(R.id.TilbageKnap_GlemtPassword)
        tilbageknap.setOnClickListener() {
            val manager = parentFragmentManager
            if (manager != null) {
                manager.popBackStack()
                Log.d(TAG, "Har popstacket, viser login side igen")
            }

        }


        return view
    }


    fun resetPasswordByEmail(){

        if (Email_GlemtPassword.text.toString().isEmpty()) {
            Email_GlemtPassword.error = "Indtast en emailadresse"
            Email_GlemtPassword.requestFocus()
            //Log.d(TAG,"Emailadresse er tom="+Email_GlemtPassword.text.toString().isEmpty())
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email_GlemtPassword.text.toString()).matches()) {
            Email_GlemtPassword.error = "Indtast en valid emailadresse"
            Email_GlemtPassword.requestFocus()
            //Log.d(TAG, "Emailadresse er ikke valid: indhold= " +Email_GlemtPassword.text.toString())
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

                    //Log.d(TAG, "Det har været muligt at sende emailen")

                }
                else{
                    Toast.makeText(
                        activity,
                        "Emailen er ikke tilknyttet nogen bruger",
                        Toast.LENGTH_SHORT
                    ).show()
                    //Log.d(TAG, "Email blev ikke sendt -  indtastet email:"+ Email_GlemtPassword.text.toString())
                }

            }
    }


}