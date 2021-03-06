package no.javazone.scheduler.repository.impl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import no.javazone.scheduler.model.ConferenceSession
import java.time.LocalDate

@Dao
interface ConferenceSessionDao {
    @Query("SELECT * from sessions where date = :date")
    fun getConferenceSessionsForDate(date: LocalDate): LiveData<List<ConferenceSession>>

    @Query("SELECT * FROM sessions")
    fun getConferenceSessions(): LiveData<List<ConferenceSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<ConferenceSession>)
}