package ss.team16.nthulostfound.domain.model

import android.graphics.Bitmap

data class UserData(
    val uuid: String = "",
    val fcmToken: String = "",
    val accessToken: String = "",
    val avatar: Bitmap? = null,
    val name: String = "",
    val studentId: String = "",
    val email: String = "",
    val isNotificationEnable: Boolean = false
)