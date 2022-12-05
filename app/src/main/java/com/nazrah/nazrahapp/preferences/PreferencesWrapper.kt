package com.nazrah.nazrahapp.preferences

import android.content.Context
import androidx.preference.PreferenceManager

class PreferencesWrapper(context: Context) {
    private val mPref = PreferenceManager.getDefaultSharedPreferences(context)
    fun getBoolean(key: String?, defaultValue: Boolean = false): Boolean {
        return mPref.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String?, value: Boolean) {
        mPref.edit().putBoolean(key, value).apply()
    }

    fun putInt(key: String?, value: Int) {
        mPref.edit().putInt(key, value).apply()
    }

    fun getInt(key: String?, defaultValue: Int): Int = mPref.getInt(key, defaultValue)


    fun putString(key: String?, value: String?) {
        mPref.edit().putString(key, value).apply()
    }

    fun getString(key: String?, defaultValue: String?): String? = mPref.getString(key, defaultValue)


    fun clear() {
        mPref.all.clear()
    }

    fun clearData(values: String) {
        mPref.edit().remove(values).commit();
    }
}