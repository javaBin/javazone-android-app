package no.javazone.scheduler.api

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.utils.LOG_TAG

class NetworkConferenceSession(
    private val client: NetworkClient,
    private val dispatchers: CoroutineDispatcher
) : ConferenceSessionApi {
    override suspend fun fetchSessions(url: String): List<ConferenceSession> =
        withContext(dispatchers) {
            val sessions = client.getSessions(url)
            Log.d(LOG_TAG, "Retrieved ${sessions.sessions.size} from $url")
            sessions.toModel()
        }

    override suspend fun fetchConference(): Conference =
        withContext(dispatchers) {
            val conference = client.getConference()
            Log.d(LOG_TAG, "Retrieve ${conference.conferenceName} conference")
            conference.toModel()
        }

    companion object {
        @Volatile
        private var instance: ConferenceSessionApi? = null

        fun getInstance(
            client: NetworkClient,
            dispatchers: CoroutineDispatcher = Dispatchers.IO
        ): ConferenceSessionApi =
            instance ?: synchronized(this) {
                instance ?: NetworkConferenceSession(client, dispatchers)
            }
    }
}