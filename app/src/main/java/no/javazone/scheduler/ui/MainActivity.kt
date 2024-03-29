package no.javazone.scheduler.ui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import coil.annotation.ExperimentalCoilApi
import no.javazone.scheduler.JavaZoneApplication
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

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
