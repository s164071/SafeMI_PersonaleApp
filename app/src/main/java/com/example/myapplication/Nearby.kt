@file:Suppress("DEPRECATION")

package com.example.myapplication


import android.os.Bundle


import android.view.LayoutInflater


import android.view.View


import android.view.ViewGroup


import androidx.fragment.app.Fragment


import com.estimote.proximity_sdk.api.*


import android.graphics.Bitmap


import android.graphics.BitmapFactory


import android.util.*


import android.widget.ImageButton


import android.widget.ImageView


import android.widget.TextView


import android.widget.Toast


import androidx.core.os.bundleOf


import androidx.core.view.drawToBitmap


import androidx.core.view.isVisible


import androidx.fragment.app.activityViewModels


import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory


import com.example.myapplication.DataModel.User


import com.example.myapplication.person.Person


import com.example.myapplication.person.PersonViewModel


import com.google.firebase.auth.FirebaseAuth


import com.google.firebase.database.DataSnapshot


import com.google.firebase.database.DatabaseError


import com.google.firebase.database.FirebaseDatabase


import com.google.firebase.database.ValueEventListener


import com.google.firebase.storage.FirebaseStorage


import kotlinx.android.synthetic.main.fragment_nearby.*


import kotlinx.android.synthetic.main.fragment_nearby.horisontalline


import java.io.ByteArrayOutputStream

import java.io.File


class Nearby : Fragment() {

    private lateinit var activity: MainActivity
    private var observationsHandler: ProximityObserver.Handler? = null
    private val logTags = MainActivity::class.simpleName
    private var borgernavn = ""
    private var value = ""
    private var key = ""
    private lateinit var bundle: Bundle
    private lateinit var mystorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    var bitmap: Bitmap? = null
    private var user = ""
    private val model: PersonViewModel by activityViewModels()
    val fragment_person = Person()
    var information = false

    ///////////////////////////////////////


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)


        navn.visibility = View.GONE


        cpr.visibility = View.GONE


        horisontalline.visibility = View.GONE


        logud.visibility = View.VISIBLE


    }


    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,


        savedInstanceState: Bundle?


    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_nearby, container, false)


        activity = getActivity() as MainActivity


        beacon()
        view?.findViewById<ImageView>(R.id.patientPic)?.setOnClickListener() {
            bundle = transferInformationToNextFragment(patientinfoBox, patientinfoBox2, patientPic)
            if (keineborgere.isVisible == false) {
                updateUI()

            }



            bundle = transferInformationToNextFragment(patientinfoBox, patientinfoBox2, patientPic)


        }


        val recent: ImageButton = view.findViewById(R.id.recent)


        recent.setOnClickListener() {


            if (this::bundle.isInitialized && information == true) {


                showRecent(bundle)


            } else {


                bundle = Bundle()



                showRecent(bundle)



                showRecent(bundle)


            }

        }


        return view


    }


    private fun beacon() {
        //1. Opsætter Estimote credentials for forbindelse til estimote cloud


        val cloudCredentials = EstimoteCloudCredentials(


            "s170720-student-dtu-dk-s-p-j0r",


            "124af5cc28250d8e2d759fafd1fb5010"


        )


        //2. Opretter Proximity Observer


        val proximityObserver =


            ProximityObserverBuilder(activity.applicationContext, cloudCredentials)

                .withBalancedPowerMode()
                .withEstimoteSecureMonitoringDisabled() //add to reduce the number of BLE callbacks being registered in OS, thus reducing the possibility of Scan error 2
                .withTelemetryReportingDisabled() // same as above

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

                keineborgere.visibility = View.GONE
                horisontalline.visibility = View.VISIBLE
                navn.visibility = View.VISIBLE
                cpr.visibility = View.VISIBLE
                patientPic.visibility = View.VISIBLE

                retrieveBeaconInformation()
            }


            .onExit { zoneContext ->
                Log.i(logTags, "Exited: " + borgernavn) //når bruger forlader zone
                patientinfoBox.text = ""
                patientinfoBox2.text = ""
                keineborgere.visibility = View.VISIBLE
                horisontalline.visibility = View.GONE
                navn.visibility = View.GONE
                cpr.visibility = View.GONE
                patientPic.visibility = View.GONE
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

    override fun onResume() {
        beacon()
        Log.d(logTags, "beacon() kaldes igen")
        super.onResume()
    }

//fremsøger navn & cpr

    private fun retrievePersonalInformation(user: String) {

        Log.d(logTags, "Hej dette er brugeren i retrievePersonalinformation" + user)

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
                    Log.d(logTags, "RetrievebeaconInformation" + user)
                    if (user != "") {
                        retrievePersonalInformation(user)
                        model.personUpdateRepo(user)
                        download(user) //henter billede med det pågældende uid

                    }

                }

            })


    }


//prøv med user fra ovenstående


    private fun download(user: String) {


        auth = FirebaseAuth.getInstance()


        mystorage = FirebaseStorage.getInstance()


        Log.i(logTags, "Downloader billede: " + user)


        val ref = mystorage.reference.child("$user/image/ProfilePic.jpg")
        val file = File.createTempFile("ProfilePic", "jpg")

        Log.d(logTags, "file $file")
        ref.getFile(file).addOnCompleteListener() { task ->
            if (task.isSuccessful && file!=null) {

                bitmap = BitmapFactory.decodeFile(file.absolutePath)

                Log.d(logTags, "file path" + file.absolutePath)

                patientPic.setImageBitmap(bitmap)
                Log.d(logTags, "SUCCESS load and set profilepic")
            } else

                Log.d(logTags, "Failed to load and set profilepic $task")

        }
    }


    private fun updateUI() {


        Log.d(logTags, "Du skrifter fragment " + user)

        val manager = parentFragmentManager
        if (manager != null) {
            val transactionToNearby = manager.beginTransaction()
            transactionToNearby.replace(R.id.fragtop, fragment_person)
            transactionToNearby.addToBackStack(null)
            transactionToNearby.commit()
        } else {
            Toast.makeText(
                activity, "Fejl kunne ikke overføre bruger", Toast.LENGTH_SHORT

            ).show()


        }


    }


    fun showRecent(bundle: Bundle) {
        val recentFragmentos: Recent = Recent()
        val manager = parentFragmentManager
        if (bundle != null) {
            recentFragmentos.arguments = bundle
        }
        if (manager != null) {
            Log.d(logTags, "Der ledes tilbage til tidligere fragment")
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragtop, recentFragmentos)
            transaction.addToBackStack(null)
            transaction.commit()

        }


    }


    fun transferInformationToNextFragment(
        textViewName: TextView,
        textViewCPR: TextView,
        imageViewProfilePic: ImageView
    ): Bundle {


        information = true
        var bundle: Bundle = Bundle()
        bundle.putString("name", patientinfoBox.text.toString())
        bundle.putString("cpr", patientinfoBox2.text.toString())
        val image = imageViewProfilePic.drawToBitmap()
        var bs: ByteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 50, bs)
        bundle.putByteArray("ProfilePic", bs.toByteArray())
        Log.d(logTags, "der overføres informationer til næste fragment")

        return bundle


    }


}




