package ss.team16.nthulostfound.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.NotificationRepositoryImpl
import ss.team16.nthulostfound.data.repository.NotificationRepositoryMockImpl
import ss.team16.nthulostfound.data.source.NotificationDatabase
import ss.team16.nthulostfound.domain.repository.NotificationRepository
import ss.team16.nthulostfound.domain.repository.UserRepository
import ss.team16.nthulostfound.domain.usecase.AddNotificationUseCase
import ss.team16.nthulostfound.domain.usecase.GetNotificationUseCase
import ss.team16.nthulostfound.domain.usecase.SaveUserUseCase
import ss.team16.nthulostfound.domain.usecase.UpdateNotificationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): NotificationDatabase {
        return Room.databaseBuilder(
            app,
            NotificationDatabase::class.java,
            NotificationDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NotificationDatabase): NotificationRepository {
        return NotificationRepositoryImpl(db.dao)
//        return NotificationRepositoryMockImpl()
    }

    @Provides
    @Singleton
    fun provideGetNotificationUseCase(repository: NotificationRepository): GetNotificationUseCase {
        return GetNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateNotificationUseCase(repository: NotificationRepository): UpdateNotificationUseCase {
        return UpdateNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddNotificationUseCase(repository: NotificationRepository): AddNotificationUseCase {
        return AddNotificationUseCase(repository)
    }
}