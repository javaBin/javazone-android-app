package no.javazone.scheduler.repository.impl

import androidx.lifecycle.LiveData
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.repository.ConferenceSessionRepository

class ConferenceSessionRepositoryImpl private constructor(private val dao: ConferenceSessionDao)
    : ConferenceSessionRepository{

    override suspend fun getSessions(): LiveData<List<ConferenceSession>> =
        dao.getConferenceSessionsForDate()

    override suspend fun getMySchedule(): LiveData<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun addOrRemoveSchedule(talkId: String) {
        TODO("Not yet implemented")
    }

    companion object {
        @Volatile
        private lateinit var instance: ConferenceSessionRepositoryImpl

        fun getInstance(dao: ConferenceSessionDao): ConferenceSessionRepository =
            if (this::instance.isInitialized) {
                instance
            } else {
                synchronized(this) {
                    if (this::instance.isInitialized) {
                        instance
                    } else {
                        ConferenceSessionRepositoryImpl(dao)
                            .also { instance = it }
                    }
                }
            }
    }
}
