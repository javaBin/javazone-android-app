package no.javazone.scheduler.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession

class NetworkConferenceSession(
        private val client: NetworkClient,
        private val dispatchers: CoroutineDispatcher
) : ConferenceSessionApi {
    override suspend fun fetchConferenceSessions(): List<ConferenceSession> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchConference(): Conference {
        TODO("Not yet implemented")
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