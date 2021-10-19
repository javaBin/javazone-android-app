package no.javazone.scheduler.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.javazone.scheduler.model.*
import no.javazone.scheduler.ui.components.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun ConferenceApp(sessions: List<ConferenceSession>) {
    val navController = rememberNavController()
    val dayToSessions = sessions.groupBy { it.date }

    Scaffold(
//        topBar = {
//            ConferenceTabRow(
//                allScreens = dayToSessions.map { it.key }.map {
//                    TopConferenceScreen(
//                        it.format(
//                            DateTimeFormatter.ISO_LOCAL_DATE
//                        )
//                    )
//                },
//                onTabSelected = {},
//                currentScreen = dayToSessions.map { it.key }.map {
//                    TopConferenceScreen(
//                        it.format(
//                            DateTimeFormatter.ISO_LOCAL_DATE
//                        )
//                    )
//                }.first()
//            )
//        },
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
                LazyColumn {
                    items(sessions.flatMap { it.talks }) { talk ->
                        Text(text = talk.startTime.toLocalTime().toString())
                        Text(text = talk.title)
                    }
                }
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ConferenceAppPreview() {
    ConferenceApp(
        sessions = listOf(
            ConferenceSession(
                ConferenceRoom(
                    name = "room1"
                ),
                talk = Talk(
                    id = "1",
                    title = "Hello World",
                    startTime = OffsetDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(9, 0, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    endTime = OffsetDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(9, 45, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    length = 45,
                    intendedAudience = "developers",
                    language = "no",
                    video = null,
                    abstract = "blablabalab",
                    speakers = setOf(
                        Speaker(
                            name = "Kari Nordmann",
                            bio = "blalala",
                            avatar = null,
                            twitter = null
                        )
                    ),
                    format = ConferenceFormat.PRESENTATION
                )
            ),
            ConferenceSession(
                ConferenceRoom(
                    name = "room1"
                ),
                talk = Talk(
                    id = "1",
                    title = "Hello World",
                    startTime = OffsetDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(9, 0, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    endTime = OffsetDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(9, 45, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    length = 45,
                    intendedAudience = "developers",
                    language = "no",
                    video = null,
                    abstract = "blablabalab",
                    speakers = setOf(
                        Speaker(
                            name = "Kari Nordmann",
                            bio = "blalala",
                            avatar = null,
                            twitter = null
                        )
                    ),
                    format = ConferenceFormat.PRESENTATION
                )
            ),
            ConferenceSession(
                ConferenceRoom(
                    name = "room1"
                ),
                talk = Talk(
                    id = "1",
                    title = "Hello World",
                    startTime = OffsetDateTime.of(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(9, 0, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    endTime = OffsetDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(9, 45, 0),
                        ZoneOffset.ofHours(1)
                    ),
                    length = 45,
                    intendedAudience = "developers",
                    language = "no",
                    video = null,
                    abstract = "blablabalab",
                    speakers = setOf(
                        Speaker(
                            name = "Kari Nordmann",
                            bio = "blalala",
                            avatar = null,
                            twitter = null
                        )
                    ),
                    format = ConferenceFormat.PRESENTATION
                )
            )

        )
    )
}