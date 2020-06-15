package com.example.myapplication.DataModel

import com.google.firebase.database.DataSnapshot

class PersonInformaitoner (snapshot: DataSnapshot) {
    lateinit var navn: String
    lateinit var persid: String
    lateinit var allergies: String

    init {
        try {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            allergies=snapshot.key?: ""
            navn = data["name"] as String
            persid= data["persid"] as String
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}