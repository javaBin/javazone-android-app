package no.javazone.scheduler.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ConferenceTabRow(
    allScreens: List<ConferenceScreen>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String
) {
    // Match the tonal surface color used by the sessions time sticky header
    // (Surface with tonalElevation = 10.dp → primary blended into surface at ~13%)
    val indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        allScreens.forEach { navItem ->
            NavigationBarItem(
                selected = navItem.route == currentRoute,
                onClick = navItem.navigateTo(navController),
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(navItem.label))
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicatorColor
                )
            )
        }
    }
}
