package no.javazone.scheduler.ui.sessions

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceRoom
import no.javazone.scheduler.model.ConferenceSpeaker
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.toLocalString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.OffsetDateTime


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
        sessionDetailFragment(session)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
private fun sessionDetailFragment(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {

    val scrollState = rememberScrollState()
    Surface() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Column {
                    Text(
                        text = sessionRoomAndTimeslot(session),
                        //style = JavaZoneTypography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = session.title,
                        //style = JavaZoneTypography.subtitle1
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

                            Text(text = speaker.name, style = JavaZoneTypography.titleSmall)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Twitter: ${speaker.twitter}",
                                style = JavaZoneTypography.titleMedium
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

class SampleTalkProvider : PreviewParameterProvider<ConferenceTalk> {
    override val values = sequenceOf(
        ConferenceTalk(
            "19F59B3A-2DF9-499B-940E-D6CA20E00840",
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            startTime = OffsetDateTime.now().minusHours(1),
            endTime = OffsetDateTime.now().plusHours(1),
            length = 120,
            intendedAudience = "Beginner",
            language = "Latin",
            video = "https://vimeo.com/253989945",
            summary = "Cras posuere hendrerit lorem a lacinia. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur dictum rutrum elit, eu dictum arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus non porta purus, et molestie ipsum. Sed iaculis faucibus maximus. Duis ut arcu lacinia, porta metus at, dignissim neque. Nam ultrices semper ex a pharetra. Donec lacinia condimentum elit, a hendrerit quam scelerisque vulputate. Quisque dui dolor, pharetra sit amet dictum eu, vehicula a turpis. Nunc pellentesque, erat non egestas viverra, mauris augue vulputate tellus, nec sagittis risus magna et erat. Proin enim sapien, elementum id sapien nec, auctor molestie orci. Pellentesque mattis leo et blandit aliquet.",
            speakers = setOf(
                ConferenceSpeaker(
                    name = "Navn Nevnes",
                    bio = "Mauris pharetra faucibus lorem, id aliquet est egestas eget. In posuere eros nibh, porta iaculis risus laoreet vitae. Quisque vulputate tincidunt mauris in pretium. Phasellus congue sodales rhoncus. Nullam fringilla nisi sapien. Fusce eget ex leo. Fusce non augue augue. Aliquam dictum mattis auctor.",
                    avatarUrl = "https://www.gravatar.com/avatar/333a3587d4c6757b04c86b47fbafc64a?d=mp",
                    twitter = "javabin"
                )
            ),
            format = ConferenceFormat.PRESENTATION,
            room = ConferenceRoom.create("Room 1"),
            scheduled = true
        ),
        ConferenceTalk(
            "19F59B3A-2DF9-499B-940E-D6CA20E00840",
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            startTime = OffsetDateTime.now().plusHours(1),
            endTime = OffsetDateTime.now().plusHours(3),
            length = 120,
            intendedAudience = "Beginner",
            language = "Latin",
            video = "https://vimeo.com/253989945",
            summary = "Cras posuere hendrerit lorem a lacinia. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur dictum rutrum elit, eu dictum arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus non porta purus, et molestie ipsum. Sed iaculis faucibus maximus. Duis ut arcu lacinia, porta metus at, dignissim neque. Nam ultrices semper ex a pharetra. Donec lacinia condimentum elit, a hendrerit quam scelerisque vulputate. Quisque dui dolor, pharetra sit amet dictum eu, vehicula a turpis. Nunc pellentesque, erat non egestas viverra, mauris augue vulputate tellus, nec sagittis risus magna et erat. Proin enim sapien, elementum id sapien nec, auctor molestie orci. Pellentesque mattis leo et blandit aliquet.",
            speakers = setOf(
                ConferenceSpeaker(
                    name = "Navn Nevnes",
                    bio = "Mauris pharetra faucibus lorem, id aliquet est egestas eget. In posuere eros nibh, porta iaculis risus laoreet vitae. Quisque vulputate tincidunt mauris in pretium. Phasellus congue sodales rhoncus. Nullam fringilla nisi sapien. Fusce eget ex leo. Fusce non augue augue. Aliquam dictum mattis auctor.",
                    avatarUrl = "https://www.gravatar.com/avatar/333a3587d4c6757b04c86b47fbafc64a?d=mp",
                    twitter = "javabin"
                )
            ),
            format = ConferenceFormat.PRESENTATION,
            room = ConferenceRoom.create("Room 1"),
            scheduled = false
        )
    )

    override val count: Int = values.count()
}
