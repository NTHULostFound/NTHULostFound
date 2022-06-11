package ss.team16.nthulostfound.ui.itemdetail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.usecase.*

enum class ViewMode {
    Owner,
    Guest
}

sealed class DialogState(
    open val title: String = "",
    open val text: String = ""
) {
    object Disabled: DialogState()
    object AskEnd: DialogState(title = "您確定要結案嗎？", text = "結案後將無法復原！")
    object AskDelete: DialogState(title = "您確定要刪除嗎？", text = "刪除後將無法復原！")
    data class ShowContact(override val text: String): DialogState(title = "聯絡資訊")
}

class ItemDetailViewModel @AssistedInject constructor(
    @Assisted val uuid: String,
    @Assisted val navigateToRoute: (String) -> Unit,
    @ApplicationContext private val context: Context,
    private val getItemUseCase: GetItemUseCase,
    private val endItemUseCase: EndItemUseCase,
    private val shareItemUseCase: ShareItemUseCase,
    private val getContactUseCase: GetContactUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
): ViewModel() {
    private var _viewMode by mutableStateOf(ViewMode.Guest)
    val viewMode: ViewMode
        get() = _viewMode

    // we need to specify the type here, or the type would be "DialogState.Disabled"
    private var _dialogState by mutableStateOf<DialogState>(DialogState.Disabled)
    val dialogState: DialogState
        get() = _dialogState

    private var _item by mutableStateOf(ItemData())
    val item: ItemData
        get() = _item

    // prevent someone crazily clicking get contact, which would bombard the item owner by notification
    private var _contactInfoCache by mutableStateOf("")

    init {
        viewModelScope.launch {
            _item = getItemUseCase(uuid).getOrDefault(ItemData())
            _viewMode = if (item.isOwner) ViewMode.Owner else ViewMode.Guest
        }
    }

    fun shareItem() {
        viewModelScope.launch {
            shareItemUseCase(item)
        }
    }

    fun askEndItem() {
        _dialogState = DialogState.AskEnd
    }

    fun askDeleteItem() {
        _dialogState = DialogState.AskDelete
    }

    private fun endItem() {
        viewModelScope.launch {
            val itemTypeString = when (item.type) {
                ItemType.FOUND -> "found"
                ItemType.LOST -> "lost"
            }
            endItemUseCase(item)
            navigateToRoute("closed_item?itemType=$itemTypeString&itemName=${item.name}")
        }
    }

    private fun deleteItem() {
        viewModelScope.launch {
            deleteItemUseCase(item)

            Toast.makeText(
                context,
                "刪除成功！",
                Toast.LENGTH_SHORT
            ).show()
            navigateToRoute("home")
        }
    }

    fun getContact() {
        viewModelScope.launch {
            var contactInfo = _contactInfoCache

            // if contactInfo is never fetched, fetch it and save it in cache
            // otherwise, it would show contact info from cache, and won't send request to server
            // therefore, it won't cause new notification before user leaves this screen
            if (_contactInfoCache.isNullOrBlank()) {
                 contactInfo = getContactUseCase(item.uuid).getOrDefault("無法取得")
                _contactInfoCache = contactInfo
            }

            _dialogState = DialogState.ShowContact(text = contactInfo)
        }
    }

    fun onDialogDismiss() {
        _dialogState = DialogState.Disabled
    }

    fun onDialogConfirm() {
        if (dialogState is DialogState.AskEnd)
            endItem()
        else if (dialogState is DialogState.AskDelete)
            deleteItem()
        _dialogState = DialogState.Disabled
    }

    @AssistedFactory
    interface Factory {
        fun create(uuid: String, navigateToRoute: (String) -> Unit): ItemDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            uuid: String,
            navigateToRoute: (String) -> Unit
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(uuid, navigateToRoute) as T
            }
        }
    }
}