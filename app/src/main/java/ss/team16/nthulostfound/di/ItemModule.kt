package ss.team16.nthulostfound.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.usecase.GetItemUseCase
import ss.team16.nthulostfound.domain.usecase.ShareItemUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemModule {
    @Provides
    @Singleton
    fun provideGetItemUseCase(itemRepository: ItemRepository): GetItemUseCase {
        return GetItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideShareItemUseCase(): ShareItemUseCase {
        return ShareItemUseCase()
    }
}