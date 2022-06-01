package ss.team16.nthulostfound.data.repository

import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class UserRepositoryImpl: UserRepository {
    override suspend fun getUser(): UserData {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: UserData) {
        TODO("Not yet implemented")
    }
}