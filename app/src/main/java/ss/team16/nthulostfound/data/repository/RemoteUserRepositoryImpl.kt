package ss.team16.nthulostfound.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import ss.team16.nthulostfound.RegisterFCMTokenMutation
import ss.team16.nthulostfound.UpdateUserDataMutation
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.RemoteUserRepository

class RemoteUserRepositoryImpl(
    private val apolloClient: ApolloClient
) : RemoteUserRepository {

    override suspend fun registerFCMToken(token: String): Result<String> {
        return try {
            val response = apolloClient.mutation(
                RegisterFCMTokenMutation(token)
            ).execute().dataAssertNoErrors

            val accessToken = response.registerFCMToken.accessToken
            Result.success(accessToken)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

    override suspend fun updateUserData(user: UserData): Result<Boolean> {
        return try {
            val updateUserDataMutation = UpdateUserDataMutation(
                Optional.presentIfNotNull(user.name),
                Optional.presentIfNotNull(user.studentId),
                Optional.presentIfNotNull(user.email)
            )
            apolloClient.mutation(updateUserDataMutation).execute().dataAssertNoErrors

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

}