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
import no.javazone.scheduler.R
import no.javazone.scheduler.utils.LOG_TAG

sealed interface ConferenceScreen {
    val icon: ImageVector
    val route: String
    val label: Int

    fun navigateTo(navController: NavHostController): () -> Unit = {
        Log.d(LOG_TAG, "Changing screen to $route")
        navController.navigate(route) {
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateTo(navController: NavHostController, arg: String): () -> Unit = navigateTo(navController)

    companion object {
        fun currentScreen(route: String): ConferenceScreen =
            with (route) {
                when {
                    startsWith(JavaZoneDestinations.SESSIONS_ROUTE) -> SessionsScreen
                    startsWith(JavaZoneDestinations.SESSION_ROUTE) -> SessionScreen
                    startsWith(JavaZoneDestinations.MY_SCHEDULE_ROUTE) -> MyScheduleScreen
                    startsWith(JavaZoneDestinations.INFO_ROUTE) -> InfoScreen
                    startsWith(JavaZoneDestinations.PARTNERS_ROUTE) -> PartnerScreen
                    else -> SessionsScreen
                }.also {
                    Log.d(LOG_TAG, "Setting ${it.route}:${it.label}")
                }
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
    const val SESSION_ROUTE = "detail_session"
}



object SessionsScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Filled.CalendarToday
    override val route: String = "${JavaZoneDestinations.SESSIONS_ROUTE}?day={day}"
    override val label: Int = R.string.sessions

    override fun navigateTo(navController: NavHostController, arg: String): () -> Unit = {
        val argRoute = "${JavaZoneDestinations.SESSIONS_ROUTE}?day=$arg"
        Log.d(LOG_TAG, "Changing screen to $argRoute")
        navController.navigate(argRoute) {
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

object MyScheduleScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Person
    override val route: String = JavaZoneDestinations.MY_SCHEDULE_ROUTE
    override val label: Int = R.string.my_schedule

    override fun navigateTo(navController: NavHostController, arg: String): () -> Unit = {
        val argRoute = JavaZoneDestinations.MY_SCHEDULE_ROUTE
        Log.d(LOG_TAG, "Changing screen to $argRoute")
        navController.navigate(argRoute) {
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

object InfoScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Info
    override val route: String = JavaZoneDestinations.INFO_ROUTE
    override val label: Int = R.string.info
}

object PartnerScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Groups
    override val route: String = JavaZoneDestinations.PARTNERS_ROUTE
    override val label: Int = R.string.partners
}

object SessionScreen : ConferenceScreen {
    override val icon: ImageVector =  Icons.Outlined.Info
    override val route: String = "${JavaZoneDestinations.SESSION_ROUTE}/{id}"
    override val label: Int = R.string.session

}
