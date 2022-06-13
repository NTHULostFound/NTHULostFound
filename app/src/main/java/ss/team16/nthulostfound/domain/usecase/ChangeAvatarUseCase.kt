package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import ss.team16.nthulostfound.domain.repository.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ChangeAvatarUseCase(
    val context: Context,
    val userRepository: UserRepository
) {
    @Suppress("DEPRECATION")
    suspend operator fun invoke(uri: Uri) {
        val filename = "${UUID.randomUUID()}.png"
        val file = File(context.filesDir, filename)

        runCatching{
            val bitmap =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri
                    )
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }

            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                userRepository.setAvatarFilename(filename)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}