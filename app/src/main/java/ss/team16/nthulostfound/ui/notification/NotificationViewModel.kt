package ss.team16.nthulostfound.ui.notification

import androidx.compose.runtime.snapshots.SnapshotStateList
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
    private lateinit var _notifs: SnapshotStateList<NotificationData>
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
}