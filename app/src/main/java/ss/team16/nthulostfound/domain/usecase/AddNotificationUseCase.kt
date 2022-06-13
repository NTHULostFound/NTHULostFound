package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.repository.NotificationRepository

class AddNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notif: NotificationData): Long {
        return repository.addNotification(notif)
    }
}