package no.javazone.scheduler.repository.impl

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.javazone.scheduler.api.ConferenceSessionApi
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Schedule
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceSessionRepository
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.Resource
import no.javazone.scheduler.utils.SuccessResource
import no.javazone.scheduler.utils.networkBoundResource

class ConferenceSessionRepositoryImpl private constructor(
    private val db: AppDatabase,
    private val api: ConferenceSessionApi
) : ConferenceSessionRepository {
    private val dao: ConferenceSessionDao = db.sessionDao()

    override fun getSessions(): Flow<Resource<List<ConferenceSession>>> = networkBoundResource(
        query = {
            Log.d(LOG_TAG, "getting saved sessions")
            dao.getConferenceSessions()
        },
        fetch = {
            Log.d(LOG_TAG, "parsing sessions")
            api.fetch()
        },
        saveFetchResult = { newSessions ->
            Log.d(LOG_TAG, "saving sessions")
            db.withTransaction {
                dao.deleteAllSessions()
                dao.insertAllSessions(newSessions)
            }
        }
    )

    override fun getMySchedule(): Flow<Resource<Set<String>>> =
        dao.getSchedules().map { SuccessResource(it) }

    override suspend fun addOrRemoveSchedule(schedule: Schedule) {
        db.withTransaction {
            if (dao.deleteSchedule(schedule) == 0) {
                dao.addSchedule(schedule)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ConferenceSessionRepository? = null

        fun getInstance(db: AppDatabase, api: ConferenceSessionApi): ConferenceSessionRepository =
            instance ?: synchronized(this) {
                instance ?: ConferenceSessionRepositoryImpl(db, api)
                    .also { instance = it }
            }

    }
}
