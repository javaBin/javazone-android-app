package no.javazone.scheduler.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.JavaZoneTypography

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
//        val navigationActions = remember(navController) {
//            JavaZoneNavigationActions(navController)
//        }

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: SessionsScreen.route

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                     TopAppBar {
                         Text(
                             text = stringResource(id = ConferenceScreen.currentScreen(currentRoute).label),
                             style = JavaZoneTypography.subtitle2
                         )
                     }
            },
            bottomBar = {
                ConferenceTabRow(
                    allScreens = listOf(SessionsScreen, MyScheduleScreen, InfoScreen, PartnerScreen),
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

