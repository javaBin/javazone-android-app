package no.javazone.scheduler.api

import android.content.Context
import android.content.res.AssetManager
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import java.io.InputStreamReader

class AssetConferenceSession(
    private val assets: AssetManager
) : ConferenceSessionApi {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(assets.open(ASSET_NAME)).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    override fun fetch(): List<ConferenceSession> = sessions

    companion object {
        private const val ASSET_NAME = "sessions.json"

        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(context: Context): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: AssetConferenceSession(context.assets)
            }

    }
}