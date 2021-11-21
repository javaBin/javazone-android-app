package no.javazone.scheduler.api

import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession

interface ConferenceSessionApi {
    suspend fun fetchSessions(url: String): List<ConferenceSession>
    suspend fun fetchConference(): Conference
}