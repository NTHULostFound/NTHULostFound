package ss.team16.nthulostfound.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ss.team16.nthulostfound.model.UserData

class ProfileViewModel(
    private val savedUser: UserData
): ViewModel() {
    private val _user by mutableStateOf(savedUser)
    val user: UserData
        get() = _user

    private var _notificationEnabled by mutableStateOf(false)
    val notificationEnabled: Boolean
        get() = _notificationEnabled

    fun enableNotification(status: Boolean) {
        _notificationEnabled = status
    }
}

class ProfileViewModelFactory(private val user: UserData = UserData()):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ProfileViewModel(user) as T
}