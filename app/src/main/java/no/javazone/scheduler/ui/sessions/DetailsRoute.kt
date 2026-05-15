package no.javazone.scheduler.ui.sessions

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.core.net.toUri
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.ConferenceScreen
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.sampleTalks
import no.javazone.scheduler.utils.toLocalString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsRoute(
    route: String,
    fromRoute: String,
    navController: NavHostController,
    viewModel: ConferenceListViewModel,
    talkId: String,
) {
    Log.d(LOG_TAG, "SessionDetailRoute route: $route, talkId: $talkId, from: $fromRoute")

    val session = viewModel.sessions.collectAsState().value.data
        .flatMap { it.talks }
        .find { it.id == talkId }
        ?: return

    val mySchedule = viewModel.mySchedule.collectAsState().value

    DetailsContent(
        session = session,
        isScheduled = mySchedule.contains(session.id),
        onScheduleToggle = { viewModel.addOrRemoveSchedule(session.id) },
        onBackClick = ConferenceScreen.currentScreen(fromRoute).navigateTo(navController)
    )
}

@Composable
private fun DetailsContent(
    session: ConferenceTalk,
    isScheduled: Boolean,
    onScheduleToggle: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onBackClick()
                    }
                )
                MyScheduleButton(
                    isScheduled = isScheduled,
                    onClick = onScheduleToggle
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Column {
                    Text(
                        text = sessionRoomAndTimeslot(session),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {
                val context = LocalContext.current
                if (session.registrationLink!=null) {
                    Text(text = stringResource(R.string.registration_required), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = {

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(session.registrationLink))
                            context.startActivity(intent)
                        },
                        content = {
                            Text(text = stringResource(R.string.preregistration), style = MaterialTheme.typography.titleMedium)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(text = stringResource(R.string.description), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = session.summary, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(id = R.string.intended_audience), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = session.intendedAudience, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(id = R.string.speakers), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))

                for (speaker in session.speakers) {
                    Row {
                        Image(
                            imageVector = Icons.Filled.Person,
                            contentDescription = speaker.name,
                            modifier = Modifier.size(74.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = speaker.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(10.dp))
                            speaker.twitter?.let { twitter ->
                                val handle = twitter.removePrefix("@")
                                val twitterBaseUrl = stringResource(R.string.social_twitter_base_url)
                                SocialLink(stringResource(R.string.social_twitter), "@$handle", "$twitterBaseUrl$handle")
                            }
                            speaker.bluesky?.let { bluesky ->
                                val handle = bluesky.removePrefix("@")
                                val profile = if ('.' in handle) handle else "$handle.bsky.social"
                                val blueskyBaseUrl = stringResource(R.string.social_bluesky_base_url)
                                SocialLink(stringResource(R.string.social_bluesky), "@$handle", "$blueskyBaseUrl$profile")
                            }
                            speaker.linkedin?.let { linkedin ->
                                val linkedinBaseUrl = stringResource(R.string.social_linkedin_base_url)
                                val url = if (linkedin.startsWith("http")) linkedin else "$linkedinBaseUrl$linkedin"
                                val handle = url.trimEnd('/').substringAfterLast('/')
                                SocialLink(stringResource(R.string.social_linkedin), handle, url)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = speaker.bio, style = MaterialTheme.typography.bodyMedium)
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
            stringResource(id = session.format.label)
}

@Composable
private fun SocialLink(label: String, displayText: String, url: String) {
    val context = LocalContext.current
    Text(
        text = "$label: $displayText",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.clickable {
            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }
    )
}

@Composable
@Preview(name = "Light Theme", showBackground = true)
fun DetailsContentLightPreview(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {
    JavaZoneTheme(useDarkTheme = false) {
        DetailsContent(session = session, isScheduled = false, onScheduleToggle = {}, onBackClick = {})
    }
}

@Composable
@Preview(name = "Dark Theme", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DetailsContentDarkPreview(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {
    JavaZoneTheme(useDarkTheme = true) {
        DetailsContent(session = session, isScheduled = true, onScheduleToggle = {}, onBackClick = {})
    }
}

class SampleTalkProvider : PreviewParameterProvider<ConferenceTalk> {
    override val values = sequenceOf(
        sampleTalks.first(),
        sampleTalks.last()
    )
}
