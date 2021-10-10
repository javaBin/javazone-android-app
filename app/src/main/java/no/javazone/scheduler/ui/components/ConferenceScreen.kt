package no.javazone.scheduler.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
        get() = Icons.Filled.Person
}

object InfoScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Filled.Info
}

object PartnerScreen : BottomConferenceScreen() {
    override val icon: ImageVector
        get() = Icons.Filled.Groups
}
