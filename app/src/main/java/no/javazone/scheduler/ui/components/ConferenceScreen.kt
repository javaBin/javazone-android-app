package no.javazone.scheduler.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import no.javazone.scheduler.utils.LOG_TAG

sealed interface ConferenceScreen {
    val icon: ImageVector
    val route: String

    fun navigateTo(navController: NavHostController): () -> Unit = {
        Log.d(LOG_TAG, "Changing screen to $route")
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

/**
 * Destinations used in the [JavaZoneApp].
 */
object JavaZoneDestinations {
    const val SESSIONS_ROUTE = "sessions"
    const val MY_SCHEDULE_ROUTE = "schedule"
    const val INFO_ROUTE = "info"
    const val PARTNERS_ROUTE = "partners"
}



object SessionsScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Filled.CalendarToday
    override val route: String = "${JavaZoneDestinations.SESSIONS_ROUTE}?day={day}"
}

object MyScheduleScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Person
    override val route: String = "${JavaZoneDestinations.MY_SCHEDULE_ROUTE}?day={day}"
}

object InfoScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Info
    override val route: String = JavaZoneDestinations.INFO_ROUTE

}

object PartnerScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Groups
    override val route: String = JavaZoneDestinations.PARTNERS_ROUTE
}
