package ss.team16.nthulostfound.model

import android.graphics.Bitmap

data class UserData(
    var avatar: Bitmap? = null,
    var uuid: String = "",
    var fcmToken: String = "",
    var name: String = "",
    var studentId: String = "",
    var email: String = ""
)