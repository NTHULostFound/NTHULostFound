package ss.team16.nthulostfound.ui.closeditem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.GetFeedbackUseCase
import ss.team16.nthulostfound.domain.usecase.ShareResultUseCase
import javax.inject.Inject

@HiltViewModel
class ClosedItemViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    val shareResultUseCase: ShareResultUseCase,
    val userRepository: UserRepository,
    val getFeedbackUseCase: GetFeedbackUseCase
): ViewModel() {
    val type = when (stateHandle.get<String>("itemType")!!) {
        "found" -> ItemType.FOUND
        "lost" -> ItemType.LOST
        else -> ItemType.FOUND
    }
    val name = stateHandle.get<String>("itemName")!!
    var askReviewState: AskReviewState by mutableStateOf(AskReviewState.FEEL)
        private set
    var askReviewTitle by mutableStateOf("很高興您嘗試了我們的 App！")
    var askReviewMessage by mutableStateOf("您目前對我們的 App 使用上滿意嗎？")
    var askReviewConfirm by mutableStateOf("滿意")
    var askReviewDismiss by mutableStateOf("不滿意")
    var isReviewAsked = userRepository.getIsReviewAsked()

    fun shareResult() {
        viewModelScope.launch {
            shareResultUseCase(type, name)
        }
    }

    fun setReviewState(askReviewState: AskReviewState) {
        this.askReviewState = askReviewState
        setReviewText()
    }

    private fun setReviewText() {
        when(askReviewState) {
            AskReviewState.FEEL -> {
                askReviewTitle = "很高興您嘗試了我們的 App！"
                askReviewMessage = "您目前對我們的 App 使用上滿意嗎？"
                askReviewConfirm = "滿意"
                askReviewDismiss = "不滿意"
            }
            AskReviewState.GOOD -> {
                askReviewTitle = "感謝您的支持！"
                askReviewMessage = "您願意花點時間幫我們在 Play 商店留下評價嗎？"
                askReviewConfirm = "好"
                askReviewDismiss = "下次再說"
            }
            AskReviewState.BAD -> {
                askReviewTitle = "很抱歉！"
                askReviewMessage = "您願意與我們分享我們可以改進的地方嗎？"
                askReviewConfirm = "好"
                askReviewDismiss = "下次再說"
            }
        }
    }

    fun setReviewAsked(value: Boolean = true) {
        viewModelScope.launch {
            userRepository.setIsReviewAsked(value)
        }
    }
}

enum class AskReviewState {
    FEEL,
    GOOD,
    BAD
}