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
import ss.team16.nthulostfound.domain.usecase.EndItemUseCase
import ss.team16.nthulostfound.domain.usecase.GetContactUseCase
import ss.team16.nthulostfound.domain.usecase.GetItemUseCase
import ss.team16.nthulostfound.domain.usecase.ShareItemUseCase

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
    data class ShowContact(override val text: String): DialogState(title = "聯絡資訊")
}

class ItemDetailViewModel @AssistedInject constructor(
    @Assisted uuid: String,
    private val getItemUseCase: GetItemUseCase,
    private val endItemUseCase: EndItemUseCase,
    private val shareItemUseCase: ShareItemUseCase,
    private val getContactUseCase: GetContactUseCase
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

    fun askEndItem() {
        _dialogState = DialogState.AskEnd
    }

    fun endItem() {
        viewModelScope.launch {
            endItemUseCase(item)

        }
    }

    fun getContact() {
        viewModelScope.launch {
            val contactInfo = getContactUseCase(item.uuid)
            _dialogState = DialogState.ShowContact(text = contactInfo)
        }
    }

    fun onDialogDismiss() {
        _dialogState = DialogState.Disabled
    }

    fun onDialogConfirm() {
        if (dialogState is DialogState.AskEnd) endItem()
        _dialogState = DialogState.Disabled
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