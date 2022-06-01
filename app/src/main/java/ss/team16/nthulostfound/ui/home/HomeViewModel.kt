package ss.team16.nthulostfound.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ss.team16.nthulostfound.domain.model.ItemData
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
                        "書",
                        "好像是機率的書",
                        Date(),
                        "台達 105",
                        "請聯繫我取回 啾咪",
                        listOf(UploadedImage("https://example.com"))
                    )
                )
            }
            ShowType.FOUND -> {
                return listOf(
                    ItemData(
                        "錢包",
                        "我的錢包不見了QAQ",
                        Date(),
                        "仁齋",
                        ""
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
