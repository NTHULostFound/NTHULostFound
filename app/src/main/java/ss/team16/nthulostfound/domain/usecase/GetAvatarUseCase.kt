package ss.team16.nthulostfound.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ss.team16.nthulostfound.domain.repository.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GetAvatarUseCase(
    val context: Context,
    val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Bitmap?> {
        return userRepository.getAvatarFilename().map { filename ->
            if (filename == null)
                return@map null

            val file = File(context.filesDir, filename)
            if (!file.exists())
                return@map  null

            val bitmap = runCatching {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        file.toUri()
                    )
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, file.toUri())
                    ImageDecoder.decodeBitmap(source)
                }
            }

            bitmap.fold(
                onSuccess = { return@map it },
                onFailure = { return@map null }
            )
        }
    }

}