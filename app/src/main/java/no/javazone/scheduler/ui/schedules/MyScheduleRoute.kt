package no.javazone.scheduler.ui.schedules

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.DetailsScreen
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDayFormat
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
            viewModel.updateDetailsArg(talkId, route)
            DetailsScreen.navigateTo(navController, talkId)()
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
        LazyColumn(
            contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
        ) {
            conferenceTalks.forEach { (slot, talks) ->
                stickyHeader {
                    Surface(tonalElevation = 10.dp) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                            ) {
                                Text(
                                    text = slot.format(SessionDayFormat),
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                    }
                }

                items(talks) { talk ->
                    Card(
                        onClick = { navigateToDetail(talk.id) },
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

@Composable
@Preview
fun MyScheduleScreenLightPreview(@PreviewParameter(SampleTalksProvider::class) talks: List<ConferenceTalk>) {
    val sessions = talks
        .groupBy { it.slotTime.toLocalDate() }
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
        .groupBy { it.slotTime.toLocalDate() }
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
