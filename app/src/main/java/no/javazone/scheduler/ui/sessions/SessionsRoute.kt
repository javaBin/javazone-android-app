package no.javazone.scheduler.ui.sessions

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.ui.components.MyScheduleButton
import no.javazone.scheduler.ui.theme.JavaZoneTypography
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
                Log.d(LOG_TAG, "Filter $$[it.second.id")
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
                horizontalArrangement = Arrangement.Center
            ) {
                groupedSessions.keys.forEach {
                    Button(
                        modifier = Modifier.navigationBarsPadding(bottom = false),
                        onClick = {
                            navController.navigate(route = "$route?day=${it.toJzString()}")
                        },
                    ) {
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }

            LazyColumn {
                items(talks) { (room, talk) ->
                    Row {
                        Column(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(
                                text = "${talk.startTime.toOffsetTime()}-${talk.endTime.toOffsetTime()}",
                                fontSize = 10.sp
                            )
                            Text(
                                text = room.name,
                                fontSize = 10.sp
                            )
                        }
                        Column {
                            Text(
                                text = talk.title,
                                style = JavaZoneTypography.body1
                            )
                            Text(
                                text = talk.speakers.joinToString { it.name },
                                fontSize = 10.sp
                            )
                        }
                        MyScheduleButton(
                            isScheduled = myTalks.contains(talk.id),
                            onClick = {
                                viewModel.addSchedule(talk.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
