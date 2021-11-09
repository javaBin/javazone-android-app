package no.javazone.scheduler.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.sessions.SessionDetailRoute
import no.javazone.scheduler.ui.sessions.SessionsRoute
import no.javazone.scheduler.utils.toJzLocalDate
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@Composable
fun JavaZoneNavGraph(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SessionsScreen.route
) {
    val viewModel: ConferenceListViewModel = viewModel(
        factory = ConferenceListViewModel.provideFactory(appContainer.sessionRepository)
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
            val day = entry.arguments
                ?.getString("day")
                ?.toJzLocalDate()
            SessionsRoute(
                navController = navController,
                route = JavaZoneDestinations.SESSIONS_ROUTE,
                viewModel = viewModel,
                day = day,
                myScheduleOnly = false
            )
        }
        composable(
            route = MyScheduleScreen.route,
            arguments = listOf(navArgument(name = "day") {
                type = NavType.StringType
                nullable = true
            })
        ) { entry ->
            val day = entry.arguments
                ?.getString("day")
                ?.toJzLocalDate()
            SessionsRoute(
                navController = navController,
                route = JavaZoneDestinations.MY_SCHEDULE_ROUTE,
                viewModel = viewModel,
                day = day,
                myScheduleOnly = true
            )
        }
        composable(route = InfoScreen.route) {
            Text(text = "info screen")
        }
        composable(route = PartnerScreen.route) {
            Text(text = "partner screen")
        }
        composable(
            route = SessionScreen.route,
            arguments = listOf(navArgument(name = "id") {
                type = NavType.StringType
                nullable = true
            })
        ) { entry ->
            SessionDetailRoute(
                navController = navController,
                route = JavaZoneDestinations.SESSION_ROUTE,
                viewModel = viewModel,
                sessionId = entry.arguments?.getString("id") ?: "NULLNULLNULL"
            )
        }
    }
}