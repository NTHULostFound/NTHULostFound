package ss.team16.nthulostfound.domain.model

import android.graphics.Bitmap

data class UserData(
    val avatar: Bitmap? = null,
    val uuid: String = "",
    val fcmToken: String = "",
    val name: String = "",
    val studentId: String = "",
    val email: String = "",
    val isNotificationEnable: Boolean = false
)