package no.javazone.scheduler.ui.sessions

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.ConferenceScreen
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

    DetailsContent(
        session = session,
        onBackClick = ConferenceScreen.currentScreen(fromRoute).navigateTo(navController)
    )
}

@Composable
private fun DetailsContent(
    session: ConferenceTalk,
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
                modifier = Modifier
                    .align(alignment = Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onBackClick()
                    }
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
            ) {
                val context = LocalContext.current
                if(session.registrationLink!=null){
                    Text(text = stringResource(R.string.registration_required), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = {

                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(session.registrationLink))
                            context.startActivity(intent)
                        },
                        content = {
                            Text(text = stringResource(R.string.preregistration), style = MaterialTheme.typography.titleMedium)
                        }


                    )
                    //text = session.registrationLink, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(text = stringResource(R.string.description), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = session.summary, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = stringResource(id = R.string.intended_audience), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = session.intendedAudience, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = stringResource(id = R.string.speakers), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                for (speaker in session.speakers) {
                    Row {

                        Column {
                            if (speaker.avatarUrl != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(speaker.avatarUrl),
                                    contentDescription = speaker.name,
                                    modifier = Modifier.size(74.dp)
                                )
                            } else {
                                Image(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = speaker.name,
                                    modifier = Modifier.size(74.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {

                            Text(text = speaker.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(10.dp))
                            speaker.twitter?.let { twitter ->
                                Text(
                                    text = "Twitter: $twitter",
                                    style = MaterialTheme.typography.bodyMedium
                                )
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
@Preview(name = "Light Theme")
fun DetailsContentLightPreview(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {
    DetailsContent(session = session, onBackClick = {})
}

@Composable
@Preview(name = "Dark Theme", uiMode = UI_MODE_NIGHT_YES)
fun DetailsContentDarkPreview(@PreviewParameter(SampleTalkProvider::class) session: ConferenceTalk) {
    JavaZoneTheme(useDarkTheme = true) {
        DetailsContent(session = session, onBackClick = {})
    }
}

class SampleTalkProvider : PreviewParameterProvider<ConferenceTalk> {
    override val values = sequenceOf(
        sampleTalks.first(),
        sampleTalks.last()
    )
}
