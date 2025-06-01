package ak.app.matchmate.di

import ak.app.matchmate.data.remote.FlakyNetworkInterceptor
import ak.app.matchmate.data.remote.RandomUserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes dependencies available throughout the app's lifecycle
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }
    }

    @Provides
    @Singleton
    fun provideFlakyNetworkInterceptor(): FlakyNetworkInterceptor {
        return FlakyNetworkInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        flakyNetworkInterceptor: FlakyNetworkInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // For logging network requests
            .addInterceptor(flakyNetworkInterceptor) // For simulating flaky network
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/") // Base URL for the RandomUser API
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // For JSON parsing
            .build()
    }

    @Provides
    @Singleton
    fun provideRandomUserApiService(retrofit: Retrofit): RandomUserApiService {
        return retrofit.create(RandomUserApiService::class.java)
    }
}
