package no.javazone.scheduler.api

import no.javazone.scheduler.model.ConferenceSession

interface ConferenceSessionApi {
    suspend fun fetchConferenceSessions(): List<ConferenceSession>
    suspend fun fetchConference(): List<ConferenceSession>
}