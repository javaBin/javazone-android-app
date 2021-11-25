package no.javazone.scheduler.ui.sessions

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.FullScreenLoading
import no.javazone.scheduler.ui.components.JavaZoneDestinations
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.SessionDayFormat
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.*
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun SessionsRoute(
    navController: NavHostController,
    route: String,
    viewModel: ConferenceListViewModel,
    day: LocalDate
) {
    Log.d(LOG_TAG, "route: $route")

    val resource = viewModel.sessions.collectAsState().value
    val conferenceDays = viewModel.conferenceDays
    val mySchedule = viewModel.mySchedule.collectAsState().value
    val selectedDay = day
    val toAllSessionScreen = @Composable {
        AllSessionsScreen(
            route = route,
            onToggleSchedule = { talkId -> viewModel.addOrRemoveSchedule(talkId) },
            navigateToDetail = { talkId ->
                //navController.navigate(deepLink= "detail_session/${talk.id}"
                //navController.navigate(deepLink= Uri.parse("android-app://androidx.navigation/detail_session/${talk.id}"))
                val newRoute = "${JavaZoneDestinations.SESSION_ROUTE}/$talkId"
                Log.d(LOG_TAG, "Navigating to $newRoute")
                viewModel.updateDetailsArg(talkId)
                navController.navigate(route = newRoute)
            },
            navigateToDay = { selectDay ->
                navController.navigate(route = "$route?day=${selectDay.toJzString()}")
            },
            conferenceSessions = viewModel.updateSessionsWithMySchedule(
                resource.data,
                selectedDay,
                mySchedule
            ),
            conferenceDays = conferenceDays,
            selectedDay = selectedDay
        )
    }

    when (resource) {
        is SuccessResource -> toAllSessionScreen()
        is LoadingResource -> {
            if (resource.data.isEmpty()) {
                FullScreenLoading()
            } else {
                toAllSessionScreen()
            }
        }
        is ErrorResource -> {

            if (resource.data.isNotEmpty()) {
                toAllSessionScreen()
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AllSessionsScreen(
    route: String,
    onToggleSchedule: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToDay: (LocalDate) -> Unit,
    conferenceSessions: List<ConferenceSession>,
    conferenceDays: List<ConferenceDate>,
    selectedDay: LocalDate
) {
    Log.d(LOG_TAG, "Number of sessions ${conferenceSessions.size}")
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Surface() {
            Column {
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    conferenceDays.sortedBy { it.date }
                        .forEach {

                            OutlinedButton(
                                modifier = Modifier
                                    .selectable(
                                        selected = it.date == selectedDay,
                                        role = Role.Button,
                                        onClick = {}
                                    )
                                    .navigationBarsPadding(bottom = false)
                                    .weight(1f),
                                onClick = {
                                    Log.d("NavController debug", route)
                                    navigateToDay(it.date)
                                },
                            ) {
                                Text(
                                    text = SessionDayFormat.format(it.date),
                                    style = if (it.date == selectedDay)
                                        JavaZoneTypography.titleLarge.plus(
                                            TextStyle(fontWeight = FontWeight.Bold)
                                        ) else JavaZoneTypography.titleLarge
                                )
                            }
                        }
                }


                LazyColumn {
                    conferenceSessions.forEach { session ->
                        stickyHeader {

                            Surface(
                                color = MaterialTheme.colors.secondary,
                                //elevation = 10.dp,

                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            session.time.toLocalString(SessionTimeFormat),
                                            fontSize = 27.sp
                                        )
                                    }
                                }
                            }
                        }

                        items(session.talks) { talk ->
                            Surface(
                                color = MaterialTheme.colors.primary,
                                //elevation = 10.dp,

                            ) {
                                Row(
                                    modifier = Modifier
                                        //.padding(1.dp)
                                        //.border(width = 2.dp, color = MaterialTheme.colors.onSecondary)
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            Log.w("SessionviewDebug", "Session is ${talk.id}")
                                            navigateToDetail(talk.id)
                                        })

                                ) {
                                    Column(
                                        modifier = Modifier.padding(
                                            top = 16.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                    ) {
                                        Text(
                                            text = talk.startTime.toLocalString(SessionTimeFormat) +
                                                    " - " +
                                                    talk.endTime.toLocalString(SessionTimeFormat),
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = talk.room.name,
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = talk.format.name,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Column(
                                        Modifier
                                            .weight(1f)
                                            .padding(top = 16.dp, bottom = 16.dp),
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .align(alignment = Alignment.CenterHorizontally)
                                                .fillMaxWidth(),
                                            text = talk.title,
                                            style = JavaZoneTypography.bodyMedium
                                        )
                                        Text(
                                            text = talk.speakers.joinToString { it.name },
                                            fontSize = 10.sp
                                        )
                                    }
                                    IconButton(onClick = { }) {
                                        MyScheduleButton(
                                            isScheduled = talk.scheduled,
                                            onClick = { onToggleSchedule(talk.id) }
                                        )
                                    }
                                }
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun AllSessionsScreenPreview(@PreviewParameter(SampleSessionProvider::class) sessions: List<ConferenceSession>) {

    var i = 0
    AllSessionsScreen(
        route = "theroute",
        onToggleSchedule = { },
        navigateToDetail = {},
        navigateToDay = {},
        conferenceSessions = sessions,
        conferenceDays = DEFAULT_CONFERENCE_DAYS.map {
            ConferenceDate(it, "day ${i++}")
        },
        selectedDay = FIRST_CONFERENCE_DAY
    )
}

class SampleSessionProvider : PreviewParameterProvider<List<ConferenceSession>> {
    override val values: Sequence<List<ConferenceSession>> = sequenceOf(
        listOf(
            ConferenceSession(
                time = OffsetDateTime.now().minusHours(1L),
                talks = sampleTalks
            ),
            ConferenceSession(
                time = OffsetDateTime.now().plusHours(2L),
                talks = sampleTalks.map {
                    it.copy(startTime = it.startTime.plusHours(2L), endTime = it.endTime.plusHours(2L))
                }
            )
        )
    )
}

