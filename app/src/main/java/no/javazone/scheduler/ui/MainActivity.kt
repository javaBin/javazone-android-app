package no.javazone.scheduler.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import no.javazone.scheduler.JavaZoneApplication

@ExperimentalFoundationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as JavaZoneApplication).container

        setContent {
            ConferenceApp(appContainer)
        }
    }

}
