package no.javazone.scheduler.api

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import java.io.InputStream
import java.io.InputStreamReader

class AssetConferenceSession(
    private val inputStream: InputStream,
    private val dispatchers: CoroutineDispatcher
) : ConferenceSessionApi {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(inputStream).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    override suspend fun fetch(): List<ConferenceSession> =
        withContext(dispatchers) {
            sessions
        }

    companion object {
        private const val ASSET_NAME = "sessions.json"

        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(
            context: Context,
            dispatchers: CoroutineDispatcher = Dispatchers.IO
        ): ConferenceSessionApi =
            getInstance(context.assets.open(ASSET_NAME), dispatchers)

        fun getInstance(
            inputStream: InputStream,
            dispatchers: CoroutineDispatcher = Dispatchers.IO
        ): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: AssetConferenceSession(inputStream, dispatchers)
            }

    }
}