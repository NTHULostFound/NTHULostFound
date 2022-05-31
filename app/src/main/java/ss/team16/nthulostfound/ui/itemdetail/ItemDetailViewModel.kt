package ss.team16.nthulostfound.ui.itemdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var _showDialog by mutableStateOf(true)
    val showDialog: Boolean
        get() = _showDialog

    private val _item by mutableStateOf(item)
    val item: ItemData
        get() = _item

    fun shareItem() {
        // TODO: share use case
    }

    fun deleteItem() {
        // TODO: delete item use case
    }

    fun endItem() {
        // TODO: end item case
    }

    fun getContact() {
        // TODO: get contact use case
    }

    fun setDialogStatus(status: Boolean) {
        _showDialog = status
    }

}