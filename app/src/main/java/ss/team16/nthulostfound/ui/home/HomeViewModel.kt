package ss.team16.nthulostfound.ui.home

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
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.usecase.GetAvatarUseCase
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemsRepository: ItemRepository,
    private val getAvatarUseCase: GetAvatarUseCase
) : ViewModel() {

    val showTypeFlow = MutableStateFlow(ShowType.FOUND)
    val searchFlow = MutableStateFlow<String?>(null)
    val myItemsFlow = MutableStateFlow<Boolean>(false)

    val avatarBitmap = getAvatarUseCase()

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
            ).cachedIn(viewModelScope)
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

    fun onFabClicked() {
        fabState = when(fabState) {
            FabState.COLLAPSED -> FabState.EXTENDED
            FabState.EXTENDED -> FabState.WITH_TEXT
            FabState.WITH_TEXT -> FabState.EXTENDED
        }
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
