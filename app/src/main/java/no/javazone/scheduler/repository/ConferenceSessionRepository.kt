package no.javazone.scheduler.repository

import androidx.lifecycle.LiveData
import no.javazone.scheduler.model.ConferenceSession
import java.time.LocalDate

interface ConferenceSessionRepository {
    fun getSessions(date: LocalDate): LiveData<List<ConferenceSession>>
}