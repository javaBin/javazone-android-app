package no.javazone.scheduler.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import coil.annotation.ExperimentalCoilApi
import no.javazone.scheduler.JavaZoneApplication
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
class SplashScreenActivity : AppCompatActivity() {
    private val appContainer by lazy {
        (application as JavaZoneApplication).container
    }

    val viewModel: ConferenceListViewModel by viewModels(
        factoryProducer = { ConferenceListViewModel.provideFactory(appContainer.repository) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            // set an exit transition
            exitTransition = Slide()
        }

        viewModel.isReady.observe(this) { isReady ->
            if (isReady) {
                startActivity(
                    Intent(this, MainActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
            }
        }
    }
}
