package ss.team16.nthulostfound.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ss.team16.nthulostfound.data.source.NotificationDao
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.model.NotificationType
import ss.team16.nthulostfound.domain.repository.NotificationRepository
import java.text.SimpleDateFormat
import java.util.*

class NotificationRepositoryMockImpl(
    private val dao: NotificationDao? = null
) : NotificationRepository {
    override fun getNotifications(): Flow<List<NotificationData>> {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return flowOf(
            listOf(
                NotificationData(
                    id = 1,
                    type = NotificationType.INSERTED,
                    content = "已成功新增協尋物品「NervGear」。",
                    timestamp = formatter.parse("2022/11/06 00:00:00").time,
                    read = false,
                ),
                NotificationData(
                    id = 2,
                    type = NotificationType.CONTACT_CHECKED,
                    content = "有人在「書」查看了您的聯絡資訊！",
                    timestamp = formatter.parse("2022/05/30 12:34:56").time,
                    read = false,
                ),
                NotificationData(
                    id = 3,
                    type = NotificationType.LOST_NOTIFICATION,
                    content = "這是您遺失的「學生證」嗎？",
                    timestamp = formatter.parse("2022/05/29 22:07:19").time,
                    read = true,
                )
            )
        )
    }

    override suspend fun addNotification(notif: NotificationData) {
//        return dao.addNotification(notif)
    }

    override suspend fun updateNotification(notif: NotificationData) {
//        return dao.updateNotification(notif)
    }

    override suspend fun deleteNotification(notif: NotificationData) {
//        return dao.deleteNotification(notif)
    }

}