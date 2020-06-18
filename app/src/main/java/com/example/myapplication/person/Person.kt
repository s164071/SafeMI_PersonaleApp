package com.example.myapplication.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estimote.coresdk.cloud.model.User
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.nearby
import kotlinx.android.synthetic.main.fragment_person.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Person : Fragment() {
    //val fragment_nearby = nearby()

    private lateinit var activity: MainActivity
    private val logtag = Person::class.simpleName
    private lateinit var database:DatabaseReference
    private lateinit var mystorage:FirebaseStorage
    private lateinit var auth:FirebaseAuth
    //private val model:PersonViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_person, container, false)

        //activity = getActivity() as MainActivity

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view?.findViewById<ImageView>(R.id.back_from_patientinfo)?.setOnClickListener() {
            //updateUI()
        //}
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        //hideSoftKeyBoard(view)


        val homeListAdaptor = PersonAdaptor()
        Log.d(logtag, "$homeListAdaptor")
        val list: RecyclerView = view.findViewById(R.id.home_RecyclerView)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        list.adapter = homeListAdaptor
    }


    private fun updateUI(){
//Denne skal føre brugeren tilbage til nearby fragment, mangler noget...
        Log.d(logtag, "Hej nu kaldes updateUI")
        val manager = fragmentManager
        if(manager!=null) {

            val transactionToNearby = manager.beginTransaction()
            //transactionToNearby.replace(R.id.fragtop, fragment_nearby)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()

        } else{
            Toast.makeText(activity,"Fejl kunne ikke overføre bruger", Toast.LENGTH_SHORT
            ).show()       }
    }



    /*download(userId) {
        val user = User(name = dataSnapshot.child("navn").getValue(true).toString(), cpr = dataSnapshot.child("persId").getValue(true).toString(), medicines = dataSnapshot.child("Medicin").value.toString()
            .getFirebaseList(), donor = dataSnapshot.child("Donor").getValue(true).toString(), allergies = dataSnapshot.child("Allergier").value.toString()
            .getFirebaseList(), others = dataSnapshot.child("Andet").value.toString().getFirebaseList(), image = it, emergencies = getEmengencyFirebaseList(
            dataSnapshot.child("Kontaktperson").child("navn").value.toString(),
            dataSnapshot.child("Kontaktperson").child("nummer").value.toString()
        ))*/


        /*model.getHomeData().observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            homeListAdaptor.list.clear()
            Log.d(logtag,"elements: ${it.elements}")
            homeListAdaptor.list.addAll(it.elements)
            homeListAdaptor.notifyDataSetChanged()
            Log.d(logtag, "in observe")

            view.findViewById<ImageView>(R.id.home_ProfilePic).setImageBitmap(it.profilePicture)
        })*/

}

