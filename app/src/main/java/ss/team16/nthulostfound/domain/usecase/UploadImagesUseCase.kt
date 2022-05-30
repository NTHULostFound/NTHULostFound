package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class UploadImagesUseCase(
    private val uploadImagesRepository: UploadImagesRepository
) {
    suspend operator fun invoke(): List<UploadedImage> {
        // TODO
        return emptyList()
    }
}