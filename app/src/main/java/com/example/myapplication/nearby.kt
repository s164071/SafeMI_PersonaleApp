package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [nearby.newInstance] factory method to
 * create an instance of this fragment.
 */
class nearby : Fragment() {
    // TODO: Rename and change types of parameters




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view : View = inflater.inflate(R.layout.fragment_nearby, container, false)
        val recent: ImageButton = view.findViewById(R.id.recent)

        recent.setOnClickListener(){
            val fragmentPerson = Person()
            val manager = fragmentManager
            if (manager != null) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragtop, fragmentPerson)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        return view
    }

}