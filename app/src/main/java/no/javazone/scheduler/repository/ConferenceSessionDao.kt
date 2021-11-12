package no.javazone.scheduler.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.repository.room.*

@Dao
interface ConferenceSessionDao {
    @Transaction
    @Query("SELECT * FROM time_slots")
    fun getConferenceSessions(): Flow<List<Session>>

    @Transaction
    @Query("SELECT * FROM talks")
    fun getConferenceTalks(): Flow<List<TalkEntity>>

    @Query("SELECT * from schedules")
    fun getSchedules(): List<Schedule>

    @Query("DELETE FROM time_slots")
    suspend fun deleteAllSessions()

    @Delete(entity = TalkEntity::class)
    suspend fun deleteSchedule(talkId: Schedule): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTimeSlots(timeSlots: List<TimeSlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRooms(rooms: List<RoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTalk(talk: TalkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpeaker(speaker: SpeakerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTalkSpeaker(relation: TalkSpeakerCrossRef)

    @Insert(entity = ScheduleEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(schedule: ScheduleEntity): Long

    @Query("SELECT schedule_id FROM schedules WHERE rowId = :rowId")
    suspend fun getIdFromRowId(rowId: Long): Long
}