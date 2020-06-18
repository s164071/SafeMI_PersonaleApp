package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_recent.*

class Recent : Fragment() {
    val TAG = "MainActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_recent, container, false)
        if (arguments != null) {
            var navn  = arguments!!.getString("name")
            var feldt : TextView = view.findViewById(R.id.hejfelt)
            Log.d(TAG, "Dette er "+ navn)
            feldt.setText(navn)

            var b= arguments!!.getByteArray("ProfilePic")
            if (b!=null) {
                var profilepic: Bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)

                var billede : ImageView = view.findViewById(R.id.Profile)
                billede.setImageBitmap(profilepic)
            }
        }
        return view

    }
}