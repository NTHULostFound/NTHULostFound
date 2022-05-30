package ss.team16.nthulostfound.data.repository

import android.net.Uri
import ss.team16.nthulostfound.data.source.ImgurApi
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class UploadImagesRepositoryImgurImpl(
    private val imgurApi: ImgurApi
): UploadImagesRepository{
    override suspend fun uploadImage(uri: Uri, title: String?): Result<UploadedImage> {
        TODO("Not yet implemented")
    }
}