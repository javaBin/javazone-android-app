package no.javazone.scheduler.repository.impl

import android.util.Log
import androidx.room.withTransaction
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

class ConferenceRepositoryImpl private constructor(
    private val db: AppDatabase,
    private val api: ConferenceSessionApi
) : ConferenceRepository {
    private val dao: ConferenceDao = db.sessionDao()

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
            api.fetch()
        },
        saveFetchResult = saveToDb
    )

    override fun getMySchedule(): Flow<Resource<Set<String>>> = TODO()
//        dao.getSchedules().map { SuccessResource(it) }

    override suspend fun addOrRemoveSchedule(talkId: String) {
        db.withTransaction {
            if (dao.deleteSchedule(Schedule(talkId)) == 0) {
                dao.addSchedule(ScheduleEntity(talkId = talkId))
            }
        }
    }

    private val saveToDb: suspend (List<ConferenceSession>) -> Unit = { conferenceSessions ->
        Log.d(LOG_TAG, "saving sessions to db")
        db.withTransaction {
            dao.deleteAllSessions()
            dao.deleteAllRooms()

            val timeSlots = saveTimeSlots(conferenceSessions)
            val rooms = saveRooms(conferenceSessions)
            saveTalkAndSpeakers(conferenceSessions, timeSlots, rooms)
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
        @Volatile
        private var instance: ConferenceRepository? = null

        fun getInstance(db: AppDatabase, api: ConferenceSessionApi): ConferenceRepository =
            instance ?: synchronized(this) {
                instance ?: ConferenceRepositoryImpl(db, api)
                    .also { instance = it }
            }

    }
}
