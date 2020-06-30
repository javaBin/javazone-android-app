package no.javazone.scheduler.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.utils.APPLICATION_JSON
import no.javazone.scheduler.utils.JAVAZONE_BASE_URL
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface SessionService {
    @GET("public/allSessions/{conference}")
    suspend fun getSessions(@Path("conference") conference: String): SessionsDto

    companion object {
        @OptIn(UnstableDefault::class)
        fun create(baseUrl: String = JAVAZONE_BASE_URL): SessionService {
            return Retrofit.Builder()
                .addConverterFactory(Json.asConverterFactory(APPLICATION_JSON))
                .baseUrl(baseUrl)
                .build()
                .create(SessionService::class.java)
        }
    }
}