package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ss.team16.nthulostfound.domain.model.ItemData

class ShareItemUseCase {
    suspend operator fun invoke(context: Context, item: ItemData) {
        val type = "text/plain"
        val subject = item.name
        val extraText = item.description
        val shareWith = "ShareWith"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = type
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, extraText)

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }
}