package no.javazone.scheduler

import android.content.Context
import no.javazone.scheduler.api.AssetConferenceSession
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceSessionRepository
import no.javazone.scheduler.repository.impl.ConferenceSessionRepositoryImpl

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val sessionRepository: ConferenceSessionRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val sessionRepository: ConferenceSessionRepository by lazy {
        ConferenceSessionRepositoryImpl.getInstance(
            db = AppDatabase.getInstance(applicationContext),
            api = AssetConferenceSession.getInstance(applicationContext)
        )
    }
}