package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Person : Fragment() {


    val TAG = "MainActivity"
    lateinit var persid: String
    lateinit var navn: String
    lateinit var allergies: String
    lateinit var id: String
    lateinit var user: String
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_person, container, false)
        retrievePersonalInformation()
        return view
    }

    private fun retrievePersonalInformation() {



            })

        /*     FirebaseDatabase.getInstance().getReference().child("users").child("$user")

            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                }
                override fun onDataChange(dataSnapshotUser: DataSnapshot) {

                    var mapUser =dataSnapshotUser.value as Map<String,Any>
                    PersonName.text = mapUser["navn"].toString()

                }

            })

    }*/
        private fun retrieveBeaconInformation() : String{


        }

    }
}





