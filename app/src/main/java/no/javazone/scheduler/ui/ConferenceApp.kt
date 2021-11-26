package no.javazone.scheduler.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.JavaZoneTypography

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun ConferenceApp(
    appContainer: AppContainer
) {

    JavaZoneTheme {

        val systemUiController = rememberSystemUiController()
        val darkIcons = MaterialTheme.colors.isLight
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = darkIcons)
        }

        val navController = rememberNavController()

        val scaffoldState = rememberScaffoldState()

        val navBackStackEntry =
            navController.currentBackStackEntryFlow.collectAsState(null).value
        val currentRoute = navBackStackEntry?.destination?.route ?: LandingScreen.route

        Surface {

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar {
                        Text(
                            text = stringResource(id = ConferenceScreen.currentScreen(currentRoute).label),
                            style = JavaZoneTypography.titleLarge
                        )
                    }
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
                    startDestination = currentRoute
                )
            }
        }
    }
}
