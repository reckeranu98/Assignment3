package com.lu.ass3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.lu.ass3.MainActivity.Companion.MSG_KEY


class MessageActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val tvReceivedMessage:EditText = findViewById(R.id.msgValueET)

        val message = intent.getStringExtra(MSG_KEY)?: ""
        tvReceivedMessage.setText(message)

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQ_CODE_POST_NOTIFY
            )
        } else {
            // Permission is already granted, proceed with creating the notification
            createNotification(message)
        }
    }

    private fun createNotification(message: String) {
        val channelId = "NOTIFICATION_MSG_CHANNEL"
        val notificationId = 101
        val context = this;

        // Create an Intent for the activity you want to start
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Create Notification Channel for Android 8.0+

        val name = "MessageChannel"
        val descriptionText = "Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        // Build the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_notification_round)
            .setContentTitle("Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setLights(0xFF00FF00.toInt(), 350, 1100)
            .setVibrate(longArrayOf(0, 600, 1200))

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED)
                return
            notify(notificationId, builder.build())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE_POST_NOTIFY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, create the notification
                createNotification(intent.getStringExtra(MSG_KEY) ?: "")
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable notification feature)
                // Optional: Notify the user that notifications won't work without the permission.
            }
        }
    }

    companion object{
        private const val REQ_CODE_POST_NOTIFY = 1
    }
}
