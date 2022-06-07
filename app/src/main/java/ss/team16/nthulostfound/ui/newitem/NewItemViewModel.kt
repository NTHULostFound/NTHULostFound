package ss.team16.nthulostfound.ui.newitem

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.domain.usecase.NewItemUseCase
import java.util.*

class NewItemViewModel @AssistedInject constructor(
    @Assisted val type: NewItemType,
    private val newItemUseCase: NewItemUseCase
) : ViewModel() {

    @OptIn(ExperimentalPagerApi::class)
    var pagerState by mutableStateOf(PagerState(0))
        private set

    var uploadStatus by mutableStateOf(NewItemUploadStatus.IDLE)
        private set

    var statusInfo by mutableStateOf("")
        private set

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerPrevButtonInfo(): PagerButtonInfo? {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.EDIT -> null
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("返回編輯", true)
            NewItemPageInfo.RESULT -> null
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerNextButtonInfo(): PagerButtonInfo {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.EDIT -> PagerButtonInfo("確認資訊", true)
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("確定送出", true)
            NewItemPageInfo.RESULT -> PagerButtonInfo("完成",
                uploadStatus == NewItemUploadStatus.DONE)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun goToNextPage(scrollToPage: (Int) -> Unit, popScreen: () -> Unit, contentResolver: ContentResolver) {
        val curPage = pagerState.currentPage
        val nextPage = curPage + 1
        if (pagerState.isScrollInProgress)
            return

        when (NewItemPageInfo.fromInt(curPage)) {
            NewItemPageInfo.EDIT -> {
                showFieldErrors = true
                if (!validateFields())
                    return
            }
            NewItemPageInfo.CONFIRM -> {
                submitForm(contentResolver)
            }
            NewItemPageInfo.RESULT -> {
                popScreen()
                return
            }
        }

        scrollToPage(nextPage)
    }

    @OptIn(ExperimentalPagerApi::class)
    fun goToPrevPage(scrollToPage: (Int) -> Unit) {
        val pagePrev = pagerState.currentPage - 1
        if (pagerState.isScrollInProgress || pagePrev < 0)
            return

        scrollToPage(pagePrev)
    }

    fun submitForm(contentResolver: ContentResolver) {

        val cal = Calendar.getInstance()
        cal.set(year, month, day, hour, minute)
        val date = cal.time

        val newItemData = NewItemData(
            type = type,
            name = name,
            description = description.ifBlank { null },
            date = date,
            place = place,
            how = how,
            contact = contact,
            who =
                if (whoEnabled)
                    who
                else
                    null
        )

        uploadStatus = NewItemUploadStatus.UPLOADING_IMAGE
        statusInfo = "圖片上傳中... (0/${imageUris.size})"
        viewModelScope.launch {
            newItemUseCase(newItemData, imageUris, contentResolver,
                onImageUploaded = { index, imageUrl ->
                    Log.d(TAG, "Image uploaded ($index): $imageUrl")
                    statusInfo = "圖片上傳中... (${index + 1}/${imageUris.size})"
                },
                onImageUploadError = { index, exception ->
                    Log.w(TAG, "Image uploaded error ($index): ${exception.message ?: "Unknown error"}")
                    uploadStatus = NewItemUploadStatus.ERROR
                    statusInfo = "圖片上傳失敗！\n${exception.message ?: "未知錯誤"}"
                },
                onImageUploadFinished = {
                    uploadStatus = NewItemUploadStatus.UPLOADING_DATA
                    statusInfo = "資料上傳中..."
                },
                onDataUploaded = {
                    uploadStatus = NewItemUploadStatus.DONE
                },
                onDataUploadError = {
                    uploadStatus = NewItemUploadStatus.ERROR
                    statusInfo = "資料上傳失敗！\n${it.message ?: "未知錯誤"}"
                }
            )
        }
    }

    private val _imageBitmaps = emptyList<Bitmap>().toMutableStateList()
    val imageBitmaps: List<Bitmap>
        get() = _imageBitmaps

    private val imageUris = mutableListOf<Uri>()

    fun onAddImage(uri: Uri?, context: Context) {
        if (uri == null)
            return

        imageUris.add(uri)

        if (Build.VERSION.SDK_INT < 28) {
            _imageBitmaps.add(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            )
        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, uri)
            _imageBitmaps.add(ImageDecoder.decodeBitmap(source))
        }
    }

    fun onDeleteImage(index: Int) {
        _imageBitmaps.removeAt(index)
    }

    private val calendar = Calendar.getInstance()

    var year by mutableStateOf(calendar[Calendar.YEAR])
        private set
    var month by mutableStateOf(calendar[Calendar.MONTH])
        private set
    var day by mutableStateOf(calendar[Calendar.DAY_OF_MONTH])
        private set
    var hour by mutableStateOf(calendar[Calendar.HOUR_OF_DAY])
        private set
    var minute by mutableStateOf(calendar[Calendar.MINUTE])
        private set

    fun onDateChange(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }
    fun onTimeChange(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
    }

    var name by mutableStateOf("")
        private set
    fun onNameChange(value: String) {
        name = value
    }

    var place by mutableStateOf("")
        private set
    fun onPlaceChange(value: String) {
        place = value
    }

    var description by mutableStateOf("")
        private set
    fun onDescriptionChange(value: String) {
        description = value
    }

    var how by mutableStateOf("")
        private set
    fun onHowChange(value: String) {
        how = value
    }

    var contact by mutableStateOf("")
        private set
    fun onContactChange(value: String) {
        contact = value
    }

    var whoEnabled by mutableStateOf(type == NewItemType.NEW_FOUND)
        private set
    fun onWhoEnabledChange(value: Boolean) {
        whoEnabled = value
    }

    var who by mutableStateOf("")
        private set
    fun onWhoChange(value: String) {
        who = value
    }

    var showFieldErrors by mutableStateOf(false)
        private set

    private fun validateFields(): Boolean {
        return name.isNotBlank() &&
                place.isNotBlank() &&
                how.isNotBlank() &&
                contact.isNotBlank() &&
                !(whoEnabled && who.isBlank())
    }







    @AssistedFactory
    interface Factory {
        fun create(type: NewItemType): NewItemViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            type: NewItemType,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(type) as T
            }
        }
    }
}

enum class NewItemPageInfo(val value: Int) {
    EDIT(0),
    CONFIRM(1),
    RESULT(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

data class PagerButtonInfo(
    val label: String,
    val enabled: Boolean
)

enum class NewItemUploadStatus {
    IDLE,
    UPLOADING_IMAGE,
    UPLOADING_DATA,
    DONE,
    ERROR;
}