package ss.team16.nthulostfound.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ss.team16.nthulostfound.data.repository.RemoteUserRepositoryImpl
import ss.team16.nthulostfound.domain.repository.RemoteUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteUserModule {

    @Provides
    @Singleton
    fun provideRemoteUserRepository(
        apolloClient: ApolloClient
    ): RemoteUserRepository {
        return RemoteUserRepositoryImpl(apolloClient)
    }

}