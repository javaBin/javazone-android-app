package no.javazone.scheduler.api

import no.javazone.scheduler.model.ConferenceSession

interface ConferenceSessionApi {
    suspend fun fetch(): List<ConferenceSession>
}