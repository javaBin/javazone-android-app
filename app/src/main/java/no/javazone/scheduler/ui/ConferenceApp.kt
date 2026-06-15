package no.javazone.scheduler.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.JavaZoneTypography

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConferenceApp(
    appContainer: AppContainer
) {

    JavaZoneTheme {
        val navController = rememberNavController()
        val navBackStackEntry =
            navController.currentBackStackEntryFlow.collectAsState(null).value
        val currentRoute = navBackStackEntry?.destination?.route ?: SessionsScreen.route

        Surface {
            Scaffold(
                topBar = {
                },
                bottomBar = {
                    ConferenceTabRow(
                        allScreens = listOf(
                            SessionsScreen,
                            MyScheduleScreen,
                            PartnerScreen,
                            InfoScreen,
                        ),
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            ) { innerPadding ->
                JavaZoneNavGraph(
                    appContainer = appContainer,
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    startDestination = SessionsScreen.route
                )
            }
        }
    }
}
