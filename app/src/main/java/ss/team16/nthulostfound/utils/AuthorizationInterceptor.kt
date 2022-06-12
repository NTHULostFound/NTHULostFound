package ss.team16.nthulostfound.utils

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ss.team16.nthulostfound.domain.repository.UserRepository

class AuthorizationInterceptor constructor(
    private val userRepository: UserRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { userRepository.getAccessToken() }
        var builder = chain.request().newBuilder()

        if (token.isNotBlank()) {
            builder = builder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(builder.build())
    }
}