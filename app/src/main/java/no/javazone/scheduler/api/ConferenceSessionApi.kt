package no.javazone.scheduler.api

import no.javazone.scheduler.model.ConferenceSession

interface ConferenceSessionApi {
    fun fetch(): List<ConferenceSession>
}