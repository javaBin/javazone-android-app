package no.javazone.scheduler.ui.schedules

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.components.JavaZoneDestinations
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.SessionDateTimeFormat
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.OffsetDateTime

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
        navController = navController,
        onToggleSchedule = { talkId -> viewModel.addOrRemoveSchedule(talkId) },
        conferenceTalks = viewModel.selectMySchedule(
            resource.data,
            mySchedule
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyScheduleScreen(
    navController: NavHostController,
    onToggleSchedule: (String) -> Unit,
    conferenceTalks: Map<OffsetDateTime, List<ConferenceTalk>>
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        LazyColumn {
            conferenceTalks.forEach { (slot, talks) ->
                stickyHeader {
                   Row(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(
                                SessionDateTimeFormat.format(slot),
                                fontSize = 27.sp
                            )
                        }
                    }
                }

                items(talks) { talk ->
                    Row(
                        modifier = Modifier
                            .padding(1.dp)
                            .border(width = 2.dp, color = MaterialTheme.colors.onSecondary)
                            .fillMaxWidth()
                            .clickable(onClick = {
                                Log.w("SessionviewDebug", "Session is ${talk}")
                                //navController.navigate(deepLink= "detail_session/${talk.id}"
                                //navController.navigate(deepLink= Uri.parse("android-app://androidx.navigation/detail_session/${talk.id}"))
                                navController.navigate(route = "${JavaZoneDestinations.SESSION_ROUTE}?id=${talk.id}")
                            })

                    ) {
                        Column(
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Text(
                                text = SessionTimeFormat.format(talk.startTime) + " - " + SessionTimeFormat.format(
                                    talk.endTime
                                ),
                                fontSize = 10.sp
                            )
                            Text(
                                text = talk.room.name,
                                fontSize = 10.sp
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
                                style = JavaZoneTypography.body1
                            )
                            Text(
                                text = talk.speakers.joinToString { it.name },
                                fontSize = 10.sp
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