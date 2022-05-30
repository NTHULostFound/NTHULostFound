package ss.team16.nthulostfound.model

enum class NotificationType {
    INSERTED,
    CONTACT_CHECKED,
    LOST_NOTIFICATION
}

data class NotificationData(
    var id: Int,
    var type: NotificationType,
    var content: String = "",
    var timestamp: Long,
    var read: Boolean = false
)