package no.javazone.scheduler.ui.landing

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import no.javazone.scheduler.ui.components.FullScreenLoading
import no.javazone.scheduler.ui.components.SessionsScreen
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.LoadingResource
import no.javazone.scheduler.utils.toJzString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@Composable
fun LandingRoute(
    navController: NavHostController,
    viewModel: ConferenceListViewModel
) {
    val isConferenceLoading = viewModel.conference.collectAsState().value is LoadingResource
    val isSessionLoading = viewModel.sessions.collectAsState().value.data.isEmpty()
    val isLoading by remember(
        isConferenceLoading,
        isSessionLoading
    ) {
        mutableStateOf(isConferenceLoading || isSessionLoading)
    }

    if (isLoading) {
        Log.d(LOG_TAG, "Loading")
        FullScreenLoading()
    } else {
        Log.d(LOG_TAG, "Loaded, sending to ${SessionsScreen.route}")
        val defaultDay = viewModel.getDefaultDate()
        SessionsScreen.navigateTo(navController, defaultDay.toJzString()).invoke()
    }
}

@Composable
@Preview
fun LandingRoutePreview() {
//    LandingRoute(
//        navController = rememberNavController(),
//        viewModel =
//    )
}