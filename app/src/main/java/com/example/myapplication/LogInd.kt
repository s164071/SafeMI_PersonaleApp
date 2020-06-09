package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_ind.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LogInd : Fragment() {
    // TODO: Rename and change types of parameters
    var PasswordOkay: Boolean = false
    private lateinit var auth: FirebaseAuth

    val TAG = "MainActivity"




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view: View = inflater!!.inflate(R.layout.fragment_log_ind, container, false)
        val text: TextView = view.findViewById(R.id.glemt_password)
        val manager = fragmentManager
        text.setOnClickListener() {
            //Switch to fragment Glemt password


            val fragmentPassword = GlemtPassword()

            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, fragmentPassword)
                transaction.addToBackStack(null)
                transaction.commit()
            }


        }

        val LogIndKnap: Button = view.findViewById(R.id.LogIndKnap)
        val fragment_nearby = nearby()
        LogIndKnap.setOnClickListener() {
            doLogInd()
            if (PasswordOkay == true) {
                if (manager != null) {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragtop, fragment_nearby)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }
        return view

    }

    fun doLogInd() {
        if (email.text.toString().isEmpty()) {
            email.error = "Indtast emailadresse"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Indtast en valid email"
            email.requestFocus()
            return

        }
        if (password.text.toString().isEmpty()) {
            password.error = "Indtast password"
            password.requestFocus()
            return
        }
        PasswordOkay = true
       // signInWithEmailAndPassword()
    }

/*    private fun signInWithEmailAndPassword() {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
            .addOnCompleteListener(this) {


                MainActivity.getActivity().getResult()

            if ((MainActivity).getActivity().getResult().isSuccessful) {
                Log.d(TAG, "signInWithEmailAndPassword:succes")
            }
                else {
                Log.d(TAG, "signInWithEmailAndPassword:failure")
                Toast.makeText(
                    (MainActivity).getActivity().getBaseContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/


}





