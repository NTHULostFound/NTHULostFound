package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.ktx.Firebase
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType

class ShareItemUseCase(
    val context: Context
) {

    companion object {
        const val DYNAMIC_LINK_BASE_URI = "https://nthu-lost-found.link"
        const val DYNAMIC_LINK_DOMAIN_PREFIX = "https://nthulostfound.page.link"
    }

    operator fun invoke(item: ItemData) {
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse("$DYNAMIC_LINK_BASE_URI/?item=${item.uuid}")
            domainUriPrefix = DYNAMIC_LINK_DOMAIN_PREFIX
            androidParameters {
                minimumVersion = 2
            }
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                if (item.type == ItemType.LOST) {
                    putExtra(Intent.EXTRA_TEXT, "喔不，我的${item.name}不見了 QQ，有人可以幫我找嗎？\n" +
                            "立刻下載「清大遺失物平台」，查看更多資訊！\n" +
                            "連結: $shortLink")
                } else if (item.type == ItemType.FOUND) {
                    putExtra(Intent.EXTRA_TEXT, "我在${item.place}撿到了${item.name}！有人遺失了${item.name}嗎？\n" +
                            "立刻下載「清大遺失物平台」來查看更多資訊吧！\n" +
                            "連結: $shortLink")
                }
            }

            ContextCompat.startActivity(
                context,
                Intent.createChooser(intent, "分享物品資訊...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                null
            )
        }
    }
}