package ss.team16.nthulostfound.ui.notification

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.usecase.GetNotificationUseCase
import ss.team16.nthulostfound.domain.usecase.UpdateNotificationUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
): ViewModel() {
    private var _notifs = emptyList<NotificationData>().toMutableStateList()
    val notifs: List<NotificationData>
        get() = _notifs

    init {
        getNotification()
    }

    private fun getNotification() {
        viewModelScope.launch {
            getNotificationUseCase().collect {
                _notifs = it.toMutableStateList()
            }
        }
    }

    fun updateNotification(notif: NotificationData) {
        viewModelScope.launch {
            updateNotificationUseCase(notif)
        }
    }
}