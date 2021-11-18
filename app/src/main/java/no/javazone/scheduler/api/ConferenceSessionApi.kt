package no.javazone.scheduler.api

import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Partner

interface ConferenceSessionApi {
    suspend fun fetchSessions(url: String): List<ConferenceSession>
    suspend fun fetchConference(): Conference
    suspend fun fetchPartners(): List<Partner>
}