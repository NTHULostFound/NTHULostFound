package ss.team16.nthulostfound.domain.usecase

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.delay
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class UploadImagesUseCase(
    private val uploadImagesRepository: UploadImagesRepository
) {
    suspend operator fun invoke(
        uris: List<Uri>,
        contentResolver: ContentResolver,
        onImageUploaded: (Int, String) -> Unit,
        onError: (Int, String?) -> Unit
    ): List<UploadedImage> {
        val uploadedImages = mutableListOf<UploadedImage>()

        uris.forEachIndexed { index, uri ->
            uploadImagesRepository.uploadImage(uri, contentResolver).fold(
                onSuccess = {
                    uploadedImages.add(it)
                    onImageUploaded(index, it.imageUrl)
                },
                onFailure = {
                    onError(index, it.message)
                    return@forEachIndexed
                }
            )
        }

        return uploadedImages
    }
}