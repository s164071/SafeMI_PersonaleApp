package com.example.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
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

    private lateinit var auth: FirebaseAuth
    private lateinit var activity: MainActivity
    val TAG = "MainActivity"
    val fragment_nearby = Nearby()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        // Log.d(TAG, "Firebase instans "+ auth.app)//Initialising activity reference
        activity = getActivity() as MainActivity
        val view: View = inflater!!.inflate(R.layout.fragment_log_ind, container, false)
        val text: TextView = view.findViewById(R.id.glemt_password)

        //Log.d(TAG, "Fremviser login side")//Initialising activity reference

        text.setOnClickListener() {
            //Switch to fragment Glemt password
            val fragmentPassword = GlemtPassword()
            val manager = parentFragmentManager
            //   Log.d(TAG,"FragmentManager"+ parentFragmentManager)

            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, fragmentPassword)
                transaction.addToBackStack(null)
                transaction.commit()
                //  Log.d(TAG, "Omdirigering til glemt password fragment")
            }/*else{
                Log.d(TAG, "Der er ikke noget indhold i fragmentManager: Manager er lig "+ parentFragmentManager)
            }*/

        }

        val LogIndKnap: Button = view.findViewById(R.id.LogIndKnap)
        LogIndKnap.setOnClickListener() {
            //Log.d(TAG, "Der er  klikket på login")
            signInwithEmail()
        }

        return view
    }


    fun signInwithEmail() {
        if (email.text.toString().isEmpty()) {

            if (password.text.toString().isEmpty()){

                email.error = "Indtast en emailadresse"
                password.error = "Indtast et password"

                LogIndOplysninger.requestFocus()

                //Log.d(TAG, "Intet er indtastet - tom=" +email.text.toString().isEmpty()+ " og password tom= "  +password.text.toString().isEmpty())
                return

            }else {
                email.error = "Indtast en emailadresse"
                email.requestFocus()
                //Log.d(TAG, "emailfelt er tomt:" + email.text.toString().isEmpty())
                return
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            if (password.text.toString().isEmpty()){
                email.error = "Indtast en valid emailadresse"
                password.error = "Indtast et password"
                LogIndOplysninger.requestFocus()
                // Log.d(TAG, "Emailadresse er ikke valid: indhold= "+email.text.toString()+" og passwordfelt er tomt -tomt: " + password.text.toString().isEmpty())
                return

            }else {
                email.error = "Indtast en valid emailadresse"
                email.requestFocus()
                // Log.d(TAG, "Emailadresse er ikke valid: indhold= "+email.text.toString())
                return
            }} else if (password.text.toString().isEmpty()) {
            password.error = "Indtast et password"
            password.requestFocus()
            // Log.d(TAG, "passwordfelt er tomt:  "+ password.text.toString().isEmpty())
            return
        }else {

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        /* Log.d(
                             TAG,
                             "signInWithEmail:succes - bruger logget ind = " + user?.uid + "baseret på email: " + email.text.toString() + " og password: " + password.text.toString()
                         )*/
                        updateUI(user)
                    } else {

                        updateUI(null)
                    }
                }
        }
    }

    fun updateUI(currentUser:FirebaseUser?){

        val manager = parentFragmentManager
        //Log.d(TAG, "Nået til at tjekke internet forbindelse")
        if (checkInternetAccess(activity)==false) {
            //Log.d(TAG, "Ingen internet forbindelse fundet")
            Toast.makeText(
                activity,
                "Check at dit internet er slået til, og prøv igen",
                Toast.LENGTH_SHORT
            ).show()
            //Log.d(TAG, "Fejlmeddelse vist")
        } else if(manager!=null && currentUser!=null) {
            val transactionToNearby = manager.beginTransaction()
            transactionToNearby.replace(R.id.fragtop, fragment_nearby)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()
            //  Log.d(TAG, "Omdirigering til nearby")
        }else{
            Toast.makeText(
                activity,
                "Ingen brugere fundet med de indtastede loginoplysninger",
                Toast.LENGTH_SHORT
            ).show()
            // Log.d(TAG, "bruger vurderet til: "+ currentUser + " med følgende email: "+ email.text.toString() + " og password: " + password.text.toString())
        }

    }

    fun checkInternetAccess(activity: AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        // Log.d(TAG, "Netværk fundet" +  networkInfo)
        return  networkInfo!=null && networkInfo.isConnected
        //https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin

    }
}







