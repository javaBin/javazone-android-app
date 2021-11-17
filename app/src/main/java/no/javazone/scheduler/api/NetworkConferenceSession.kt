package no.javazone.scheduler.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel

class NetworkConferenceSession(
        private val client: NetworkClient,
        private val dispatchers: CoroutineDispatcher
) : ConferenceSessionApi {
    override suspend fun fetchConferenceSessions(): List<ConferenceSession> =
            withContext(dispatchers) {
                client.getSessions("https://localhost/javazone_2019").toModel()
            }

    override suspend fun fetchConference(): Conference =
            withContext(dispatchers) {
                client.getConference().toModel()
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