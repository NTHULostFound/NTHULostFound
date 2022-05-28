package ss.team16.nthulostfound.ui.newitem

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.model.NewItemData
import ss.team16.nthulostfound.model.NewItemType

class NewItemViewModel(type: NewItemType, val popScreen: () -> Unit) : ViewModel() {
    private val _newItemData by mutableStateOf(NewItemData(type))
    val newItemData: NewItemData
        get() = _newItemData

    @OptIn(ExperimentalPagerApi::class)
    var pagerState by mutableStateOf(PagerState(0))
        private set

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerPrevButtonInfo(): PagerButtonInfo? {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.ENTER -> null
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("返回編輯", true)
            NewItemPageInfo.SENDING -> PagerButtonInfo("返回", false)
            NewItemPageInfo.DONE -> null
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun getPagerNextButtonInfo(): PagerButtonInfo? {
        return when (NewItemPageInfo.fromInt(pagerState.currentPage)) {
            NewItemPageInfo.ENTER -> PagerButtonInfo("確認資訊", true)
            NewItemPageInfo.CONFIRM -> PagerButtonInfo("確定送出", true)
            NewItemPageInfo.SENDING -> null
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

}

class NewItemViewModelFactory(private val type: NewItemType, private val popScreen: () -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewItemViewModel(type, popScreen) as T
}

enum class NewItemPageInfo(val value: Int) {
    ENTER(0),
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