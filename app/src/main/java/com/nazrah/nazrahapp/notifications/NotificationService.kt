package com.nazrah.nazrahapp.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nazrah.nazrahapp.utils.Utility


class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.messageId?.let { Utility.sendNotification(it, applicationContext) }
        Log.e("NotificationService", "$remoteMessage")

    }


    override fun onNewToken(token: String) {
        Log.e("NotificationService", token)
    }


}