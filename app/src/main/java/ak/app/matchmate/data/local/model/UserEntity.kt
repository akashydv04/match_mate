package ak.app.matchmate.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uuid: String, // Unique identifier from the API (login.uuid)
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
    val imageUrl: String, // Large image URL
    val matchScore: Int,
    val status: String, // "PENDING", "ACCEPTED", "DECLINED"
    val education: String?, // Our custom field
    val religion: String? // Our custom field
)