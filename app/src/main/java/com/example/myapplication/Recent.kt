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
            Log.d(TAG,"Der bliver trykket på home knappen fra recentsiden")
            parentFragmentManager.popBackStack()
        }

        Informationer.setOnClickListener() {
            if (requireArguments().getString("name")!=null) {

                val personFragment: Person = Person()
                showNext(personFragment)
            }

        }

            val navn = requireArguments().getString("name")
            val cpr: String? = requireArguments().getString("cpr")
            val b = requireArguments().getByteArray("ProfilePic")
            if (navn != null && cpr != null && b!=null ) {
                borgere.visibility = View.GONE
                Informationer.visibility = View.VISIBLE
                Log.d(TAG, "Der overføres data fra person siden")
                var navnefeldt: TextView = view.findViewById(R.id.navnefeldt)
                var cprfeldt: TextView = view.findViewById(R.id.cprfeldt)


                Log.d(TAG, "Dette er " + navn+ "På recent siden")
                navnefeldt.text = navn
                cprfeldt.text = cpr


                    var profilepic: Bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                    var billede: ImageView = view.findViewById(R.id.Profile)
                    billede.setImageBitmap(profilepic)

            } else {
                Log.d(TAG, "Jeg er her og viser blot felt")
                borgere.visibility = View.VISIBLE
                val Informationer: LinearLayout = view.findViewById(R.id.Informationer)
                Informationer.visibility = View.GONE

            }


        return view

    }

    fun showNext(nextFragment: Fragment) {

        val manager = parentFragmentManager
        if (manager != null) {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragtop, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


    }

}