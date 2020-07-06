package no.javazone.scheduler.repository.impl

import androidx.lifecycle.LiveData
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.Talk
import no.javazone.scheduler.repository.ConferenceSessionRepository
import java.time.LocalDate

class ConferenceSessionRepositoryImpl private constructor(private val dao: ConferenceSessionDao)
    : ConferenceSessionRepository{

    override fun getSessions(date: LocalDate): LiveData<List<ConferenceSession>> =
        dao.getConferenceSessionsForDate(date)

    override fun getTalk(talkId: String): LiveData<Talk> {
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
