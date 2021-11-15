package no.javazone.scheduler.repository

import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.utils.Resource
import java.time.LocalDate

interface ConferenceRepository {
    fun getSessions(): Flow<Resource<List<ConferenceSession>>>

    fun getConferenceDays(): Flow<List<LocalDate>>

    fun getSchedules(): Flow<List<String>>

    suspend fun addOrRemoveSchedule(talkId: String)
}