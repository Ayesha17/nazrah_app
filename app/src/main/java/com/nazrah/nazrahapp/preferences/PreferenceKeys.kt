package com.nazrah.nazrahapp.preferences

import com.nazrah.nazrahapp.BuildConfig

object PreferenceKeys {
    const val TOKEN = "${BuildConfig.APPLICATION_ID}.token"
    const val UID = "${BuildConfig.APPLICATION_ID}.uId"
    const val LOCALE = "${BuildConfig.APPLICATION_ID}.locale"
    const val FIRST_TIME_APP_LAUNCH = "firstTimeAppLaunch"
    const val USER_DATA = "userData"

}
