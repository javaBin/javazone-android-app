package no.javazone.scheduler.ui.sessions

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.ConferenceChip
import no.javazone.scheduler.ui.components.DetailsScreen
import no.javazone.scheduler.ui.components.FullScreenLoading
import no.javazone.scheduler.ui.components.JavaZoneDestinations
import no.javazone.scheduler.ui.components.TalkCard
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDayFormat
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.DEFAULT_CONFERENCE_DAYS
import no.javazone.scheduler.utils.ErrorResource
import no.javazone.scheduler.utils.FIRST_CONFERENCE_DAY
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.LoadingResource
import no.javazone.scheduler.utils.SuccessResource
import no.javazone.scheduler.utils.sampleTalks
import no.javazone.scheduler.utils.toLocalString
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
                contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp)
            ) {
                conferenceSessions.forEach { session ->
                    stickyHeader {
                        Surface(
                            tonalElevation = 10.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
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
                        TalkCard(
                            talk = talk,
                            onToggleSchedule = onToggleSchedule,
                            navigateToDetail = navigateToDetail
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
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
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
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

