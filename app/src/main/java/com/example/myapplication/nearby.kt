package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import com.estimote.proximity_sdk.api.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.os.Message
import android.util.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.estimote.coresdk.common.internal.utils.L
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.ProximityZoneContext
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.person.Person
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_nearby.*
import kotlinx.android.synthetic.main.fragment_nearby.view.*
import java.io.File

class nearby : Fragment() {

    val fragment_person = Person()

    private lateinit var activity: MainActivity
    private var observationsHandler: ProximityObserver.Handler? = null
    private val logTags = MainActivity::class.simpleName
    private var borgernavn = ""
    private var value = ""
    private var key = ""
    private lateinit var mystorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    var bitmap: Bitmap? = null


    ///////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_nearby, container, false)

        activity = getActivity() as MainActivity

        beacon()

        // view.findViewById<TextView>(R.id.borger).setText(borgernavn)

        // borger.text= borgernavn


        return view
    }


    fun beacon() {


        //1. Opsætter Estimote credentials for forbindelse til estimote cloud
        val cloudCredentials = EstimoteCloudCredentials(
            "s170720-student-dtu-dk-s-p-j0r",
            "124af5cc28250d8e2d759fafd1fb5010"
        )


        //2. Opretter Proximity Observer
        val proximityObserver =
            ProximityObserverBuilder(activity.applicationContext, cloudCredentials)
                .withBalancedPowerMode()
                .onError { throwable ->
                    Log.e("app", "proximity observer error: $throwable")
                }
                .build()


        //3. Definerer Proximity zone

        val venueZone = ProximityZoneBuilder()
            .forTag("patient1") //remember to change iBeaconTag for the right beacon
            .inNearRange()

            .onEnter { zoneContext ->
                borgernavn = zoneContext.tag
                Log.d(logTags, "Entered: " + borgernavn)

                keineborgere.visibility=View.GONE
                horisontalline.visibility=View.VISIBLE

                retrieveBeaconInformation()

            }
            .onExit { zoneContext ->
                Log.i(logTags, "Exited: " + borgernavn) //når bruger forlader zone

                patientinfoBox.text = ""
                patientinfoBox2.text = ""

                keineborgere.visibility=View.VISIBLE
                horisontalline.visibility=View.GONE




                }

            .onContextChange { contexts ->
                for (context in contexts) {
                    key = "CPR"
                    value = context.attachments[key] ?: "kukuk"

                    val notNullPersons = contexts.filterNotNull()

                }
            }
            .build()


        //4. Lokationstilladelse + Starter Proximity observering
        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(activity,
                {
                    Log.i("app", "Krav opfyldt")
                    val observationsHandler =
                        proximityObserver.startObserving(venueZone) // onRequirementsFulfilled
                },
                { requirements ->
                    Log.e(
                        "app",
                        "Krav mangler - Scanning virker ikke: " + requirements   //onRequirementsMissing
                    )
                },
                { throwable ->
                    Log.e("app", "Fejl i krav: " + throwable) //onError
                })

    }

    //6. Stopper scanning
    override fun onDestroy() {
        observationsHandler?.stop()
        super.onDestroy()

    }

    //fremsøger navn & cpr
    private fun retrievePersonalInformation(user: String) {
        Log.d(logTags, "Hej dette er brugeren " + user)

        FirebaseDatabase.getInstance().getReference().child("users").child(user)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(logTags, "Jeg har IKKE søgt " + user)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var mapUser = dataSnapshot.value as Map<String, Any>
                    Log.d(logTags, "Jeg har søgt " + user)
                    patientinfoBox.text = mapUser["navn"].toString()
                    patientinfoBox2.text = "CPR: " + mapUser["persId"].toString()


                }


            })
        Log.d(logTags, "Hej dette er brugeren " + user)
        view?.findViewById<ImageView>(R.id.patientPic)?.setOnClickListener() {
            transferData(user)
            updateUI(user)
            Log.d(logTags, "UpdateUImetode der over føres "+user)
        }
    }

    private fun transferData(user:String) {

        TODO("Not yet implemented")
    }



    //anvender TAG til at finde tilknyttede uuid og bruger derefter retrievePersonalInformation til at udtrække navn og cpr
    private fun retrieveBeaconInformation() {
        FirebaseDatabase.getInstance().getReference().child("ibeacon").child(borgernavn)

            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var map = dataSnapshot.value as Map<String, Any>

                    var user = map["brugerUID"].toString()

                    Log.d(logTags, "dette er brugeren " + user)
                    //patientinfoBox2.text = user
                    if (user != "") {
                        retrievePersonalInformation(user)
                        download(user)
                    }
                }

            })
    }
//prøv med user fra ovenstående
    private fun download(user: String) {
        auth = FirebaseAuth.getInstance()
        mystorage = FirebaseStorage.getInstance()
        val storageRef = mystorage.reference
        //val user = auth.currentUser?.uid.toString()

        Log.i(logTags, "Bruger: " +user)


        val ref = mystorage.reference.child("$user/image/ProfilePic.jpg")

        val file = File.createTempFile("ProfilePic", "jpg")

        Log.d(logTags, "file $file")
        ref.getFile(file).addOnCompleteListener() { task ->
            if  (task.isSuccessful) {
                bitmap = BitmapFactory.decodeFile(file.absolutePath)
                Log.d(logTags, "file path" + file.absolutePath)

                patientPic.setImageBitmap(bitmap)

                Log.d(logTags, "SUCCESS load and set profilepic")

            } else
                Log.d(logTags, "Failed to load and set profilepic $task")

        }
    }

     fun updateUI(user:String){

        Log.d(logTags, "Hej dette er brugeren der bliver ført videre " + user)
        val manager = fragmentManager
        if(manager!=null && user!=null) {

            val transactionToNearby = manager.beginTransaction()
            transactionToNearby.replace(R.id.fragtop, fragment_person)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()

        } else{
            Toast.makeText(activity,"Fejl kunne ikke overføre bruger", Toast.LENGTH_SHORT
            ).show()       }
    }



}

