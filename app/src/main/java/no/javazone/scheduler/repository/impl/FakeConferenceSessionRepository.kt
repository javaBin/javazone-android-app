package no.javazone.scheduler.repository.impl

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.repository.ConferenceSessionRepository
import java.io.InputStreamReader

class FakeConferenceSessionRepository(assets: AssetManager) : ConferenceSessionRepository {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(assets.open("sessions.json")).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    override suspend fun getSessions(): LiveData<List<ConferenceSession>> =
        withContext(Dispatchers.IO) {
            MutableLiveData(sessions)
        }
}
