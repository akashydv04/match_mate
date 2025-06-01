package ak.app.matchmate.data.local

import ak.app.matchmate.data.local.dao.UserDao
import ak.app.matchmate.data.local.model.UserEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class MatchMateDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}