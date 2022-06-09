package ss.team16.nthulostfound.data.repository

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.data.source.NotificationDao
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val dao: NotificationDao
) : NotificationRepository {
    override fun getNotifications(): Flow<List<NotificationData>> {
        return dao.getNotifications()
    }

    override suspend fun addNotification(notif: NotificationData): Long {
        return dao.addNotification(notif)
    }

    override suspend fun updateNotification(notif: NotificationData) {
        return dao.updateNotification(notif)
    }

    override suspend fun deleteNotification(notif: NotificationData) {
        return dao.deleteNotification(notif)
    }

}