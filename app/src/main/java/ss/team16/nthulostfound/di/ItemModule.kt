package ss.team16.nthulostfound.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.ItemRepositoryMockImpl
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemModule {

    @Provides
    @Singleton
    fun provideItemRepository(): ItemRepository {
        return ItemRepositoryMockImpl()
    }

    @Provides
    @Singleton
    fun provideGetItemUseCase(itemRepository: ItemRepository): GetItemUseCase {
        return GetItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideGetItemsUseCase(itemRepository: ItemRepository): GetItemsUseCase {
        return GetItemsUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideEndItemUseCase(itemRepository: ItemRepository): EndItemUseCase {
        return EndItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideNewItemUseCase(uploadImagesUseCase: UploadImagesUseCase) : NewItemUseCase {
        return NewItemUseCase(uploadImagesUseCase)
    }

    @Provides
    @Singleton
    fun provideShareItemUseCase(): ShareItemUseCase {
        return ShareItemUseCase()
    }

    @Provides
    @Singleton
    fun provideGetContactUseCase(itemRepository: ItemRepository): GetContactUseCase {
        return GetContactUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideShareResultUseCase(): ShareResultUseCase {
        return ShareResultUseCase()
    }
}