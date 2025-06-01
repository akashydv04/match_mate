package ak.app.matchmate.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.random.Random

class FlakyNetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (Random.nextFloat() < 0.3f) {
            throw IOException("Simulated network failure by FlakyNetworkInterceptor")
        }
        return chain.proceed(chain.request())
    }
}