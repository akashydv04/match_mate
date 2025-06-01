package ak.app.matchmate.data.local.dao

import ak.app.matchmate.data.local.model.UserEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE uuid = :uuid")
    suspend fun getUserByUuid(uuid: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE status = :status")
    fun getUsersWithStatus(status: String): Flow<List<UserEntity>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}