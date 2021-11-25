package no.javazone.scheduler.ui.sessions

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.sampleTalks
import no.javazone.scheduler.utils.toLocalString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionDetailRoute(
    route: String,
    viewModel: ConferenceListViewModel,
    sessionId: String,
) {
    Log.d(LOG_TAG, "SessionDetailRoute route: $route, sessionId: $sessionId")

    val scaffoldState = rememberScaffoldState()

    val session = viewModel.sessions.collectAsState().value.data
        .flatMap { it.talks }
        .find { it.id == sessionId }
        ?: return

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        SessionDetailFragment(session)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun SessionDetailFragment(session: ConferenceTalk) {

    val scrollState = rememberScrollState()
    Surface {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Column {
                    Text(
                        text = sessionRoomAndTimeslot(session),
                        style = JavaZoneTypography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = session.title,
                        style = JavaZoneTypography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(text = "Abstract", style = JavaZoneTypography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = session.summary, style = JavaZoneTypography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Intended Audience", style = JavaZoneTypography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = session.intendedAudience, style = JavaZoneTypography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Speakers", style = JavaZoneTypography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                for (speaker in session.speakers) {
                    Row {

                        Column {
                            Image(
                                painter = rememberImagePainter(speaker.avatarUrl),
                                contentDescription = speaker.name,
                                modifier = Modifier.size(74.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {

                            Text(text = speaker.name, style = JavaZoneTypography.titleMedium)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Twitter: ${speaker.twitter?:""}",
                                style = JavaZoneTypography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = speaker.bio, style = JavaZoneTypography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}

@Composable
private fun sessionRoomAndTimeslot(session: ConferenceTalk): String {
    return session.startTime.toLocalString(SessionTimeFormat) +
            "-" +
            session.endTime.toLocalString(SessionTimeFormat) +
            "\n" +
            session.room.name +
            "\n" +
            session.format.name.lowercase()
}

@Composable
@Preview
fun SessionDetailFragmentPreview(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {
    SessionDetailFragment(session = session)
}


class SampleTalkProvider : PreviewParameterProvider<ConferenceTalk> {
    override val values = sampleTalks.asSequence()

    override val count: Int = sampleTalks.size
}
