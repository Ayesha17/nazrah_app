package com.nazrah.nazrahapp.utils

object Constants {

    const val NOTIFICATION_ID = 1
    const val TEACHER = "Teacher"
    const val STUDENTS = "Student"
    const val OTHERS = "Other"
    const val USERS = "Users"
    const val CLASS_COLLECTION = "classes"




    val hashMap= hashMapOf<String,Int>().apply {

        this[TEACHER]=1
        this[STUDENTS]=2
        this[OTHERS]=3
    }
}