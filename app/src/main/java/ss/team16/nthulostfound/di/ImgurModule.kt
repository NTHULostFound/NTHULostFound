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
import ss.team16.nthulostfound.domain.usecase.UploadImagesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImgurModule {

    @Provides
    @Singleton
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ImgurApi.baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideImgurApi(retrofit: Retrofit) : ImgurApi {
        return retrofit.create(ImgurApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUploadImagesRepository(imgurApi: ImgurApi) : UploadImagesRepository {
        return UploadImagesRepositoryImgurImpl(imgurApi)
    }

    @Provides
    @Singleton
    fun provideUploadImagesUseCase(repository: UploadImagesRepository) : UploadImagesUseCase {
        return UploadImagesUseCase(repository)
    }

}