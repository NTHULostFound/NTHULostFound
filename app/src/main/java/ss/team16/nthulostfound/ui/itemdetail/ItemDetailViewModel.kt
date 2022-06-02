package ss.team16.nthulostfound.ui.itemdetail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.usecase.GetItemUseCase
import ss.team16.nthulostfound.domain.usecase.ShareItemUseCase

enum class ViewMode {
    Owner,
    Guest
}

class ItemDetailViewModel @AssistedInject constructor(
    @Assisted uuid: String,
    private val getItemUseCase: GetItemUseCase,
    private val shareItemUseCase: ShareItemUseCase
): ViewModel() {
    private var _viewMode by mutableStateOf(ViewMode.Guest)
    val viewMode: ViewMode
        get() = _viewMode

    private var _showDialog by mutableStateOf(false)
    val showDialog: Boolean
        get() = _showDialog

    private var _item by mutableStateOf(ItemData())
    val item: ItemData
        get() = _item

    init {
        viewModelScope.launch {
            _item = getItemUseCase(uuid)!!
            _viewMode = if (item.isOwner) ViewMode.Owner else ViewMode.Guest
        }
    }

    fun shareItem(context: Context) {
        viewModelScope.launch {
            shareItemUseCase(context, item)
        }
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

    @AssistedFactory
    interface Factory {
        fun create(uuid: String): ItemDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            uuid: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(uuid) as T
            }
        }
    }
}