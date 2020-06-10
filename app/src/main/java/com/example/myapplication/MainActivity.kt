package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
  //  lateinit var fraglogin: LogInd
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

       val fraglogin = LogInd()
        val manager = supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.add(R.id.fragtop, fraglogin)
        fragmentTransaction.commit()


}



}





