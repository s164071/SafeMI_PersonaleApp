package com.example.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_log_ind.*


class LogInd : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var activity: MainActivity
    val TAG = "MainActivity"
    val fragment_nearby = nearby()
    val fragmentPassword = GlemtPassword()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        activity = getActivity() as MainActivity

        val view: View = inflater!!.inflate(R.layout.fragment_log_ind, container, false)
        val text: TextView = view.findViewById(R.id.glemt_password)

        text.setOnClickListener() {

            //Changes fragment to GlemtPassword
            val manager = fragmentManager
            Log.d(TAG, "jeg er her")
            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, fragmentPassword)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }

        val LogIndKnap: Button = view.findViewById(R.id.LogIndKnap)
        //Listens for click on the LogIn button
        LogIndKnap.setOnClickListener() {
            signInwithEmailandPassword()
        }
        return view
    }


    fun signInwithEmailandPassword() {
        if (email.text.toString().isEmpty()) {
            email.error = "Indtast en emailadresse"
            email.requestFocus()

                    //Shows error
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Email skal indeholde @"
            email.requestFocus()
            return
        } else if (password.text.toString().isEmpty()) {
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
                }
                else {
                        // If sign in fails, display a message to the user.
                    Log.d(TAG,"signInWithEmail:failure")
                    updateUI(null)
                }
            }
    }

    fun updateUI(currentUser:FirebaseUser?){

        val manager = fragmentManager

        if (checkInternetAccess(activity)==false) {
            Toast.makeText(
                activity,
                "Check at dit internet er slået til, og prøv igen",
                Toast.LENGTH_SHORT
            ).show()
        } else if(manager!=null && currentUser!=null) {
            val transactionToNearby = manager.beginTransaction()
            transactionToNearby.replace(R.id.fragtop, fragment_nearby)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()
        }else{
            Toast.makeText(activity, "Ingen brugere fundet med de indtastede loginoplysninger", Toast.LENGTH_SHORT).show()
        }

    }

    fun checkInternetAccess(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
        //https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin
    }
}








