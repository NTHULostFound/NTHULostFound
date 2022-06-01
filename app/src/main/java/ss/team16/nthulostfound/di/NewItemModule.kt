package ss.team16.nthulostfound.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ss.team16.nthulostfound.data.repository.UploadImagesRepositoryImgurImpl
import ss.team16.nthulostfound.data.source.ImgurApi
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository
import ss.team16.nthulostfound.domain.usecase.NewItemUseCase
import ss.team16.nthulostfound.domain.usecase.UploadImagesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewItemModule {

    @Provides
    @Singleton
    fun provideNewItemUseCase(uploadImagesUseCase: UploadImagesUseCase) : NewItemUseCase {
        return NewItemUseCase(uploadImagesUseCase)
    }

}