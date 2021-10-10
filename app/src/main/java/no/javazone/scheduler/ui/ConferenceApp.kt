package no.javazone.scheduler.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.*
import java.time.format.DateTimeFormatter

@Composable
fun ConferenceApp(sessions: List<ConferenceSession>) {
    val navController = rememberNavController()
    val dayToSessions = sessions.groupBy { it.date }

    Scaffold(
        topBar = {
            ConferenceTabRow(
                allScreens = dayToSessions.map { it.key }.map { TopConferenceScreen(it.format(
                    DateTimeFormatter.ISO_LOCAL_DATE)) },
                onTabSelected = {},
                currentScreen = dayToSessions.map { it.key }.map { TopConferenceScreen(it.format(
                    DateTimeFormatter.ISO_LOCAL_DATE)) }.first()
            )
        },
        bottomBar = {
            ConferenceTabRow(
                allScreens = listOf(SessionsScreen, MyScheduleScreen, InfoScreen, PartnerScreen),
                onTabSelected = {},
                currentScreen = SessionsScreen // need to change this to some default?
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SessionsScreen.javaClass.simpleName,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SessionsScreen.javaClass.simpleName) {
                Text(text = "conference screen")
            }
            composable(route = MyScheduleScreen.javaClass.simpleName) {
                Text(text = "my schedule screen")
            }
            composable(route = MyScheduleScreen.javaClass.simpleName) {
                Text(text = "info screen")
            }
            composable(route = MyScheduleScreen.javaClass.simpleName) {
                Text(text = "partner screen")
            }
        }
    }
}
