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
            // Pop up to the start destination and save its state so switching
            // bottom-nav tabs doesn't build up a back stack. This MUST be applied
            // uniformly to every tab: if some tabs pop-up-with-saveState and others
            // don't, navigating (back) to the start destination becomes a silent
            // no-op once another tab has been visited.
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

    fun navigateTo(navController: NavHostController, arg: String): () -> Unit = navigateTo(navController)

    companion object {
        fun currentScreen(route: String): ConferenceScreen =
            with (route) {
                when {
                    startsWith(JavaZoneDestinations.SESSIONS_ROUTE) -> SessionsScreen
                    startsWith(JavaZoneDestinations.DETAILS_ROUTE) -> DetailsScreen
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
    const val DETAILS_ROUTE = "detail_session"
}



object SessionsScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Filled.CalendarToday
    override val route: String = JavaZoneDestinations.SESSIONS_ROUTE
    override val label: Int = R.string.sessions
}

object MyScheduleScreen : ConferenceScreen {
    override val icon: ImageVector = Icons.Outlined.Person
    override val route: String = JavaZoneDestinations.MY_SCHEDULE_ROUTE
    override val label: Int = R.string.my_schedule
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

object DetailsScreen : ConferenceScreen {
    override val icon: ImageVector =  Icons.Outlined.Info
    override val route: String = "${JavaZoneDestinations.DETAILS_ROUTE}/{id}"
    override val label: Int = R.string.session

    override fun navigateTo(navController: NavHostController, arg: String): () -> Unit = {
        val argRoute = "${JavaZoneDestinations.DETAILS_ROUTE}/$arg"
        Log.d(LOG_TAG, "Changing screen to $argRoute")
        navController.navigate(argRoute) {
            // A talk detail is a leaf screen pushed on top of whichever tab opened it
            // (Sessions or My Schedule). Do NOT pop up to the start destination here:
            // popping would drop the originating tab from the back stack, so Back (and
            // the derived fromRoute) would always return to Sessions instead of the tab
            // the user came from. launchSingleTop guards against a duplicate on double tap.
            launchSingleTop = true
        }
    }
}
