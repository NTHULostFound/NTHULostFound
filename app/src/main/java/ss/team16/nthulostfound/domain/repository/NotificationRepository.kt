package ss.team16.nthulostfound.domain.repository

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.NotificationData

interface NotificationRepository {
    fun getNotifications() : Flow<List<NotificationData>>
    suspend fun addNotification(notif: NotificationData)
    suspend fun updateNotification(notif: NotificationData)
    suspend fun deleteNotification(notif: NotificationData)
}