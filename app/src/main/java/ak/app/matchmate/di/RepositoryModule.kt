package ak.app.matchmate.di

import ak.app.matchmate.data.local.dao.UserDao
import ak.app.matchmate.data.remote.RandomUserApiService
import ak.app.matchmate.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: RandomUserApiService,
        userDao: UserDao
    ): UserRepository {
        return UserRepository(apiService, userDao)
    }
}