package no.javazone.scheduler.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.utils.APPLICATION_JSON
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkClient {
    @GET("public/config")
    suspend fun getConference(): ConferenceDto

    @GET
    suspend fun getSessions(@Url conferenceUrl: String): SessionsDto

    companion object {
        @ExperimentalSerializationApi
        fun create(baseUrl: String): NetworkClient =
                Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(Json.asConverterFactory(APPLICATION_JSON))
                        .build()
                        .create(NetworkClient::class.java)
    }
}