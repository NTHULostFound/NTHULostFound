package ss.team16.nthulostfound.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationType {
    INSERTED,
    CONTACT_CHECKED,
    LOST_NOTIFICATION
}

@Entity(tableName = "notification")
data class NotificationData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type: NotificationType,
    var content: String = "",
    var timestamp: Long,
    var read: Boolean = false
)