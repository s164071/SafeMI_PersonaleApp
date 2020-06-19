package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myapplication.person.Person
import kotlinx.android.synthetic.main.fragment_nearby.*
import kotlinx.android.synthetic.main.fragment_recent.*

class Recent : Fragment() {
    val TAG = "MainActivity"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_recent, container, false)
        val Informationer: LinearLayout = view.findViewById(R.id.Informationer)
        val borgere: TextView = view.findViewById(R.id.IngenBorgere)
        val home: ImageButton = view.findViewById(R.id.home_recent)


        home.setOnClickListener() {
            parentFragmentManager.popBackStack()
        }

        Informationer.setOnClickListener() {
            val personFragment: Person = Person()
            showNext(personFragment)
        }

        if (arguments != null) {
            borgere.visibility = View.GONE
            Informationer.visibility = View.VISIBLE
            val navn = requireArguments().getString("name")
            val cpr: String? = requireArguments().getString("cpr")
            if (navn != "" && cpr != "") {
                Log.d(TAG, "Jeg har kørt dette kode")
                var navnefeldt: TextView = view.findViewById(R.id.navnefeldt)
                var cprfeldt: TextView = view.findViewById(R.id.cprfeldt)


                Log.d(TAG, "Dette er " + navn)
                navnefeldt.text = navn
                cprfeldt.text = cpr


                var b = requireArguments().getByteArray("ProfilePic")
                if (b != null) {
                    var profilepic: Bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)

                    var billede: ImageView = view.findViewById(R.id.Profile)
                    billede.setImageBitmap(profilepic)
                } }else {
                    Log.d(TAG, "Jeg er her og viser blot felt")
                    borgere.visibility = View.VISIBLE
                    val Informationer: LinearLayout = view.findViewById(R.id.Informationer)
                    Informationer.visibility = View.GONE
                }

            }

            return view

        }

        fun showNext(nextFragment: Fragment) {

            val manager = fragmentManager
            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }


        }

}