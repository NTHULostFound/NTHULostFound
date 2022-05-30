package ss.team16.nthulostfound

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.UploadImagesRepositoryImgurImpl
import ss.team16.nthulostfound.data.source.ImgurApi
import ss.team16.nthulostfound.domain.repository.UploadImagesRepository
import ss.team16.nthulostfound.domain.usecase.UploadImagesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImgurApi() : ImgurApi {
        return ImgurApi()
    }

    @Provides
    @Singleton
    fun provideUploadImagesRepository(imgurApi: ImgurApi) : UploadImagesRepository {
        return UploadImagesRepositoryImgurImpl(
            imgurApi
        )
    }

    @Provides
    @Singleton
    fun provideUploadImagesUseCase(repository: UploadImagesRepository) : UploadImagesUseCase {
        return UploadImagesUseCase(
            repository
        )
    }

}