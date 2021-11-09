package no.javazone.scheduler.api

import android.content.Context
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import java.io.InputStream
import java.io.InputStreamReader

class AssetConferenceSession(
    private val inputStream: InputStream
) : ConferenceSessionApi {
    private val sessions: SessionsDto by lazy {
        val jsonStringBuffer = InputStreamReader(inputStream).readText()
        Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
    }

    override fun fetch(): SessionsDto = sessions

    companion object {
        private const val ASSET_NAME = "sessions.json"

        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(context: Context): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: AssetConferenceSession(context.assets.open(ASSET_NAME))
            }

        fun getInstance(inputStream: InputStream): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: AssetConferenceSession(inputStream)
            }

    }
}