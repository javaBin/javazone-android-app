package no.javazone.scheduler.repository

import androidx.lifecycle.LiveData
import no.javazone.scheduler.model.ConferenceSession

interface ConferenceSessionRepository {
    suspend fun getSessions(): LiveData<List<ConferenceSession>>
    suspend fun getMySchedule(): LiveData<List<String>>
    suspend fun addSchedule(talkId: String)
}