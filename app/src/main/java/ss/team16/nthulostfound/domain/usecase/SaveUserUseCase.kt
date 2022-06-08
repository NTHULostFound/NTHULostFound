package ss.team16.nthulostfound.domain.usecase

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import ss.team16.nthulostfound.UpdateUserDataMutation
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class SaveUserUseCase (
    private val userRepository: UserRepository,
    private val apolloClient: ApolloClient
) {
    suspend operator fun invoke(user: UserData) {
        try {
            val updateUserDataMutation = UpdateUserDataMutation(
                Optional.presentIfNotNull(user.name),
                Optional.presentIfNotNull(user.studentId),
                Optional.presentIfNotNull(user.email)
            )
            val response = apolloClient.mutation(updateUserDataMutation).execute()
            assert(!response.hasErrors())

            // send request first, so if the API call failed, we won't save data the local datastore
            userRepository.saveUser(user)
        } catch (e: Exception) {
            Log.e("UserUseCase", "Error occurred while saving user", e)
        }
    }
}