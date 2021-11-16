package no.javazone.scheduler.api

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import java.io.InputStreamReader

class AssetConferenceSession(
        private val assetManager: AssetManager,
        private val dispatchers: CoroutineDispatcher
) : ConferenceSessionApi {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(assetManager.open(SESSIONS_FILE)).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    private val conference: Conference by lazy {
        val json = InputStreamReader(assetManager.open(CONFERENCE_FILE)).readText()
        val dto = Json.decodeFromString(ConferenceDto.serializer(), json)
        dto.toModel()
    }

    override suspend fun fetchConferenceSessions(): List<ConferenceSession> =
            withContext(dispatchers) {
                sessions
            }

    override suspend fun fetchConference(): Conference =
            withContext(dispatchers) {
                conference
            }


    companion object {
        private const val SESSIONS_FILE = "sessions.json"
        private const val CONFERENCE_FILE = "conference.json"

        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(
                context: Context,
                dispatchers: CoroutineDispatcher = Dispatchers.IO
        ): ConferenceSessionApi =
                instance ?: synchronized(this) {
                    instance ?: AssetConferenceSession(context.assets, dispatchers)
                }
    }
}