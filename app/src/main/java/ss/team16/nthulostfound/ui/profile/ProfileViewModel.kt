package ss.team16.nthulostfound.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.usecase.GetUserUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase

class ProfileViewModel @AssistedInject constructor(
    val getUserUseCase: GetUserUseCase,
    val saveUserUseCase: SaveUserUseCase
): ViewModel() {
    private var _user by mutableStateOf(UserData())
    val user: UserData
        get() = _user

    private var _notificationEnabled by mutableStateOf(false)
    val notificationEnabled: Boolean
        get() = _notificationEnabled
    init {
        viewModelScope.launch {
            _user = getUserUseCase()
        }
    }


    fun enableNotification(status: Boolean) {
        _notificationEnabled = status
    }
}

    @AssistedFactory
    interface Factory {
        fun create(): ProfileViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: ProfileViewModel.Factory,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create() as T
            }
        }
    }
}