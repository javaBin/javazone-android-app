package no.javazone.scheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.ui.ConferenceApp
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sessions: List<ConferenceSession> = remember {
                val jsonStringBuffer = InputStreamReader(assets.open("sessions.json")).readText()
                val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
                dto.toModel()
            }

            ConferenceApp(sessions = sessions)
        }
    }

}
