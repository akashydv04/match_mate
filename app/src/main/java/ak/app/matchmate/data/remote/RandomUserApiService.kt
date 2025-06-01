package ak.app.matchmate.data.remote

import ak.app.matchmate.data.remote.model.UserApiResponse
import retrofit2.http.GET

interface RandomUserApiService {
    @GET("?results=10")
    suspend fun getUsers(): UserApiResponse
}