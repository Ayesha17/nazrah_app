package com.nazrah.nazrahapp.preferences

import com.nazrah.nazrahapp.models.UserData
import com.nazrah.nazrahapp.preferences.PreferenceKeys.FIRST_TIME_APP_LAUNCH
import com.nazrah.nazrahapp.preferences.PreferenceKeys.USER_DATA
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Preferences(private val preferencesWrapper: PreferencesWrapper) {
    var firstTimeLaunch: Boolean
        get() = preferencesWrapper.getBoolean(FIRST_TIME_APP_LAUNCH, false)
        set(value) = preferencesWrapper.putBoolean(FIRST_TIME_APP_LAUNCH, value)
    var user: UserData?
        get() {
            val dataGet = preferencesWrapper.getString(USER_DATA, "")
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter: JsonAdapter<UserData> =
                moshi.adapter(UserData::class.java)
            return if (dataGet.isNullOrEmpty()) null else dataGet.let { adapter.fromJson(it) }
        }
        set(value) {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val jsonAdapter: JsonAdapter<UserData> =
                moshi.adapter(UserData::class.java)
            val json: String? = jsonAdapter.toJson(value)
            preferencesWrapper.putString(USER_DATA, json)
        }


}