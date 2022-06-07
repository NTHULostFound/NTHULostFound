package ss.team16.nthulostfound.domain.repository

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.UserData

interface UserRepository {
    suspend fun getUser(): UserData

    suspend fun saveUser(user: UserData)

    fun getIsNotificationEnable(): Flow<Boolean>

    suspend fun setIsNotificationEnable(status: Boolean)

    suspend fun getAccessToken(): String

    suspend fun saveAccessToken(token: String)
}