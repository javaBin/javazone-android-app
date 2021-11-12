package no.javazone.scheduler.repository

import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.utils.Resource

interface ConferenceRepository {
    fun getSessions(): Flow<Resource<List<ConferenceSession>>>
    fun getMySchedule(): Flow<Resource<Set<String>>>

    suspend fun addOrRemoveSchedule(talkId: String)
}