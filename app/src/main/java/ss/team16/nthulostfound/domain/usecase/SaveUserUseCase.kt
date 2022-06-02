package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserData) {
        return userRepository.saveUser(user)
    }
}