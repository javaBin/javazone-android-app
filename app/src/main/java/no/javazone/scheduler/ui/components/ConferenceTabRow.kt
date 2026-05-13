package no.javazone.scheduler.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ConferenceTabRow(
    allScreens: List<ConferenceScreen>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth()
    ) {
        allScreens.forEach { navItem ->
            NavigationBarItem(
                selected = navItem.route == currentRoute,
                onClick = navItem.navigateTo(navController),
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = null)
                }
            )
        }
    }
}
