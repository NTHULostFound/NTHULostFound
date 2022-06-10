package ss.team16.nthulostfound.domain.usecase

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.delay
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class NewItemUseCase(
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(
        newItemData: NewItemData,
        imageUris: List<Uri>,
        onImageUploaded: (Int, String) -> Unit = {_, _ -> },
        onImageUploadError: (Int, Throwable) -> Unit = {_, _ -> },
        onImageUploadFinished: () -> Unit = {},
        onDataUploaded: (ItemData) -> Unit = {},
        onDataUploadError: (Throwable) -> Unit = {}
    ) {
        lateinit var uploadedImages: List<UploadedImage>

        try {
            uploadedImages = uploadImagesUseCase(
                imageUris,
                onImageUploaded,
                onImageUploadError
            )
        } catch (e: Exception) {
            return
        }

        onImageUploadFinished()

        val uploadNewItemData = newItemData.copy(
            images = uploadedImages.map { it.imageUrl }
        )

        itemRepository.newItem(uploadNewItemData).fold(
            onSuccess = { item -> onDataUploaded(item) },
            onFailure = { onDataUploadError(it) }
        )
    }
}