package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_log_ind.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LogInd : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var activity: MainActivity
    val TAG = "MainActivity"
    val fragment_nearby = nearby()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        activity = getActivity() as MainActivity

        val view: View = inflater!!.inflate(R.layout.fragment_log_ind, container, false)
        val text: TextView = view.findViewById(R.id.glemt_password)

        text.setOnClickListener() {
            //Switch to fragment Glemt password
            val fragmentPassword = GlemtPassword()
            val manager = fragmentManager
            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, fragmentPassword)
                transaction.addToBackStack(null)
                transaction.commit()
            }


        }

        val LogIndKnap: Button = view.findViewById(R.id.LogIndKnap)
        LogIndKnap.setOnClickListener() {
            signInwithEmail()
        }

        return view
    }


    fun signInwithEmail() {
        if (email.text.toString().isEmpty()) {
            email.error = "Indtast en emailadresse"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Indtast en valid emailadresse"
            email.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Indtast et password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d(TAG, "signInWithEmail:succes")
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG,"signInWithEmail:failure")
                    Toast.makeText(
                        activity,
                        "Ingen bruger fundet med de indtastede loginoplysninger",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    fun updateUI(currentUser:FirebaseUser?){

        val manager = fragmentManager
        if(manager!=null && currentUser!=null) {

            val transactionToNearby = manager.beginTransaction()
            transactionToNearby.replace(R.id.fragtop, fragment_nearby)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()

        } else{
            Toast.makeText(activity,"Fejl ved login", Toast.LENGTH_SHORT
            ).show()       }
    }
}








