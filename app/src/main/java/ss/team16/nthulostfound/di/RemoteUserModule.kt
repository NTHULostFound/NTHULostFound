package ss.team16.nthulostfound.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.RemoteUserRepositoryImpl
import ss.team16.nthulostfound.data.repository.UserRepositoryImpl
import ss.team16.nthulostfound.domain.repository.RemoteUserRepository
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.ChangeAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.GetAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteUserModule {

    @Provides
    @Singleton
    fun provideRemoteUserRepository(
        apolloClient: ApolloClient
    ): RemoteUserRepository {
        return RemoteUserRepositoryImpl(apolloClient)
    }

}