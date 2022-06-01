package ss.team16.nthulostfound.domain.usecase

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.delay
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository

class NewItemUseCase(
    private val uploadImagesUseCase: UploadImagesUseCase
) {
    suspend operator fun invoke(
        newItemData: NewItemData,
        imageUris: List<Uri>,
        contentResolver: ContentResolver,
        onImageUploaded: (Int, String) -> Unit = {_, _ -> },
        onImageUploadError: (Int, Throwable) -> Unit = {_, _ -> },
        onImageUploadFinished: () -> Unit = {},
        onDataUploaded: () -> Unit = {},
        onDataUploadError: (Throwable) -> Unit = {}
    ) {
        try {
            uploadImagesUseCase(imageUris,contentResolver, onImageUploaded, onImageUploadError)
        } catch (e: Exception) {
            return
        }

        onImageUploadFinished()

        delay(1000L)
        onDataUploaded()
    }
}