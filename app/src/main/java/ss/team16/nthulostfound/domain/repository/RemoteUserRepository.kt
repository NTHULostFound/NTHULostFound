package ss.team16.nthulostfound.domain.repository

import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.UserData

interface RemoteUserRepository {

    // Returns access token
    suspend fun registerFCMToken(token: String): Result<String>

    suspend fun updateUserData(user: UserData): Result<Boolean>
}