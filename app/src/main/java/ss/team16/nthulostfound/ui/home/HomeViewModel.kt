package ss.team16.nthulostfound.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.UploadedImage
import java.util.*

enum class ShowType {
    FOUND,
    LOST
}

enum class FabState {
    WITH_TEXT,
    COLLAPSED,
    EXTENDED
}

class HomeViewModel @AssistedInject constructor(
    @Assisted showType: ShowType
) : ViewModel() {
    private var _showType by mutableStateOf(showType)
    val showType: ShowType
        get() = _showType
    private var _items by mutableStateOf(getItems(showType))
    val items: List<ItemData>
        get() = _items
    var fabState: FabState by mutableStateOf(FabState.WITH_TEXT)

    private fun getItems(showType: ShowType) : List<ItemData> {
        when(showType) {
            ShowType.LOST -> {
                return List(10) {
                    ItemData(
                        type = ItemType.LOST,
                        uuid = "C8763",
                        name = "書",
                        description = "好像是機率的書",
                        date = Date(),
                        place = "台達 105",
                        how = "請聯繫我取回 啾咪",
                        images = listOf("https://example.com")
                    )
                }
            }
            ShowType.FOUND -> {
                return listOf(
                    ItemData(
                        type = ItemType.FOUND,
                        uuid = "C8764",
                        name = "錢包",
                        description = "我的錢包不見了QAQ",
                        date = Date(),
                        place = "仁齋",
                        how = ""
                    ),
                )
            }
        }
    }

    fun onShowTypeChanged() {
        _showType = if(_showType == ShowType.FOUND) ShowType.LOST else ShowType.FOUND
        _items = getItems(showType)
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

    @AssistedFactory
    interface Factory {
        fun create(showType: ShowType): HomeViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            showType: ShowType
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(showType) as T
            }
        }
    }


}
