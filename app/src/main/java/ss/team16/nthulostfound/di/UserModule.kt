package ss.team16.nthulostfound.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.UserRepositoryImpl
import ss.team16.nthulostfound.data.repository.UserRepositoryMockImpl
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.GetUserUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext appContext: Context): UserRepository {
        return UserRepositoryImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveUserUseCase(userRepository: UserRepository): SaveUserUseCase {
        return SaveUserUseCase(userRepository)
    }
}