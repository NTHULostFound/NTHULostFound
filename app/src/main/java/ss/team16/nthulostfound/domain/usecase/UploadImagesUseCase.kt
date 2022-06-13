package ss.team16.nthulostfound.domain.usecase

import android.net.Uri
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class UploadImagesUseCase(
    private val uploadImagesRepository: UploadImagesRepository
) {
    suspend operator fun invoke(
        uris: List<Uri>,
        onImageUploaded: (Int, String) -> Unit,
        onError: (Int, Throwable) -> Unit
    ): List<UploadedImage> {
        val uploadedImages = mutableListOf<UploadedImage>()

        uris.forEachIndexed { index, uri ->
            uploadImagesRepository.uploadImage(uri).fold(
                onSuccess = {
                    uploadedImages.add(it)
                    onImageUploaded(index, it.imageUrl)
                },
                onFailure = {
                    onError(index, it)
                    throw Exception(it)
                }
            )
        }

        return uploadedImages
    }
}