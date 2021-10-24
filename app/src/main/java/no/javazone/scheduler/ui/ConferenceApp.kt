package no.javazone.scheduler.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.ui.components.InfoScreen
import no.javazone.scheduler.ui.components.MyScheduleScreen
import no.javazone.scheduler.ui.components.PartnerScreen
import no.javazone.scheduler.ui.components.SessionsScreen
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
        val navigationActions = remember(navController) {
            JavaZoneNavigationActions(navController)
        }

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: JavaZoneDestinations.SESSIONS_ROUTE

        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
//                ConferenceTabRow(
//                    allScreens = listOf(
//                        SessionsScreen,
//                        MyScheduleScreen,
//                        InfoScreen,
//                        PartnerScreen
//                    ),
//                    modifier = Modifier
//                        .height(TabHeight)
//                        .fillMaxWidth()
//                        .navigationBarsPadding(),
//                    onTabSelected = {},
//                    currentScreen = SessionsScreen // need to change this to some default?
//                )
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    BottomNavigation() {
                        listOf(
                            SessionsScreen,
                            MyScheduleScreen,
                            InfoScreen,
                            PartnerScreen
                        ).forEach { navItem ->
                            BottomNavigationItem(
                                selected = navItem.javaClass.simpleName == currentRoute,
                                onClick = { /*TODO*/ },
                                icon = {
                                    Icon(imageVector = navItem.icon, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {

            }
        }
    }
}

