package com.example.myapplication.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    val fragment_nearby = nearby()

    private lateinit var activity: MainActivity
    private val logtag = Person::class.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var mystorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private val model: PersonViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_person, container, false)
        //Forsøger at hente beacon id og user...
        //setResultListener("requestKey") { key, bundle ->
            // string user skal overføres fra nearby til denne
            //val current = bundle.getString("bundleKey")
            // Do something with the result...
        //}
        model.homeUpdateRepo(current)
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

