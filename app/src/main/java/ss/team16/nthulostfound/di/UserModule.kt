package ss.team16.nthulostfound.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.UserRepositoryImpl
import ss.team16.nthulostfound.domain.repository.RemoteUserRepository
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.ChangeAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.GetAvatarUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext appContext: Context
    ): UserRepository {
        return UserRepositoryImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideSaveUserUseCase(
        userRepository: UserRepository,
        remoteUserRepository: RemoteUserRepository
    ): SaveUserUseCase {
        return SaveUserUseCase(userRepository, remoteUserRepository)
    }

    @Provides
    @Singleton
    fun provideChangeAvatarUseCase(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): ChangeAvatarUseCase {
        return ChangeAvatarUseCase(context, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetAvatarUseCase(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): GetAvatarUseCase {
        return GetAvatarUseCase(context, userRepository)
    }

}