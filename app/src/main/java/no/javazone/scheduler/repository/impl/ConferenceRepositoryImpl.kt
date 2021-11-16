package no.javazone.scheduler.repository.impl

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.javazone.scheduler.api.ConferenceSessionApi
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceDao
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.repository.room.*
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.Resource
import no.javazone.scheduler.utils.networkBoundResource
import java.time.LocalDate
import java.time.OffsetDateTime

class ConferenceRepositoryImpl private constructor(
    private val db: AppDatabase,
    private val api: ConferenceSessionApi,
    private val dispatcher: CoroutineDispatcher
) : ConferenceRepository {
    private val dao: ConferenceDao = db.sessionDao()
    private var lastUpdated: OffsetDateTime = OffsetDateTime.MIN

    override fun getSessions(): Flow<Resource<List<ConferenceSession>>> = networkBoundResource(
        query = {
            Log.d(LOG_TAG, "retrieving saved sessions")
            dao.getConferenceSessions()
                .map { sessions ->
                    sessions.map(toConferenceSession())
                }
        },
        fetch = {
            Log.d(LOG_TAG, "fetching sessions")
            api.fetchConferenceSessions()
        },
        saveFetchResult = saveToDb,
        shouldFetch = {
            it.isEmpty() || OffsetDateTime.now().isAfter(lastUpdated.plusMinutes(CACHE_EXPIRE_MIN))
        }
    )

    override fun getConferenceDays(): Flow<List<LocalDate>> {
        Log.d(LOG_TAG, "retrieving saved dates")
        return dao.getTimeSlots()
    }

    override fun getSchedules(): Flow<List<String>> =
        dao.getSchedules()
            .map { schedules ->
                schedules.map { it.talkId }
            }

    override suspend fun addOrRemoveSchedule(talkId: String) {
        db.withTransaction {
            if (dao.deleteSchedule(Schedule(talkId)) == 0) {
                Log.d(LOG_TAG, "Adding $talkId to my schedule")
                dao.addSchedule(ScheduleEntity(talkId = talkId))
            } else {
                Log.d(LOG_TAG, "Deleting $talkId to my schedule")
            }
        }
    }

    private val saveToDb: suspend (List<ConferenceSession>) -> Unit = { conferenceSessions ->
        if (conferenceSessions.isNotEmpty()) {
            Log.d(LOG_TAG, "saving sessions to db")
            db.withTransaction {
                dao.deleteAllSessions()
                dao.deleteAllRooms()

                val timeSlots = saveTimeSlots(conferenceSessions)
                val rooms = saveRooms(conferenceSessions)
                saveTalkAndSpeakers(conferenceSessions, timeSlots, rooms)

                lastUpdated = OffsetDateTime.now()
            }
        }
    }

    private suspend fun saveTimeSlots(conferenceSessions: List<ConferenceSession>): List<TimeSlotEntity> {
        val timeSlots = conferenceSessions
            .map { session ->
                TimeSlotEntity(
                    startTime = session.time,
                    endTime = session.talks.maxOfOrNull { it.endTime } ?: session.time.withHour(17)
                )
            }
            .distinctBy {
                it.timeSlotId
            }
        dao.addTimeSlots(timeSlots)
        return timeSlots
    }

    private suspend fun saveRooms(conferenceSessions: List<ConferenceSession>): List<RoomEntity> {
        val rooms = conferenceSessions
            .map { it.room }
            .distinct()
            .sorted()
            .map { RoomEntity(it.name) }
        dao.addRooms(rooms)
        return rooms
    }

    private suspend fun saveTalkAndSpeakers(
        conferenceSessions: List<ConferenceSession>,
        timeSlots: List<TimeSlotEntity>,
        rooms: List<RoomEntity>
    ) {
        val talks = conferenceSessions.flatMap { it.talks }
        talks.forEach { talk ->
            val room = rooms.first { it.name == talk.room.name }
            val timeSlot = timeSlots.first { it.startTime == talk.slotTime }

            dao.addTalk(talk.toEntity(room, timeSlot))
            talk.speakers.forEach { speaker ->
                val id = dao.addSpeaker(speaker.toEntity())
                dao.addTalkSpeaker(TalkSpeakerCrossRef(talkId = talk.id, speakerId = id))
            }
        }
    }

    companion object {
        private const val CACHE_EXPIRE_MIN = 10L

        @Volatile
        private var instance: ConferenceRepository? = null

        fun getInstance(
            db: AppDatabase,
            api: ConferenceSessionApi,
            dispatcher: CoroutineDispatcher = Dispatchers.IO
        ): ConferenceRepository =
            instance ?: synchronized(this) {
                instance ?: ConferenceRepositoryImpl(db, api, dispatcher)
                    .also { instance = it }
            }

    }
}
