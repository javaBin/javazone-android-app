package no.javazone.scheduler.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.javazone.scheduler.api.ConferenceSessionApi
import no.javazone.scheduler.model.Partner
import no.javazone.scheduler.repository.PartnersRepository

class PartnersRepositoryImpl(private val assetApi: ConferenceSessionApi) : PartnersRepository {
    override fun getPartners(): Flow<List<Partner>> = flow {
        emit(assetApi.fetchPartners())
    }

    companion object {
        @Volatile
        private var instance: PartnersRepository? = null

        fun getInstance(
            assetApi: ConferenceSessionApi
        ): PartnersRepository =
            instance ?: synchronized(this) {
                instance ?: PartnersRepositoryImpl(assetApi)
                    .also { instance = it }
            }

    }
}