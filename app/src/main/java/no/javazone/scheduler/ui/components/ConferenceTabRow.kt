package no.javazone.scheduler.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

val TabHeight = 56.dp
private const val InactiveTabOpacity = 0.60f

private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100

@Composable
fun ConferenceTabRow(
    allScreens: List<ConferenceScreen>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String
) {
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        BottomNavigation {
            allScreens.forEach { navItem ->
                BottomNavigationItem(
                    selected = navItem.route == currentRoute,
                    onClick = navItem.navigateTo(navController),
                    icon = {
                        Icon(imageVector = navItem.icon, contentDescription = null)
                    }
                )
            }
        }
    }
//    Surface(
//        modifier = modifier,
//    ) {
//        Row(
//            modifier = Modifier.selectableGroup(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//        ) {
//            allScreens.forEach {
//                ConferenceTab(
//                    text = "Hello",
//                    icon = it.icon,
//                    onSelected = {},
//                    selected = it is SessionsScreen
//                )
//            }
//        }
//    }
}
