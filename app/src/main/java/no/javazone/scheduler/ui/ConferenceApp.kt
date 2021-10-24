package no.javazone.scheduler.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.theme.JavaZoneTheme

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
            topBar = { },
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

