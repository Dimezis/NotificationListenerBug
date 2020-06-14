package com.eightbitlab.notificationlistenerbug

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!haveNotificationPermission()) {
            val permissionsIntent =
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(permissionsIntent)
        }
        //Introduce any code changes and rebuild/reinstall the app
//        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show()
    }

    private fun haveNotificationPermission(): Boolean {
        val enabledListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        ).split(":")

        val stackerService = ComponentName(this, NotificationListener::class.java).flattenToString()
        return enabledListeners.contains(stackerService)
    }
}