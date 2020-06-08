package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LogIndKnap.setOnClickListener {
            val intent = Intent(this, BeaconOversigt::class.java)
            this.finish()
            startActivity(intent)
            overridePendingTransition(0, 0);
        }
    }
}

