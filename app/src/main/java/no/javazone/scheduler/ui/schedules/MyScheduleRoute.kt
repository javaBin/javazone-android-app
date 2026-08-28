package no.javazone.scheduler.ui.schedules

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.DetailsScreen
import no.javazone.scheduler.ui.components.SessionSectionHeader
import no.javazone.scheduler.ui.components.TalkCard
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDayFormat
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.sampleTalks
import no.javazone.scheduler.utils.toLocalString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlinx.coroutines.launch

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
        ),
        listState = viewModel.myScheduleListState
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyScheduleScreen(
    onToggleSchedule: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    conferenceTalks: Map<LocalDate, List<ConferenceTalk>>,
    listState: LazyListState = rememberLazyListState()
) {
    val coroutineScope = rememberCoroutineScope()

    // Measured height of the sticky day header, so the "Jump to now" target lands
    // below it instead of behind it — measured rather than guessed. Stays 0 until the
    // first header has been laid out (by which time the user can tap the chip).
    var headerHeightPx by remember { mutableIntStateOf(0) }

    Surface {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                // One-shot action: scrolls to the talk happening now (or the next
                // upcoming one). Never scrolls on its own, so it can't fight the user.
                AssistChip(
                    onClick = {
                        val targetIndex =
                            currentSlotItemIndex(conferenceTalks, OffsetDateTime.now())
                        if (targetIndex != null) {
                            coroutineScope.launch {
                                listState.scrollToItem(targetIndex, -headerHeightPx)
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.jump_to_now)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null
                        )
                    }
                )
            }

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
            ) {
                conferenceTalks.forEach { (slot, talks) ->
                    stickyHeader {
                        SessionSectionHeader(
                            text = slot.format(SessionDayFormat),
                            modifier = Modifier.onSizeChanged { headerHeightPx = it.height }
                        )
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
}

/**
 * Computes the flat [LazyColumn] item index of the first talk — in the schedule's own
 * day/slot order — whose [ConferenceTalk.endTime] is still in the future relative to
 * [now], i.e. the talk you should currently be at (ongoing) or the next one up. Because
 * the scan follows schedule order rather than end-time order, when talk durations differ
 * this is the first not-yet-ended talk in the list, which is the sensible scroll target
 * even if some later-listed talk ends sooner. Accounts for the one sticky day-header item
 * that precedes each day's talks. Returns `null` when every talk has already ended.
 */
@VisibleForTesting
internal fun currentSlotItemIndex(
    conferenceTalks: Map<LocalDate, List<ConferenceTalk>>,
    now: OffsetDateTime
): Int? {
    var index = 0
    conferenceTalks.forEach { (_, talks) ->
        index++ // sticky day header
        for (talk in talks) {
            if (now.isBefore(talk.endTime)) {
                return index
            }
            index++
        }
    }
    return null
}

@Composable
@Preview(name = "Light Theme", showBackground = true)
fun MyScheduleScreenLightPreview(@PreviewParameter(SampleTalksProvider::class) talks: List<ConferenceTalk>) {
    val sessions = talks
        .groupBy { it.slotTime.toLocalDate() }
        .toMap()

    JavaZoneTheme(useDarkTheme = false) {
        MyScheduleScreen(
            onToggleSchedule = {},
            navigateToDetail = {},
            conferenceTalks = sessions
        )
    }
}

@Composable
@Preview(name = "Dark Theme", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
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
