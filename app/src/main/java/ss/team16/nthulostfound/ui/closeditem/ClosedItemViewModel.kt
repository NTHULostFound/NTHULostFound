package ss.team16.nthulostfound.ui.closeditem

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.usecase.ShareResultUseCase
import javax.inject.Inject

@HiltViewModel
class ClosedItemViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    val shareResultUseCase: ShareResultUseCase
): ViewModel() {
    val type = when (stateHandle.get<String>("itemType")!!) {
        "found" -> ItemType.FOUND
        "lost" -> ItemType.LOST
        else -> ItemType.FOUND
    }
    val name = stateHandle.get<String>("itemName")!!

    fun shareResult() {
        viewModelScope.launch {
            shareResultUseCase(type, name)
        }
    }
}