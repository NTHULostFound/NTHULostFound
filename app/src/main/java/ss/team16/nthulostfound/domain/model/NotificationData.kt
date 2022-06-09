package ss.team16.nthulostfound.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationType {
    LOST_INSERTED,
    FOUND_INSERTED,
    CONTACT_CHECKED,
    LOST_NOTIFICATION,
    UNSPECIFIED
}

@Entity(tableName = "notification")
data class NotificationData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var type: NotificationType,
    var content: String = "",
    @ColumnInfo(name = "item_uuid")
    var itemUUID: String = "",
    var timestamp: Long,
    var read: Boolean = false
)