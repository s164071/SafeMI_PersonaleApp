package com.example.myapplication.person
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import androidx.fragment.app.activityViewModels

class Person : Fragment() {

    private lateinit var activity: MainActivity
    private val logtag = Person::class.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var mystorage: FirebaseStorage

    private val model: PersonViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_person, container, false)

        view.findViewById<ImageView>(R.id.backfromPTinformationer).setOnClickListener() {
            Log.d(logtag,"Der bliver nu trykket på tilbage fra personfragment")
                    val manager : FragmentManager? =parentFragmentManager
            if (manager!=null){
                manager.popBackStack()
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        database = Firebase.database.reference

        val personListAdaptor = PersonAdaptor()
        Log.d(logtag, "$personListAdaptor")
        val list: RecyclerView = view.findViewById(R.id.home_RecyclerView)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        list.adapter = personListAdaptor


        model.getPersonData().observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            personListAdaptor.list.clear()
            Log.d(logtag, "elementer ${it.elements}")
            personListAdaptor.list.addAll(it.elements)
            personListAdaptor.notifyDataSetChanged()
            Log.d(logtag, " observere for ændringer")

            view.findViewById<ImageView>(R.id.person_ProfilePic).setImageBitmap(it.profilePicture)

        })




    }







}

