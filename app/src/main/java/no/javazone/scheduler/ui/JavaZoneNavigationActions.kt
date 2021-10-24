package no.javazone.scheduler.ui

import androidx.navigation.NavHostController

/**
 * Destinations used in the [JavaZoneApp].
 */
object JavaZoneDestinations {
    const val SESSIONS_ROUTE = "sessions"
    const val MY_SCHEDULE_ROUTE = "schedule"
    const val INFO_ROUTE = "info"
    const val PARTNERS_ROUTE = "partners"
}

class JavaZoneNavigationActions(navController: NavHostController) {
    val navigateToSessions: () -> Unit = {

    }
    val navigateToMySchedule: () -> Unit = {

    }
    val navigateToInfo: () -> Unit = {

    }
    val navigateToPartners: () -> Unit = {

    }
}
