package no.javazone.scheduler.repository.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.model.Schedule

@Dao
interface ConferenceSessionDao {
    @Transaction
    @Query("SELECT * FROM time_slots")
    fun getConferenceSessions(): Flow<List<ConferenceSession>>

    @Transaction
    @Query("SELECT * FROM time_slots")
    fun getConferenceTalks(): Flow<List<ConferenceTalk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSessions(sessions: List<ConferenceSession>)

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT talk_id FROM schedules")
    fun getSchedules(): Flow<Set<String>>

    @Delete
    suspend fun deleteSchedule(schedule: Schedule): Int

    @Insert(entity = Schedule::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(schedule: Schedule)
}