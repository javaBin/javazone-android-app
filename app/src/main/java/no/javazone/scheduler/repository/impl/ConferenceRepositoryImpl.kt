package no.javazone.scheduler.repository.impl

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.api.ConferenceSessionApi
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceDao
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.repository.room.Schedule
import no.javazone.scheduler.repository.room.ScheduleEntity
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
            Log.d(LOG_TAG, "getting saved sessions")
            //dao.getConferenceSessions()
            TODO()
        },
        fetch = {
            Log.d(LOG_TAG, "parsing sessions")
            api.fetch()
        },
        saveFetchResult = { newSessions ->
            Log.d(LOG_TAG, "saving sessions")
            db.withTransaction {
//                dao.deleteAllSessions()
//                dao.insertAllSessions(newSessions)
            }
        }
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
