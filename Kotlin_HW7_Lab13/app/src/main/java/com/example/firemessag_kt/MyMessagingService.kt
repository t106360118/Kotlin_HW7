package com.example.firemessag_kt

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("fcm", "refresh token:$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.run {
            var msg = StringBuffer()
            notification?.let {
                Log.e("fcm", "msg title:${it.title}, body:${it.body}")
                msg = msg.append("msg title:${it.title}, body:${it.body}\n")
            }
            data.forEach {
                val m = "key:${it.key}, value:${it.value}"
                Log.e("fcm", "m:$it")
                msg.append(m)
            }

        }

    }


}