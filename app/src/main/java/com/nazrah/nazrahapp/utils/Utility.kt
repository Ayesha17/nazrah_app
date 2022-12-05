package com.nazrah.nazrahapp.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nazrah.nazrahapp.MainActivity
import com.nazrah.nazrahapp.R


object Utility {

    fun sendNotification(model: String, context: Context) {

        val intent = Intent(context, MainActivity::class.java)
        intent.apply {
            putExtra("Test", model)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle().bigText(model))
            //.setContentTitle("Zofeur")
            .setContentText(model)
            .setColor(Color.parseColor("#000000"))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (model.isNotEmpty()) {
            notificationManager.notify(
                Constants.NOTIFICATION_ID /* ID of notification */,
                notificationBuilder.build()
            )
        }

    }

}