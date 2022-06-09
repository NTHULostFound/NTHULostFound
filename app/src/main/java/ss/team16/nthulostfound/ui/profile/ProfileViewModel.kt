package ss.team16.nthulostfound.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import ss.team16.nthulostfound.domain.usecase.ChangeAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.GetAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val saveUserUseCase: SaveUserUseCase,
    private val changeAvatarUseCase: ChangeAvatarUseCase,
    getAvatarUseCase: GetAvatarUseCase
): ViewModel() {
    private var _user by mutableStateOf(UserData())
    val user: UserData
        get() = _user

    val isNotificationEnable = userRepository.getIsNotificationEnable()
    val avatarBitmap = getAvatarUseCase()

    private var _hasChangedTextFieldValue by mutableStateOf(false)
    val hasChangedTextFieldValue: Boolean
        get() = _hasChangedTextFieldValue

    private var _submitDisabled by mutableStateOf(false)
    val submitDisabled: Boolean
        get() = _submitDisabled

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
        // disable submit buttons while waiting API response
        _submitDisabled = true

        viewModelScope.launch {
            // call API
            saveUserUseCase(_user.copy())

            // if done, make submit button invisible
            _submitDisabled = false
            _hasChangedTextFieldValue = false
        }
    }

    fun resetUser() {
        _hasChangedTextFieldValue = false

        viewModelScope.launch {
            _user = userRepository.getUser()
        }
    }

    fun resetPinMessage() {
        viewModelScope.launch {
            userRepository.setShowPinMessage(0b11)
        }
    }

    fun onChangeAvatar(uri: Uri?) {
        if (uri == null)
            return

        viewModelScope.launch {
            changeAvatarUseCase(uri)
        }
    }

}