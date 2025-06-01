package ak.app.matchmate.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MatchMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}