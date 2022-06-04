package ss.team16.nthulostfound.domain.usecase

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.repository.NotificationRepository

class GetNotificationUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<NotificationData>> {
        return repository.getNotifications()
    }
}