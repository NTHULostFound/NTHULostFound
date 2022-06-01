package ss.team16.nthulostfound.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.ItemRepositoryMockImpl
import ss.team16.nthulostfound.domain.repository.ItemRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemRepositoryModule {

    @Provides
    @Singleton
    fun provideItemRepository(): ItemRepository {
        return ItemRepositoryMockImpl()
    }

}