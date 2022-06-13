package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import ss.team16.nthulostfound.domain.model.ItemType

class GetFeedbackUseCase(
    val context: Context
) {
    val LINK = "https://forms.gle/Ut7Ge8qS55sNSQi56"

    operator fun invoke() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LINK))
        ContextCompat.startActivity(
            context, intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),null
        )
    }
}