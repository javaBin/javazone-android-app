package no.javazone.scheduler.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ConferenceScreen

class TopConferenceScreen(val text: String) : ConferenceScreen()

sealed class BottomConferenceScreen: ConferenceScreen() {
    abstract val icon: ImageVector
}

object SessionsScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Filled.CalendarToday
}

object MyScheduleScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Outlined.Person
}

object InfoScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Outlined.Info
}

object PartnerScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Outlined.Groups
}
