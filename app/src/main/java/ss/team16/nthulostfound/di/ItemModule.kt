package ss.team16.nthulostfound.di

import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.ItemRepositoryImpl
import ss.team16.nthulostfound.data.repository.ItemRepositoryMockImpl
import ss.team16.nthulostfound.data.source.ItemsDatabase
import ss.team16.nthulostfound.domain.repository.ItemRepository
import ss.team16.nthulostfound.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemModule {

    @Provides
    @Singleton
    fun provideItemsDatabase(
        @ApplicationContext context: Context
    ): ItemsDatabase {
        return Room.databaseBuilder(
            context,
            ItemsDatabase::class.java,
            ItemsDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideItemRepository(apolloClient: ApolloClient): ItemRepository {
        return ItemRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetItemUseCase(itemRepository: ItemRepository): GetItemUseCase {
        return GetItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideEndItemUseCase(itemRepository: ItemRepository): EndItemUseCase {
        return EndItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideNewItemUseCase(
        uploadImagesUseCase: UploadImagesUseCase,
        itemRepository: ItemRepository,
    ) : NewItemUseCase {
        return NewItemUseCase(
            uploadImagesUseCase,
            itemRepository
        )
    }

    @Provides
    @Singleton
    fun provideShareItemUseCase(@ApplicationContext context: Context): ShareItemUseCase {
        return ShareItemUseCase(context)
    }

    @Provides
    @Singleton
    fun provideGetContactUseCase(itemRepository: ItemRepository): GetContactUseCase {
        return GetContactUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideShareResultUseCase(@ApplicationContext context: Context): ShareResultUseCase {
        return ShareResultUseCase(context)
    }
}