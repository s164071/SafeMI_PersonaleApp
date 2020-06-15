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
import android.nfc.Tag
import android.os.Message
import android.util.*
import android.widget.TextView
import com.estimote.coresdk.common.internal.utils.L
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.ProximityZoneContext
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_nearby.*

class nearby : Fragment() {

    private lateinit var activity: MainActivity
    private var observationsHandler: ProximityObserver.Handler? = null
    private val logTags = MainActivity::class.simpleName
    private var borgernavn = ""
    private var value = ""
    private var key = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_nearby, container, false)

        activity = getActivity() as MainActivity

        beacon()

        val borger = view.findViewById<>()

        borger.text="Borger i nærheden: " + borgernavn
        oplysninger.text="Oplysninger: " + key + " " + value

        return view
    }


    ///////////////////////////////////////


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
                .onError { throwable ->
                    Log.e("app", "proximity observer error: $throwable")
                }
                .build()


        //3. Definerer Proximity zone

        val venueZone = ProximityZoneBuilder()
            .forTag("Jasmin")
            .inNearRange()

            .onEnter { zoneContext ->
                borgernavn = zoneContext.tag
                Log.d(logTags, "Entered: " + borgernavn)
                /* val title = zoneContext.attachments["CPR"]
             val description = zoneContext.attachments["0123456789"]
             Log.i(logTags, title + "" + description)*/
            }
            .onExit { zoneContext ->
                Log.i(logTags, "Exited: " + borgernavn) //når bruger forlader zone
                /* val title = zoneContext.attachments["CPR"]
             val description = zoneContext.attachments["0123456789"]
             Log.i(logTags, title + "" + description)*/
            }
            .onContextChange { contexts ->
                for (context in contexts) {
                    key = "CPR"
                    value = context.attachments[key] ?: "kukuk"

                    val notNullPersons = contexts.filterNotNull()
                    if (notNullPersons.isNotEmpty()) {
                        Log.i(logTags, "Oplysninger: " + key + " " + value)
                    }

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

}
