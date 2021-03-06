package ss.team16.nthulostfound.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.GetAvatarUseCase
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemsRepository: ItemRepository,
    private val userRepository: UserRepository,
    private val getAvatarUseCase: GetAvatarUseCase
) : ViewModel() {

    val showTypeFlow = MutableStateFlow(ShowType.FOUND)
    val searchFlow = MutableStateFlow<String?>(null)
    val myItemsFlow = MutableStateFlow<Boolean>(false)

    val lazyListState = LazyListState()

    val showPinMessageFlow = userRepository.getShowPinMessage()
    val isUserDataSetFlow = userRepository.getIsUserDataSet()
    val setUserDataPoppedUp = MutableStateFlow(false)
    val canShowPopUpFlow = combine(
            isUserDataSetFlow,
            setUserDataPoppedUp
        ) { isUserDataSet, setUserDataPoppedUp ->
            !(isUserDataSet || setUserDataPoppedUp)
    }

    var avatarBitmap : StateFlow<Bitmap?>? = null
        private set

    init {
        viewModelScope.launch {
            avatarBitmap = getAvatarUseCase().stateIn(viewModelScope)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var items =
        combine(
            showTypeFlow,
            searchFlow,
            myItemsFlow
        ) { showType, search, myItems ->
            ItemsArgs(
                itemType =
                    if (showType == ShowType.FOUND)
                        ItemType.FOUND
                    else
                        ItemType.LOST
                ,
                search = search,
                myItems = myItems
            )
        }.flatMapLatest { args ->
            itemsRepository.getPagingItems(
                type = args.itemType,
                search = args.search,
                myItems = args.myItems
            ).cachedIn(viewModelScope).onCompletion {
                lazyListState.scrollToItem(0)
            }
        }

    var fabState: FabState by mutableStateOf(FabState.WITH_TEXT)
        private set

    fun onPageChanged(showType: ShowType) {
        showTypeFlow.value = showType
    }

    fun onSearch(text: String) {
        if (text.isBlank())
            searchFlow.value = null
        else
            searchFlow.value = text
    }

    fun onPinMessageClose() {
        viewModelScope.launch {
            var code = showPinMessageFlow.first()

            if (showTypeFlow.value == ShowType.FOUND) {
                // set FOUND bit = 0
                code = code and 0b01
            } else if (showTypeFlow.value == ShowType.LOST) {
                // set LOST bit = 0
                code = code and 0b10
            }

            userRepository.setShowPinMessage(code)
        }
    }

    fun onFabClicked() {
        fabState = when(fabState) {
            FabState.COLLAPSED -> FabState.EXTENDED
            FabState.EXTENDED -> FabState.WITH_TEXT
            FabState.WITH_TEXT -> FabState.EXTENDED
        }
    }

    fun onSetUserDataPopUp() {
        setUserDataPoppedUp.value = true
        // use this to ensure the pop up only shows once during the app running
    }
}

enum class ShowType {
    FOUND,
    LOST
}

enum class FabState {
    WITH_TEXT,
    COLLAPSED,
    EXTENDED
}

data class ItemsArgs(
    val itemType: ItemType,
    val search: String?,
    val myItems: Boolean
)
