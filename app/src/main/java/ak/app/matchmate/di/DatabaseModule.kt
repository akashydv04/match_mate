package ak.app.matchmate.di

import ak.app.matchmate.data.local.MatchMateDatabase
import ak.app.matchmate.data.local.dao.UserDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MatchMateDatabase {
        return Room.databaseBuilder(
            context,
            MatchMateDatabase::class.java,
            "match_mate_db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MatchMateDatabase): UserDao {
        return database.userDao()
    }
}