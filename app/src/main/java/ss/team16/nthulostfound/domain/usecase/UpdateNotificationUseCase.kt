package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.repository.NotificationRepository

class UpdateNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(data: NotificationData) {
        return repository.updateNotification(data)
    }
}