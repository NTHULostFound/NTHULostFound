package ss.team16.nthulostfound.domain.usecase

import android.util.Log
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.RemoteUserRepository
import ss.team16.nthulostfound.domain.repository.UserRepository

class SaveUserUseCase (
    private val userRepository: UserRepository,
    private val remoteUserRepository: RemoteUserRepository
) {
    suspend operator fun invoke(user: UserData) {
        val result = remoteUserRepository.updateUserData(user)
        result.fold(
            onSuccess = {
                userRepository.saveUser(user)
            },
            onFailure = {
                Log.e("UserUseCase", "Error occurred while saving user", it)
            }
        )
    }
}