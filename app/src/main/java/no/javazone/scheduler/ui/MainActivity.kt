package no.javazone.scheduler.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import no.javazone.scheduler.JavaZoneApplication
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
class MainActivity : AppCompatActivity() {

    private val appContainer by lazy {
        (application as JavaZoneApplication).container
    }

    private val viewModel: ConferenceListViewModel by viewModels(
        factoryProducer = { ConferenceListViewModel.provideFactory(appContainer.repository) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible until the initial sessions data has loaded.
        splashScreen.setKeepOnScreenCondition { !viewModel.isReady.value }

        setContent {
            ConferenceApp(appContainer)
        }
    }
}
