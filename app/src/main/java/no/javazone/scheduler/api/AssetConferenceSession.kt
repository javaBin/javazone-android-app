package no.javazone.scheduler.api

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.dto.PartnerDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.*
import no.javazone.scheduler.utils.*
import java.io.InputStreamReader

class AssetConferenceSession(
    private val assetManager: AssetManager,
    private val dispatchers: DispatchersProvider
) : ConferenceSessionApi {
    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(assetManager.open(SESSIONS_FILENAME)).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    private val conference: Conference by lazy {
        val json = InputStreamReader(assetManager.open(CONFERENCE_FILENAME)).readText()
        val dto = Json.decodeFromString(ConferenceDto.serializer(), json)
        dto.toModel()
    }

    private val partners: List<Partner> by lazy {
        val json = InputStreamReader(assetManager.open(PARTNERS_FILENAME)).readText()
        val dto = Json.decodeFromString(ListSerializer(PartnerDto.serializer()), json)
        dto.map(dtoToPartner())
    }

    override suspend fun fetchSessions(url: String): List<ConferenceSession> =
        withContext(dispatchers.io) {
            sessions
        }

    override suspend fun fetchConference(): Conference =
        withContext(dispatchers.io) {
            conference
        }

    override suspend fun fetchPartners(): List<Partner> =
        withContext(dispatchers.io) {
            partners
        }

    companion object {
        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(
            context: Context,
            dispatchers: DispatchersProvider = DefaultDispatchersProvider
        ): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: AssetConferenceSession(context.assets, dispatchers)
            }
    }
}