package no.javazone.scheduler.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.repository.room.*
import java.time.LocalDate

@Dao
interface ConferenceDao {

    @Transaction
    @Query("SELECT * FROM conferences ORDER BY name DESC")
    fun getConferences(): Flow<List<ConferenceWithDates>>

    @Transaction
    @Query("SELECT * FROM time_slots")
    fun getConferenceSessions(): Flow<List<Session>>

    @Transaction
    @Query("SELECT * FROM time_slots WHERE date = :date")
    fun getConferenceSessions(date: LocalDate): Flow<List<Session>>

    @Transaction
    @Query("SELECT * FROM talks")
    fun getConferenceTalks(): Flow<List<TalkEntity>>

    @Query("SELECT DISTINCT date FROM time_slots")
    fun getTimeSlots(): Flow<List<LocalDate>>

    @Query("SELECT * FROM conference_dates")
    fun getConferenceDates(): Flow<List<ConferenceDateEntity>>

    @Query("SELECT talk_id from schedules")
    fun getSchedules(): Flow<List<Schedule>>

    @Transaction
    @Query("DELETE FROM time_slots")
    suspend fun deleteAllSessions()

    @Transaction
    @Query("DELETE FROM rooms")
    suspend fun deleteAllRooms()

    @Delete(entity = ScheduleEntity::class)
    suspend fun deleteSchedule(talkId: Schedule): Int

    @Transaction
    @Query("DELETE FROM conferences")
    suspend fun deleteAllConference()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConference(conference: ConferenceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConferenceDates(dates: List<ConferenceDateEntity>)

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