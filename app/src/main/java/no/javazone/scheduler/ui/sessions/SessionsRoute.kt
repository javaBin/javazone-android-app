package no.javazone.scheduler.ui.sessions

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.JavaZoneDestinations
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.SessionTimeFormat
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.toJzString
import no.javazone.scheduler.viewmodels.ConferenceListViewModel
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionsRoute(
    navController: NavHostController,
    route: String,
    viewModel: ConferenceListViewModel,
    day: LocalDate?,
    myScheduleOnly: Boolean = false
) {
    Log.d(LOG_TAG, "route: $route")
    val scaffoldState = rememberScaffoldState()

    val sessions: List<ConferenceSession> = viewModel.sessions.value
    if (sessions.isEmpty()) {
        return
    }
    val groupedSessions = sessions.groupBy { it.date }.toSortedMap()
    val selectedDay = day ?: groupedSessions.firstKey()
    val filtered = sessions.filter {
        it.date == selectedDay
    }
    val groupedBySessionSlot = filtered.groupBy { it.startTime }

    Log.d(LOG_TAG, "Number of sessions ${filtered.size}")
    val myTalks = viewModel.mySchedule.value

    Log.d(LOG_TAG, "Number of interested talks: ${myTalks.size}")

    val talks = filtered
        .flatMap { session ->
            session.talks.map {
                session.room to it
            }
        }
        .filter {
            if (myScheduleOnly) {
                Log.d(LOG_TAG, "Filter ${it.second.id}")
                myTalks.contains(it.second.id)
            } else {
                Log.d(LOG_TAG, "No filter")
                true
            }
        }

    Log.d(LOG_TAG, "Number of talks: ${talks.size}")

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                groupedSessions.keys.forEach {
                    OutlinedButton(
                        modifier = Modifier
                            .selectable(
                                selected = it == day,
                                role = Role.Button,
                                onClick = {}
                            )
                            .navigationBarsPadding(bottom = false),
                        onClick = {
                            Log.d("NavController debug","$route")
                            navController.navigate(route = "$route?day=${it.toJzString()}")
                        },
                    ) {
                        Text(
                            text = it.toString(),
                            style = JavaZoneTypography.button,
                            color = if (it == day) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant

                        )
                    }
                }
            }


            val talksGroupedOnStart = talks.groupBy { it.second.startTime }

            LazyColumn {
                talksGroupedOnStart.forEach { (startTime, talks) ->
                    stickyHeader {

                        Row(modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                Text(
                                    SessionTimeFormat.format(startTime),
                                    fontSize = 27.sp
                                )
                            }
                        }
                    }

                    items(talks) { (room, talk) ->
                        Row(
                            modifier = Modifier
                                .padding(1.dp)
                                .border(width = 2.dp, color = MaterialTheme.colors.onSecondary)
                                .fillMaxWidth()
                                .clickable( onClick = {
                                    Log.w("SessionviewDebug","Session is ${talk.id}")
                                    //navController.navigate(deepLink= "detail_session/${talk.id}"
                                    //navController.navigate(deepLink= Uri.parse("android-app://androidx.navigation/detail_session/${talk.id}"))
                                    navController.navigate(route="${JavaZoneDestinations.SESSION_ROUTE}?id=${talk.id}")
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
                                    text = room.name,
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
                            IconButton(onClick = { /*TODO*/ }) {
                                MyScheduleButton(
                                    isScheduled = myTalks.contains(talk.id),
                                    onClick = {
                                        viewModel.addOrRemoveSchedule(talk.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
