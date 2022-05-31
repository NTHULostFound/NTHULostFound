package ss.team16.nthulostfound.ui.itemdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ss.team16.nthulostfound.domain.model.ItemData

enum class ViewMode {
    Owner,
    Guest
}

class ItemDetailViewModel(
    viewMode: ViewMode,
    item: ItemData
): ViewModel() {
    private val _viewMode by mutableStateOf(viewMode)
    val viewMode: ViewMode
        get() = _viewMode

    private val _item by mutableStateOf(item)
    val item: ItemData
        get() = _item

    fun shareItem() {
        // TODO: share use case
    }

    fun getContact() {
        // TODO: get contact use case
    }
}