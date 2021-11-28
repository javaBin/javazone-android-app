package no.javazone.scheduler.ui.sessions

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.*
import no.javazone.scheduler.ui.theme.JavaZoneTheme
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
    viewModel: ConferenceListViewModel
) {
    Log.d(LOG_TAG, "route: $route")

    val resource = viewModel.sessions.collectAsState().value
    val conferenceDays = viewModel.conferenceDays
    val mySchedule = viewModel.mySchedule.collectAsState().value
    var selectedDay by remember {
        mutableStateOf(viewModel.getDefaultDate())
    }
    val toAllSessionScreen = @Composable {
        AllSessionsScreen(
            onToggleSchedule = { talkId -> viewModel.addOrRemoveSchedule(talkId) },
            navigateToDetail = { talkId ->
                //navController.navigate(deepLink= "detail_session/${talk.id}"
                //navController.navigate(deepLink= Uri.parse("android-app://androidx.navigation/detail_session/${talk.id}"))
                val newRoute = "${JavaZoneDestinations.DETAILS_ROUTE}/$talkId"
                Log.d(LOG_TAG, "Navigating to $newRoute")
                viewModel.updateDetailsArg(talkId, route)
                DetailsScreen.navigateTo(navController, talkId)()
            },
            navigateToDay = { selectDay ->
                selectedDay = selectDay
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
    onToggleSchedule: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToDay: (LocalDate) -> Unit,
    conferenceSessions: List<ConferenceSession>,
    conferenceDays: List<ConferenceDate>,
    selectedDay: LocalDate
) {
    Log.d(LOG_TAG, "Number of sessions ${conferenceSessions.size}")

    Surface() {
        Column {
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(start = 5.dp, bottom = 10.dp)
                    .fillMaxWidth()
            ) {
                conferenceDays.sortedBy { it.date }
                    .forEach {
                        ConferenceChip(
                            label = SessionDayFormat.format(it.date),
                            selected = it.date == selectedDay,
                            onExecute = { navigateToDay(it.date) }
                        )
                    }
            }


            LazyColumn {
                conferenceSessions.forEach { session ->
                    stickyHeader {

                        Surface(
                            tonalElevation = 10.dp,

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        session.time.toLocalString(SessionTimeFormat),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }
                            }
                        }
                    }

                    items(session.talks) { talk ->
                        Surface(
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
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = talk.room.name,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = talk.format.name,
                                        style = MaterialTheme.typography.labelMedium
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
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = talk.speakers.joinToString { it.name },
                                        style = MaterialTheme.typography.labelLarge
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

@Composable
@Preview
fun AllSessionsScreenLightPreview(@PreviewParameter(SampleSessionProvider::class) sessions: List<ConferenceSession>) {

    var i = 0
    AllSessionsScreen(
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

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun AllSessionsScreenDarkPreview(@PreviewParameter(SampleSessionProvider::class) sessions: List<ConferenceSession>) {

    var i = 0
    JavaZoneTheme(useDarkTheme = true) {
        AllSessionsScreen(
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
                    it.copy(
                        startTime = it.startTime.plusHours(2L),
                        endTime = it.endTime.plusHours(2L)
                    )
                }
            )
        )
    )
}

