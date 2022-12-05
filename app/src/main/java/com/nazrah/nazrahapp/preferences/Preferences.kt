package com.nazrah.nazrahapp.preferences

import com.nazrah.nazrahapp.preferences.PreferenceKeys.FIRST_TIME_APP_LAUNCH

class Preferences(private val preferencesWrapper: PreferencesWrapper) {
    var firstTimeLaunch: Boolean
        get() = preferencesWrapper.getBoolean(FIRST_TIME_APP_LAUNCH, false)
        set(value) = preferencesWrapper.putBoolean(FIRST_TIME_APP_LAUNCH, value)


}