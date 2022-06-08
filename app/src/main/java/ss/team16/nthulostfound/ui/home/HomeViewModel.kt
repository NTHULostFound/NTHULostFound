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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemsRepository: ItemRepository
) : ViewModel() {

    val showTypeFlow = MutableStateFlow(ShowType.FOUND)

    @OptIn(ExperimentalCoroutinesApi::class)
    var items = showTypeFlow.flatMapLatest { type ->
        itemsRepository.getPagingItems(
            type =
            if (type == ShowType.FOUND)
                ItemType.FOUND
            else
                ItemType.LOST
        ).cachedIn(viewModelScope)
    }

    var fabState: FabState by mutableStateOf(FabState.WITH_TEXT)
        private set

    fun onPageChanged(showType: ShowType) {
        showTypeFlow.update { _ -> showType }
    }

    fun onSearch(text: String) {

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

