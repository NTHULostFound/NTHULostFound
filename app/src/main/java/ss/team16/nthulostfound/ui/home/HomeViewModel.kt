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
    @Assisted val showType: ShowType
) : ViewModel() {
    private var _items = getItems(showType).toMutableStateList()
    val items: List<ItemData>
        get() = _items
    var fabState: FabState by mutableStateOf(FabState.WITH_TEXT)

    private fun getItems(showType: ShowType) : List<ItemData> {
        when(showType) {
            ShowType.LOST -> {
                return listOf(
                    ItemData(
                        type = ItemType.LOST,
                        name = "書",
                        description = "好像是機率的書",
                        date = Date(),
                        place = "台達 105",
                        how = "請聯繫我取回 啾咪",
                        images = listOf("https://example.com")
                    )
                )
            }
            ShowType.FOUND -> {
                return listOf(
                    ItemData(
                        type = ItemType.FOUND,
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

    fun onShowTypeChanged(showType: ShowType) {
        _items = getItems(showType).toMutableStateList()
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
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(showType) as T
            }
        }
    }


}
