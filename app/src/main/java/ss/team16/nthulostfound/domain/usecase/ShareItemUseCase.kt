package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType

class ShareItemUseCase(
    val context: Context
) {

    operator fun invoke(item: ItemData) {

        val message =
            if (item.type == ItemType.LOST)
                "喔不，我的${item.name}不見了 QQ，有人可以幫我找嗎？\n" +
                "立刻下載「清大遺失物平台」，查看更多資訊！"
            else
                "我在${item.place}撿到了${item.name}！有人遺失了${item.name}嗎？\n" +
                "立刻下載「清大遺失物平台」來查看更多資訊吧！"

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            putExtra(Intent.EXTRA_TEXT, "$message\n連結: ${item.dynamicLink}")
        }

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, "分享物品資訊...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            null
        )
    }
}