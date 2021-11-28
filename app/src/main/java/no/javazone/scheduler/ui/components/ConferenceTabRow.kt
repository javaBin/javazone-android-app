package no.javazone.scheduler.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

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
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
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
}
