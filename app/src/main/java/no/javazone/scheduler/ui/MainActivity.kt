package no.javazone.scheduler.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import coil.annotation.ExperimentalCoilApi
import no.javazone.scheduler.JavaZoneApplication

@ExperimentalMaterial3Api
@ExperimentalCoilApi
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
