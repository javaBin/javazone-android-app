package no.javazone.scheduler.repository

import kotlinx.coroutines.flow.Flow
import no.javazone.scheduler.model.Partner

interface PartnersRepository {
    fun getPartners(): Flow<List<Partner>>
}