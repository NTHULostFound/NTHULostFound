package ss.team16.nthulostfound.domain.repository

import android.net.Uri
import ss.team16.nthulostfound.domain.model.UploadedImage

interface UploadImagesRepository {
    suspend fun uploadImage(
        uri: Uri,
        title: String? = null
    ): Result<UploadedImage>
}
