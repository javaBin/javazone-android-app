package no.javazone.scheduler.ui.schedules

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.DetailsScreen
import no.javazone.scheduler.ui.components.TalkCard
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDayFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.sampleTalks
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

@Composable
@Preview(showBackground = true)
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
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
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
