package com.nazrah.nazrahapp.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkConnection {
    fun isOnline(context: Context?): Boolean {
        if (context != null) {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni != null && ni.isConnected) return true
        }
        return false
    }
}