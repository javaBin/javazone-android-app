package no.javazone.scheduler.ui.schedules

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
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.JavaZoneDestinations
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDateFormat
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.sampleTalks
import no.javazone.scheduler.utils.toLocalString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.LocalDate

@Composable
fun MyScheduleRoute(
    navController: NavHostController,
    route: String,
    viewModel: ConferenceListViewModel
) {
    Log.d(LOG_TAG, "route: $route")

    val resource = viewModel.sessions.collectAsState().value
    val mySchedule = viewModel.mySchedule.collectAsState().value

    MyScheduleScreen(
        onToggleSchedule = { talkId -> viewModel.addOrRemoveSchedule(talkId) },
        navigateToDetail = { talkId ->
            Log.w("SessionviewDebug", "Session is $talkId")
            //navController.navigate(deepLink= "detail_session/${talk.id}"
            //navController.navigate(deepLink= Uri.parse("android-app://androidx.navigation/detail_session/${talk.id}"))
            viewModel.updateDetailsArg(talkId, route)
            navController.navigate(route = "${JavaZoneDestinations.SESSION_ROUTE}/${talkId}")
        },
        conferenceTalks = viewModel.selectMySchedule(
            resource.data,
            mySchedule
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyScheduleScreen(
    onToggleSchedule: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    conferenceTalks: Map<LocalDate, List<ConferenceTalk>>
) {
    Surface {
        LazyColumn {
            conferenceTalks.forEach { (slot, talks) ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {
                            Text(
                                text = slot.format(SessionDateFormat),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }

                items(talks) { talk ->
                    Row(
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                navigateToDetail(talk.id)
                            })

                    ) {
                        Column(
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Text(
                                text = talk.startTime.toLocalString(SessionTimeFormat) +
                                        " - " +
                                        talk.endTime.toLocalString(SessionTimeFormat),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = talk.room.name,
                                style = MaterialTheme.typography.labelLarge
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
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        IconButton(onClick = { }) {
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

@Composable
@Preview
fun MyScheduleScreenLightPreview(@PreviewParameter(SampleTalksProvider::class) talks: List<ConferenceTalk>) {
    val sessions = talks
        .groupBy {
            it.slotTime.toLocalDate()
        }
        .toMap()

    MyScheduleScreen(
        onToggleSchedule = {},
        navigateToDetail = {},
        conferenceTalks = sessions
    )
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun MyScheduleScreenDarkPreview(@PreviewParameter(SampleTalksProvider::class) talks: List<ConferenceTalk>) {
    val sessions = talks
        .groupBy {
            it.slotTime.toLocalDate()
        }
        .toMap()

    JavaZoneTheme(useDarkTheme = true) {
        MyScheduleScreen(
            onToggleSchedule = {},
            navigateToDetail = {},
            conferenceTalks = sessions
        )
    }
}

class SampleTalksProvider : PreviewParameterProvider<List<ConferenceTalk>> {
    override val values: Sequence<List<ConferenceTalk>> = sequenceOf(sampleTalks)

    override val count: Int
        get() = values.count()
}
