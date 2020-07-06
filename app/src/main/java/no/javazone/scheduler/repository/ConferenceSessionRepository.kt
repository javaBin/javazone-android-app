package no.javazone.scheduler.repository

import androidx.lifecycle.LiveData
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Talk
import java.time.LocalDate

interface ConferenceSessionRepository {
    fun getSessions(date: LocalDate): LiveData<List<ConferenceSession>>
    fun getSession(sessionId: Long): LiveData<ConferenceSession>
    fun getTalk(talkId: String): LiveData<Talk>
}