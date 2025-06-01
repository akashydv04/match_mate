package ak.app.matchmate.data.repository

import ak.app.matchmate.data.local.dao.UserDao
import ak.app.matchmate.data.local.model.UserEntity
import ak.app.matchmate.data.mappers.UserMappers.toUserEntity
import ak.app.matchmate.data.remote.RandomUserApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: RandomUserApiService,
    private val userDao: UserDao
) {

    /**
     * Fetches users from the API, maps them to UserEntity, and stores them in the local database.
     * Includes a retry mechanism for network failures.
     */
    suspend fun refreshUsers(maxRetries: Int = 3, initialDelayMillis: Long = 1000L) {
        var currentDelay = initialDelayMillis
        var attempts = 0
        while (attempts < maxRetries) {
            try {
                // Fetch data from API
                val apiUsers = apiService.getUsers().results
                // Map API users to Room entities and insert into database
                val userEntities = apiUsers.map { apiUser ->

                    val existingUser = userDao.getUserByUuid(apiUser.login.uuid)
                    if (existingUser != null) {
                        apiUser.toUserEntity().copy(status = existingUser.status)
                    } else {
                        apiUser.toUserEntity()
                    }
                }
                userDao.insertUsers(userEntities)
                return // Success, exit loop
            } catch (e: IOException) {
                // Only retry for network-related exceptions (e.g., from FlakyNetworkInterceptor)
                attempts++
                if (attempts < maxRetries) {
                    delay(currentDelay) // Wait before retrying
                    currentDelay *= 2 // Exponential backoff
                } else {
                    // Max retries reached, re-throw the exception to be handled by ViewModel
                    throw e
                }
            } catch (e: Exception) {
                // For other types of exceptions (e.g., parsing errors, non-network issues),
                // do not retry, just re-throw.
                throw e
            }
        }
    }

    /**
     * Retrieves all users from the local database.
     * This will be the primary source of data for the UI, ensuring offline availability.
     */
    fun getMatchCards(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    /**
     * Updates the status of a specific user in the local database.
     */
    suspend fun updateUserStatus(uuid: String, newStatus: String) {
        val userToUpdate = userDao.getUserByUuid(uuid)
        if (userToUpdate != null) {
            val updatedUser = userToUpdate.copy(status = newStatus)
            userDao.updateUser(updatedUser)
        } else {
            // Handle case where user is not found (e.g., log an error, throw an exception)
            // For now, we'll just print a message.
            println("User with UUID $uuid not found for status update.")
        }
    }
}
