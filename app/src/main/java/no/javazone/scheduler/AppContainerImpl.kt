package no.javazone.scheduler

import android.content.Context
import kotlinx.serialization.json.Json
import no.javazone.scheduler.api.ConferenceSessionApi
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceSessionRepository
import no.javazone.scheduler.repository.impl.ConferenceSessionRepositoryImpl
import java.io.InputStreamReader

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

    private val sessions: List<ConferenceSession> by lazy {
        val jsonStringBuffer = InputStreamReader(applicationContext.assets.open("sessions.json")).readText()
        val dto = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)
        dto.toModel()
    }

    override val sessionRepository: ConferenceSessionRepository by lazy {
        ConferenceSessionRepositoryImpl.getInstance(
            db = AppDatabase.getInstance(applicationContext),
            api = object: ConferenceSessionApi {
                override fun fetch(): List<ConferenceSession> {
                    return sessions
                }

            }
        )
    }
}