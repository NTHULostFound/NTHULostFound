package ss.team16.nthulostfound.domain.repository

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.UserData

interface UserRepository {
    suspend fun getUser(): UserData

    suspend fun saveUser(user: UserData)

    fun getIsNotificationEnable(): Flow<Boolean>

    fun getIsUserDataSet(): Flow<Boolean>

    suspend fun setIsNotificationEnable(status: Boolean)

    suspend fun getAccessToken(): String

    suspend fun saveAccessToken(token: String)

    fun getAvatarFilename(): Flow<String?>

    suspend fun setAvatarFilename(filename: String)

    fun getShowPinMessage(): Flow<Int>

    suspend fun setShowPinMessage(code: Int)

    fun getIsReviewAsked(): Flow<Boolean>

    suspend fun setIsReviewAsked(value: Boolean)
}