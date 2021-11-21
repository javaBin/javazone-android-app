package no.javazone.scheduler.repository

import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.utils.Resource

interface ConferenceRepository {
    fun getConference(): Flow<Resource<Conference>>

    fun getSessions(): Flow<Resource<List<ConferenceSession>>>

    fun getSchedules(): Flow<List<String>>

    suspend fun addOrRemoveSchedule(talkId: String)
}