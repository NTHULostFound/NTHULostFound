package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ss.team16.nthulostfound.domain.model.ItemType

class ShareResultUseCase(
    val context: Context
) {

    operator fun invoke(itemType: ItemType, itemName: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            if (itemType == ItemType.FOUND) {
                putExtra(Intent.EXTRA_TEXT, "我剛剛透過「清大遺失物平台」幫助了別人找回「$itemName」！\n" +
                        "大家也來加入清大的遺失物社群，讓您的善心幫助更多人吧！\n" +
                        "現在立刻下載: $link")
            } else {
                putExtra(Intent.EXTRA_TEXT, "我剛剛透過「清大遺失物平台」找回了「$itemName」！感謝平台上的好心人！\n" +
                        "大家也來加入清大的遺失物社群，來幫助更多人，創造更美好的世界吧！\n" +
                        "現在立刻下載: $link")
            }
        }

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, "分享您的喜悅...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            null
        )
    }

    companion object {
        const val link = "https://nthulostfound.page.link/app"
    }

}