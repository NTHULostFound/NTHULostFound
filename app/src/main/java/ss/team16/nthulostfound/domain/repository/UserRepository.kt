package ss.team16.nthulostfound.domain.repository

import ss.team16.nthulostfound.domain.model.UserData

interface UserRepository {
    suspend fun getUser(): UserData

    suspend fun saveUser(user: UserData)
}