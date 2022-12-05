package com.nazrah.nazrahapp.database

import com.google.firebase.firestore.FirebaseFirestore
import com.nazrah.nazrahapp.utils.Constants.CLASS_COLLECTION

class FirestoreSource constructor(private val fireStoreDb: FirebaseFirestore) {

     fun createClass(classCollection: MutableMap<String, Any>, handleResult: (String) -> Unit) {

        fireStoreDb.collection(CLASS_COLLECTION).add(classCollection).addOnSuccessListener {

            handleResult("true")

        }.addOnFailureListener {
            handleResult("false")
        }


    }
}