package ss.team16.nthulostfound.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.usecase.GetUserUseCase
import ss.team16.nthulostfound.utils.AuthorizationInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {
    private val BASE_URL = "https://nthu-lost-found.yikuo.dev/graphql/"

    @Provides
    fun provideAuthorizationInterceptor(getUserUseCase: GetUserUseCase): AuthorizationInterceptor {
        return AuthorizationInterceptor(getUserUseCase)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }
}