package ss.team16.nthulostfound.ui.profile

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private var _user by mutableStateOf(UserData())
    val user: UserData
        get() = _user

    val isNotificationEnable = userRepository.getIsNotificationEnable()

    private var _hasChangedTextFieldValue by mutableStateOf(false)
    val hasChangedTextFieldValue: Boolean
        get() = _hasChangedTextFieldValue

    init {
        viewModelScope.launch {
            _user = userRepository.getUser()
        }
    }

    fun onTextFieldChange(field: String, value: String) {
        _hasChangedTextFieldValue = true

        _user = when (field) {
            "name" -> user.copy(name = value)
            "studentId" -> user.copy(studentId = value)
            "email" -> user.copy(email = value)
            else -> user.copy()
        }
    }

    fun setEnableNotification(status: Boolean) {
        viewModelScope.launch {
            userRepository.setIsNotificationEnable(status)
        }
    }

    fun saveUser() {
        _hasChangedTextFieldValue = false

        viewModelScope.launch {
            userRepository.saveUser(_user.copy())
        }
    }

    fun resetUser() {
        _hasChangedTextFieldValue = false

        viewModelScope.launch {
            _user = userRepository.getUser()
        }
    }

}