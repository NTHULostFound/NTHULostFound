package ss.team16.nthulostfound.service

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.RegisterFCMTokenMutation
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.model.NotificationType
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.AddNotificationUseCase
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
                "INSERTED" -> NotificationType.INSERTED
                else -> throw IllegalArgumentException("type unspecified")
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
            sendNotification(message.data["content"]!!)
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "new token: $token")
        registerToken(token)
    }

    private fun sendNotification(message: String) {
        // TODO: send notification
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

}