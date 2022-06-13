package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

class GetFeedbackUseCase(
    val context: Context
) {

    operator fun invoke() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        ContextCompat.startActivity(
            context, intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),null
        )
    }

    companion object {
        const val link = "https://forms.gle/Ut7Ge8qS55sNSQi56"
    }
}