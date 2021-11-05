package no.javazone.scheduler.repository.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Schedule

@Dao
interface ConferenceSessionDao {
    @Query("SELECT * FROM sessions")
    fun getConferenceSessions(): Flow<List<ConferenceSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSessions(sessions: List<ConferenceSession>)

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT * FROM schedules")
    fun getSchedules(): Flow<Set<Schedule>>

    @Query("SELECT * FROM schedules WHERE talk_id = :talkId")
    fun findSchedule(talkId: String): Schedule?

    @Delete
    suspend fun deleteSchedule(schedule: Schedule): Int

    @Insert(entity = Schedule::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(schedule: Schedule)
}