package no.javazone.scheduler.repository.impl

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.repository.ConferenceSessionRepository
import no.javazone.scheduler.utils.LOG_TAG
import java.io.InputStreamReader

class FakeConferenceSessionRepository(assets: AssetManager) : ConferenceSessionRepository {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(assets.open("sessions.json")).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }
    private val schedule: MutableLiveData<List<String>> = MutableLiveData()

    override suspend fun getSessions(): LiveData<List<ConferenceSession>> =
        withContext(Dispatchers.IO) {
            Log.d(LOG_TAG, "Retrieve static sessions")
            MutableLiveData(sessions)
        }

    override suspend fun getMySchedule(): LiveData<List<String>> =
        withContext(Dispatchers.IO) {
            schedule
        }

    override suspend fun addOrRemoveSchedule(talkId: String) {
        withContext(Dispatchers.IO) {
            val tmp = schedule.value?.toMutableSet() ?: mutableSetOf()
            if (!tmp.add(talkId)) {
                Log.d(LOG_TAG, "Remove talk $talkId to mySchedule")
                tmp.remove(talkId)
            } else {
                Log.d(LOG_TAG, "Adding talk $talkId to mySchedule")
            }
            schedule.value = tmp.toList()
        }
    }
}
