package no.javazone.scheduler.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import no.javazone.scheduler.JavaZoneApplication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as JavaZoneApplication).container

        setContent {
            ConferenceApp(appContainer)
        }
    }

}
