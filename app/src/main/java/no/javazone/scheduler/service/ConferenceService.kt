package no.javazone.scheduler.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.utils.APPLICATION_JSON
import no.javazone.scheduler.utils.JAVAZONE_BASE_URL
import retrofit2.Retrofit
import retrofit2.http.GET

interface ConferenceService {
    @GET("public/config")
    suspend fun getConference(): ConferenceDto

    companion object {
        @UnstableDefault
        fun create(): ConferenceService =
            Retrofit.Builder()
                .addConverterFactory(Json.asConverterFactory(APPLICATION_JSON))
                .baseUrl(JAVAZONE_BASE_URL)
                .build()
                .create(ConferenceService::class.java)
    }
}