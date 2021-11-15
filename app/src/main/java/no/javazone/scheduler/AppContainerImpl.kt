package no.javazone.scheduler

import android.content.Context
import android.net.ConnectivityManager
import no.javazone.scheduler.api.AssetConferenceSession
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.repository.impl.ConferenceRepositoryImpl

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val repository: ConferenceRepository
    val hasNetwork: Boolean
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val repository: ConferenceRepository by lazy {
        ConferenceRepositoryImpl.getInstance(
            db = AppDatabase.getInstance(applicationContext),
            api = AssetConferenceSession.getInstance(applicationContext)
        )
    }

    override val hasNetwork: Boolean
        get() {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm
                ?.activeNetwork
                ?.let {
                    cm.getNetworkCapabilities(it) != null
                }
                ?: false
        }
}
