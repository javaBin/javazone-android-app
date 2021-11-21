package no.javazone.scheduler.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.info.InfoRoute
import no.javazone.scheduler.ui.landing.LandingRoute
import no.javazone.scheduler.ui.partners.PartnersRoute
import no.javazone.scheduler.ui.schedules.MyScheduleRoute
import no.javazone.scheduler.ui.sessions.SessionDetailRoute
import no.javazone.scheduler.ui.sessions.SessionsRoute
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.toJzLocalDate
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun JavaZoneNavGraph(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SessionsScreen.route
) {
    val viewModel: ConferenceListViewModel = viewModel(
        factory = ConferenceListViewModel.provideFactory(appContainer.repository)
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = SessionsScreen.route,
            arguments = listOf(navArgument(name = "day") {
                type = NavType.StringType
                nullable = true
            })
        ) { entry ->
            var day = entry.arguments
                ?.getString("day")
                ?.toJzLocalDate()
            day = day ?: viewModel.getDefaultDate()

            SessionsRoute(
                navController = navController,
                route = JavaZoneDestinations.SESSIONS_ROUTE,
                viewModel = viewModel,
                day = day
            )
        }
        composable(
            route = MyScheduleScreen.route
        ) {
            MyScheduleRoute(
                navController = navController,
                route = JavaZoneDestinations.MY_SCHEDULE_ROUTE,
                viewModel = viewModel
            )
        }
        composable(route = InfoScreen.route) {
            InfoRoute()
        }
        composable(route = PartnerScreen.route) {
            PartnersRoute(appContainer)
        }
        composable(
            route = SessionScreen.route,
            arguments = listOf(navArgument(name = "id") {
                type = NavType.StringType
                defaultValue = "NULLNULLNULL"
            })
        ) { entry ->
            val entryArg = entry.arguments?.getString("id")
            val sessionId = if (entryArg != null && entryArg != "NULLNULLNULL") {
                entryArg
            } else {
                Log.d(LOG_TAG, "Bug workaround: arguments null, retrieve from viewModel")
                viewModel.getDetailsArg()
            }
            SessionDetailRoute(
                route = JavaZoneDestinations.SESSION_ROUTE,
                viewModel = viewModel,
                sessionId = sessionId
            )
        }
        composable(
            route = LandingScreen.route
        ) { entry ->
            LandingRoute(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}