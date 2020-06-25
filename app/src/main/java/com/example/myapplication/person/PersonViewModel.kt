package com.example.myapplication.person

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.myapplication.DataModel.PersonInformaitoner


enum class  PersonDataType {
    NAME,
    CPR,
    BLOD,
    MEDICIN,
    ALLERGIE,
    DONER,
    EMERGENCY,
    OTHER
}

data class PersonDataElement (
    val text: String,
    val type:  PersonDataType

)

data class PersonData(
    val profilePicture: Bitmap?,
    val elements: List<PersonDataElement>
    //val authentication: FirebaseUser?
)

class PersonViewModel: ViewModel() {
    private val logtag = PersonViewModel::class.simpleName
    private val repo = PersonInformaitoner.getUserRepo()
    private val modelViewHomeLiveData by lazy {
        MutableLiveData<PersonData?>()
    }

    fun getPersonData(): LiveData<PersonData?> = modelViewHomeLiveData

    fun personUpdateRepo(currentUser: String) {

        repo.upDataRepo(currentUser) { user ->
            if (user == null) {
                modelViewHomeLiveData.postValue(null)
            } else {
                Log.d(logtag, "user: $user")
                val list = mutableListOf<PersonDataElement>()
                list.add(PersonDataElement(user.name,  PersonDataType.NAME))
                list.add(PersonDataElement(user.cpr,  PersonDataType.CPR))
                list.add(PersonDataElement(user.donor,  PersonDataType.DONER))
                list.add(PersonDataElement(user.blodtype,  PersonDataType.BLOD))
                list.addAll(user.medicines.map { PersonDataElement(it, PersonDataType.MEDICIN) })
                list.addAll(user.allergies.map { PersonDataElement(it,  PersonDataType.ALLERGIE) })
                list.addAll(user.emergencies.map { PersonDataElement(it,  PersonDataType.EMERGENCY) })
                list.addAll(user.others.map { PersonDataElement(it,  PersonDataType.OTHER) })
                modelViewHomeLiveData.postValue(PersonData(user.image, list))
            }
        }
    }
}