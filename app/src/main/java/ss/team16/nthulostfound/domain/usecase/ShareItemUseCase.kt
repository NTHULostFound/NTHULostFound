package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType

class ShareItemUseCase(
    val context: Context
) {

    companion object {
        const val DYNAMIC_LINK_BASE_URI = "https://nthulostfound.page.link/item"
        const val DYNAMIC_LINK_DOMAIN_PREFIX = "https://nthulostfound.page.link"
        const val FALLBACK_LINK = "https://play.google.com/store/apps/details?id=ss.team16.nthulostfound"
    }

    operator fun invoke(item: ItemData) {

        val message =
            if (item.type == ItemType.LOST)
                "喔不，我的${item.name}不見了 QQ，有人可以幫我找嗎？\n" +
                "立刻下載「清大遺失物平台」，查看更多資訊！"
            else
                "我在${item.place}撿到了${item.name}！有人遺失了${item.name}嗎？\n" +
                "立刻下載「清大遺失物平台」來查看更多資訊吧！"

        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("$DYNAMIC_LINK_BASE_URI/?id=${item.uuid}")
            domainUriPrefix = DYNAMIC_LINK_DOMAIN_PREFIX
            androidParameters {
                minimumVersion = 2
            }
            // Fake iOS link
            iosParameters("ss.team16.nthulostfound") {
                setFallbackUrl(FALLBACK_LINK.toUri())
                ipadFallbackUrl = FALLBACK_LINK.toUri()
            }
            socialMetaTagParameters {
                title = item.name
                description = message

                if (item.images.isNotEmpty())
                    imageUrl = item.images.first().toUri()
            }
        }.uri.toString()

        val linkWithFallback = "$dynamicLink&ofl=$FALLBACK_LINK"

        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            longLink = linkWithFallback.toUri()
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, "$message\n連結: $shortLink")
            }

            ContextCompat.startActivity(
                context,
                Intent.createChooser(intent, "分享物品資訊...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                null
            )
        }
    }
}