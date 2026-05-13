package no.javazone.scheduler.ui.sessions

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceFormat
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
    val selectedDay = viewModel.selectedDay.value
    val selectedFormat = viewModel.selectedFormat.value
    val searchQuery = viewModel.searchQuery.value
    val toAllSessionScreen = @Composable {
        AllSessionsScreen(
            onToggleSchedule = { talkId -> viewModel.addOrRemoveSchedule(talkId) },
            navigateToDetail = { talkId ->
                val newRoute = "${JavaZoneDestinations.DETAILS_ROUTE}/$talkId"
                Log.d(LOG_TAG, "Navigating to $newRoute")
                viewModel.updateDetailsArg(talkId, route)
                DetailsScreen.navigateTo(navController, talkId)()
            },
            navigateToDay = { selectDay ->
                viewModel.updateSelectedDay(selectDay)
            },
            navigateToFormat = { format ->
                viewModel.updateSelectedFormat(format)
            },
            onSearchQueryChange = { query ->
                viewModel.updateSearchQuery(query)
            },
            conferenceSessions = viewModel.updateSessionsWithMySchedule(
                resource.data,
                selectedDay,
                selectedFormat,
                mySchedule,
                searchQuery
            ),
            conferenceDays = conferenceDays,
            selectedDay = selectedDay,
            selectedFormat = selectedFormat,
            searchQuery = searchQuery
        )
    }

    when (resource) {
        is SuccessResource -> {
            Log.d(LOG_TAG, "Successfully retrieved sessions")
            toAllSessionScreen()
        }
        is LoadingResource -> {
            if (resource.data.isEmpty()) {
                Log.d(LOG_TAG, "Loading sessions, no data")
                FullScreenLoading()
            } else {
                Log.d(LOG_TAG, "Loading sessions, has data")
                toAllSessionScreen()
            }
        }
        is ErrorResource -> {

            if (resource.data.isNotEmpty()) {
                Log.d(LOG_TAG, "Loading sessions, has data")
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
    navigateToDay: (LocalDate?) -> Unit,
    navigateToFormat: (ConferenceFormat?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    conferenceSessions: List<ConferenceSession>,
    conferenceDays: List<ConferenceDate>,
    selectedDay: LocalDate?,
    selectedFormat: ConferenceFormat?,
    searchQuery: String
) {
    Log.d(LOG_TAG, "Number of sessions ${conferenceSessions.size}")

    Surface() {
        Column (modifier = Modifier.fillMaxWidth()) {
            // Conference Days Filter
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(start = 5.dp, bottom = 10.dp, top = 10.dp)
            ) {
                Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                    ConferenceChip(
                        label = "All",
                        selected = selectedDay == null,
                        onExecute = { navigateToDay(null) }
                    )
                }
                conferenceDays.sortedBy { it.date }.forEach {
                    Column(modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                    ) {
                        ConferenceChip(
                            label = SessionDayFormat.format(it.date),
                            selected = it.date == selectedDay,
                            onExecute = { navigateToDay(it.date) }
                        )
                    }
                }
            }

            // Conference Format Filter
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(start = 5.dp, bottom = 10.dp)
            ) {
                Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                    ConferenceChip(
                        label = "All",
                        selected = selectedFormat == null,
                        onExecute = { navigateToFormat(null) }
                    )
                }
                ConferenceFormat.entries.forEach { format ->
                    Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                        ConferenceChip(
                            label = stringResource(id = format.label),
                            selected = format == selectedFormat,
                            onExecute = { navigateToFormat(format) }
                        )
                    }
                }
            }

            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            // Workshop Info Text
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(start = 5.dp, bottom = 10.dp, top = 10.dp)
            ){
                if (!conferenceDays.isNullOrEmpty() && selectedDay != null && selectedDay == conferenceDays.sortedBy { it.date }.first().date) {
                    Text("Workshops require registration ahead of time")
                }
            }

            // Sessions
            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
            ) {
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
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }

                    items(session.talks) { talk ->
                        Card(
                            onClick = {
                                Log.w("SessionviewDebug", "Session is ${talk.id}")
                                navigateToDetail(talk.id)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Time / Room / Format metadata column
                                Column(
                                    modifier = Modifier
                                        .width(90.dp)
                                        .padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = talk.startTime.toLocalString(SessionTimeFormat) +
                                                " – " +
                                                talk.endTime.toLocalString(SessionTimeFormat),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = talk.room.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            maxLines = 1
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = talk.format.label),
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            maxLines = 1
                                        )
                                    }
                                }
                                // Title and speakers column
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = talk.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (talk.speakers.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = talk.speakers.joinToString { it.name },
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                // Bookmark toggle
                                MyScheduleButton(
                                    isScheduled = talk.scheduled,
                                    onClick = { onToggleSchedule(talk.id) }
                                )
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
fun AllSessionsScreenLightPreview(@PreviewParameter(SampleSessionProvider::class) sessions: List<ConferenceSession>) {

    var i = 0
    AllSessionsScreen(
        onToggleSchedule = { },
        navigateToDetail = {},
        navigateToDay = {},
        navigateToFormat = {},
        onSearchQueryChange = {},
        conferenceSessions = sessions,
        conferenceDays = DEFAULT_CONFERENCE_DAYS.map {
            ConferenceDate(it, "day ${i++}")
        },
        selectedDay = FIRST_CONFERENCE_DAY,
        selectedFormat = null,
        searchQuery = ""
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
            navigateToFormat = {},
            onSearchQueryChange = {},
            conferenceSessions = sessions,
            conferenceDays = DEFAULT_CONFERENCE_DAYS.map {
                ConferenceDate(it, "day ${i++}")
            },
            selectedDay = FIRST_CONFERENCE_DAY,
            selectedFormat = null,
            searchQuery = ""
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

