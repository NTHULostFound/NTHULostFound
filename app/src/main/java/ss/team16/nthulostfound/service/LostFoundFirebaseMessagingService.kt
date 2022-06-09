package ss.team16.nthulostfound.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.RegisterFCMTokenMutation
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.model.NotificationType
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.AddNotificationUseCase
import ss.team16.nthulostfound.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class LostFoundFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var addNotificationUseCase: AddNotificationUseCase
    @Inject lateinit var apolloClient: ApolloClient
    @Inject lateinit var userRepository: UserRepository

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "From: ${message.from}")
        message.notification?.let {
            Log.d("FCM", "${it.title}: ${it.body}")
            Log.d("FCM", "${it.imageUrl}")
        }
        Log.d("FCM", "sent time(epoch): ${message.sentTime}")

        if (message.data.isNotEmpty()) {
            Log.d("FCM", "Message data payload: ${message.data}")
            val notificationType = when(message.data["type"]) {
                "LOST_NOTIFICATION" -> NotificationType.LOST_NOTIFICATION
                "CONTACT_CHECKED" -> NotificationType.CONTACT_CHECKED
                "LOST_INSERTED" -> NotificationType.LOST_INSERTED
                "FOUND_INSERTED" -> NotificationType.FOUND_INSERTED
                else -> NotificationType.UNSPECIFIED
            }
            val notifTitle = when(notificationType) {
                NotificationType.LOST_NOTIFICATION -> getString(R.string.lost_notification_message, message.data["content"]!!)
                else -> "NTHU Lost & Found"
            }
            val notifMessage = when(notificationType) {
                NotificationType.LOST_NOTIFICATION -> "有人拾獲了與您資訊相符的物品！\n立刻與拾物者取得聯繫！"
                NotificationType.CONTACT_CHECKED -> getString(R.string.contact_checked_message, message.data["content"]!!)
                NotificationType.LOST_INSERTED -> getString(R.string.lost_inserted_message, message.data["content"]!!)
                NotificationType.FOUND_INSERTED -> getString(R.string.found_inserted_message, message.data["content"]!!)
                NotificationType.UNSPECIFIED -> message.data["content"]!!
            }

            val notificationData = NotificationData(
                type = notificationType,
                content = message.data["content"]!!,
                itemUUID = message.data["item_uuid"]!!,
                timestamp = message.sentTime
            )
            CoroutineScope(Dispatchers.IO).launch {
                addNotificationUseCase(notificationData)
            }
            sendNotification(notifTitle, notifMessage, message.data["item_uuid"]!!)
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "new token: $token")
        registerToken(token)
    }

    private fun sendNotification(title: String, message: String, itemId: String) {
        // TODO: send notification
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        Log.d("FCM", "send notification")
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "nthulostfound://item/$itemId".toUri(),
            this,
            MainActivity::class.java
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

//        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
//            PendingIntent.FLAG_IMMUTABLE)
//
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(deepLinkPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id++ /* ID of notification */, notificationBuilder.build())
    }

    private fun registerToken(fcmToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apolloClient.mutation(RegisterFCMTokenMutation(fcmToken)).execute().dataAssertNoErrors
                val accessToken = response.registerFCMToken.accessToken

                Log.d("FCM-Apollo", "register fcm token $fcmToken, get access token $accessToken")
                userRepository.saveAccessToken(accessToken)
            } catch(e: Exception) {
                Log.e("FCM-Apollo", "Error while registering token", e)
            }
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NTHU_LOST_FOUND_NOTIFICATION"
        const val NOTIFICATION_CHANNEL_NAME = "Lost & Found Notification"
        var id = 0
    }

}