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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.domain.usecase.UploadImagesUseCase
import java.util.*
import javax.inject.Inject

class NewItemViewModel @AssistedInject constructor(
    @Assisted val type: NewItemType,
    @Assisted private val popScreen: () -> Unit,
    private val uploadImagesUseCase: UploadImagesUseCase
) : ViewModel() {

    @OptIn(ExperimentalPagerApi::class)
    var pagerState by mutableStateOf(PagerState(0))
        private set

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerPrevButtonInfo(): PagerButtonInfo? {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.EDIT -> null
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("返回編輯", true)
            NewItemPageInfo.SENDING -> null
            NewItemPageInfo.DONE -> null
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerNextButtonInfo(): PagerButtonInfo? {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.EDIT -> PagerButtonInfo("確認資訊", true)
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("確定送出", true)
            NewItemPageInfo.SENDING -> PagerButtonInfo("完成", false)
            NewItemPageInfo.DONE -> PagerButtonInfo("完成", true)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun goToNextPage(scrollToPage: (Int) -> Unit, contentResolver: ContentResolver) {
        if (pagerState.currentPage == NewItemPageInfo.DONE.value) {
            popScreen()
        } else {
            val curPage = pagerState.currentPage
            val nextPage = curPage + 1
            if (pagerState.isScrollInProgress || nextPage >= pagerState.pageCount)
                return

            when (curPage) {
                NewItemPageInfo.EDIT.value -> {
                    showFieldErrors = true
                    if (!validateFields())
                        return
                }
                NewItemPageInfo.CONFIRM.value -> {
                    doWork(contentResolver) { scrollToPage(NewItemPageInfo.DONE.value) }
                }
                else -> {}
            }

            scrollToPage(nextPage)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun goToPrevPage(scrollToPage: (Int) -> Unit) {
        val pagePrev = pagerState.currentPage - 1
        if (pagerState.isScrollInProgress || pagePrev < 0)
            return

        scrollToPage(pagePrev)
    }

    private fun doWork(contentResolver: ContentResolver, doneCallback: () -> Unit) {
        viewModelScope.launch {
            uploadImagesUseCase(imageUris, contentResolver,
                onImageUploaded = { index, imageUrl ->
                    Log.d(TAG, "Image uploaded ($index): $imageUrl")
                },
                onError = { index, message ->
                    Log.d(TAG, "Image uploaded error ($index): ${message ?: "Unknown error"}")
                }
            )
            doneCallback()
        }
    }

    private val _imageBitmaps = emptyList<Bitmap>().toMutableStateList()
    val imageBitmaps: List<Bitmap>
        get() = _imageBitmaps

    private val _imageUris = emptyList<Uri>().toMutableStateList()
    val imageUris: List<Uri>
        get() = _imageUris

    fun onAddImage(uri: Uri?, context: Context) {
        if (uri == null)
            return

        _imageUris.add(uri)

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
        fun create(type: NewItemType, popScreen: () -> Unit): NewItemViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            type: NewItemType,
            popScreen: () -> Unit
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(type, popScreen) as T
            }
        }
    }
}

enum class NewItemPageInfo(val value: Int) {
    EDIT(0),
    CONFIRM(1),
    SENDING(2),
    DONE(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

data class PagerButtonInfo(
    val label: String,
    val enabled: Boolean
)