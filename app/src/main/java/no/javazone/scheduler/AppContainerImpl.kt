package no.javazone.scheduler

import android.content.Context
import android.net.ConnectivityManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import kotlinx.serialization.ExperimentalSerializationApi
import no.javazone.scheduler.api.AssetConferenceSession
import no.javazone.scheduler.api.NetworkClient
import no.javazone.scheduler.api.NetworkConferenceSession
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.repository.PartnersRepository
import no.javazone.scheduler.repository.impl.ConferenceRepositoryImpl
import no.javazone.scheduler.repository.impl.PartnersRepositoryImpl
import no.javazone.scheduler.utils.JAVAZONE_BASE_URL

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val repository: ConferenceRepository
    val partnersRepository: PartnersRepository
    val hasNetwork: Boolean
    val imageLoader: ImageLoader
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
@OptIn(ExperimentalSerializationApi::class)
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val repository: ConferenceRepository by lazy {
        ConferenceRepositoryImpl.getInstance(
            db = AppDatabase.getInstance(applicationContext),
            api = NetworkConferenceSession.getInstance(
                client = NetworkClient.create(JAVAZONE_BASE_URL)
            ),
            assetApi = null
        )
    }

    override val partnersRepository: PartnersRepository by lazy {
        PartnersRepositoryImpl.getInstance(
            assetApi = AssetConferenceSession.getInstance(applicationContext)
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

    override val imageLoader: ImageLoader by lazy {
        ImageLoader.Builder(applicationContext)
            .componentRegistry {
                add(SvgDecoder(applicationContext))
            }
            .build()
    }
}
