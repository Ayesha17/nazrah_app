package com.nazrah.nazrahapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.nazrah.nazrahapp.preferences.Preferences
import com.nazrah.nazrahapp.utils.TimberLineNumberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass : Application() {
    @Inject
    lateinit var preferences: Preferences

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Timber.plant(TimberLineNumberDebugTree("--- MedIQ ---"))

    }

}