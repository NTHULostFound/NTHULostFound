package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserData {
        return userRepository.getUser()
    }
}