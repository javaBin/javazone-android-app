package no.javazone.scheduler.api

import android.util.Log
import kotlinx.coroutines.withContext
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Partner
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.utils.DefaultDispatchersProvider
import no.javazone.scheduler.utils.DispatchersProvider
import no.javazone.scheduler.utils.LOG_TAG

class NetworkConferenceSession(
    private val client: NetworkClient,
    private val dispatchers: DispatchersProvider
) : ConferenceSessionApi {
    override suspend fun fetchSessions(url: String): List<ConferenceSession> =
        withContext(dispatchers.io) {
            val sessions = client.getSessions(url)
            Log.d(LOG_TAG, "Retrieved ${sessions.sessions.size} from $url")
            sessions.toModel()
        }

    override suspend fun fetchConference(): Conference =
        withContext(dispatchers.io) {
            val conference = client.getConference()
            Log.d(LOG_TAG, "Retrieve ${conference.conferenceName} conference")
            conference.toModel()
        }

    override suspend fun fetchPartners(): List<Partner> =
        throw NotImplementedError("Partners doesn't have an url")

    companion object {
        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(
            client: NetworkClient,
            dispatchers: DispatchersProvider = DefaultDispatchersProvider
        ): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: NetworkConferenceSession(client, dispatchers)
            }
    }
}