package no.javazone.scheduler.repository

import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Schedule
import no.javazone.scheduler.utils.Resource

interface ConferenceSessionRepository {
    fun getSessions(): Flow<Resource<List<ConferenceSession>>>
    fun getMySchedule(): Flow<Resource<Set<String>>>
    suspend fun addOrRemoveSchedule(scheduke: Schedule)
}