package ss.team16.nthulostfound.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ss.team16.nthulostfound.domain.model.UserData

class AuthorizationInterceptor(val context: Context, val user: UserData): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", user.accessToken ?: "")
            .build()

        return chain.proceed(request)
    }
}