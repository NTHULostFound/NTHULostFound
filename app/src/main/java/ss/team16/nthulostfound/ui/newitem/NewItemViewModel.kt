package ss.team16.nthulostfound.ui.newitem

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.model.NewItemData
import ss.team16.nthulostfound.model.NewItemType

class NewItemViewModel(val type: NewItemType, val popScreen: () -> Unit) : ViewModel() {

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
    fun goToNextPage(scrollToPage: (Int) -> Unit) {
        if (pagerState.currentPage == NewItemPageInfo.DONE.value) {
            popScreen()
        } else {
            val pageNext = pagerState.currentPage + 1
            if (pagerState.isScrollInProgress || pageNext >= pagerState.pageCount)
                return

            scrollToPage(pageNext)

            when (pageNext) {
                NewItemPageInfo.SENDING.value -> {
                    doWork { scrollToPage(NewItemPageInfo.DONE.value) }
                }
                else -> {}
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun goToPrevPage(scrollToPage: (Int) -> Unit) {
        val pagePrev = pagerState.currentPage - 1
        if (pagerState.isScrollInProgress || pagePrev < 0)
            return

        scrollToPage(pagePrev)
    }

    private fun doWork(doneCallback: () -> Unit) {
        viewModelScope.launch {
            delay(1000L)
            doneCallback()
        }
    }

    private val _imageBitmaps = emptyList<Bitmap>().toMutableStateList()
    val imageBitmaps: List<Bitmap>
        get() = _imageBitmaps

    fun onAddImage(uri: Uri?, context: Context) {
        if (uri == null)
            return

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
}

class NewItemViewModelFactory(private val type: NewItemType, private val popScreen: () -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewItemViewModel(type, popScreen) as T
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