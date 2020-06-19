package com.example.myapplication.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import androidx.fragment.app.activityViewModels

class Person : Fragment() {
    //val fragment_nearby = nearby()

    private lateinit var activity: MainActivity
    private val logtag = Person::class.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var mystorage: FirebaseStorage
    //private lateinit var auth: FirebaseAuth
    private val model: PersonViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //auth=FirebaseAuth.getInstance()

        val view: View = inflater.inflate(R.layout.fragment_person, container, false)
        view?.findViewById<ImageView>(R.id.backfromPTinformationer)?.setOnClickListener() {
            Log.d(logtag,"Der bliver nu trykket på tilbage fra personfragment")

        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        database = Firebase.database.reference

        val homeListAdaptor = PersonAdaptor()
        Log.d(logtag, "$homeListAdaptor")
        val list: RecyclerView = view.findViewById(R.id.home_RecyclerView)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        list.adapter = homeListAdaptor


        model.getHomeData().observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            homeListAdaptor.list.clear()
            Log.d(logtag, "elementer ${it.elements}")
            homeListAdaptor.list.addAll(it.elements)
            homeListAdaptor.notifyDataSetChanged()
            Log.d(logtag, " observere for ændringer")
            view.findViewById<ImageView>(R.id.home_ProfilePic).setImageBitmap(it.profilePicture)

        })
    }



}

