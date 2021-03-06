package no.javazone.scheduler.worker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.repository.AppDatabase
import no.javazone.scheduler.service.ConferenceService
import no.javazone.scheduler.service.SessionService
import no.javazone.scheduler.utils.APP_PREFERENCE_FILE
import no.javazone.scheduler.utils.CONFERENCE_FILENAME
import no.javazone.scheduler.utils.JAVAZONE_DATE_PATTERN
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ConferenceUrlWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val conferenceService: ConferenceService by lazy {
        ConferenceService.create()
    }
    private lateinit var conference: ConferenceDto

    @UnstableDefault
    override suspend fun doWork(): Result {
        try {
            if (!this::conference.isInitialized) {
                initConference()
            }
            val sessionService = SessionService.create()
            val sessionsDto = sessionService.getSessions(conference.conferenceUrlPath)
            val sessions = sessionsDto.toModel()

            val database = AppDatabase.getInstance(context)
            database.sessionDao().insertAll(sessions)

            return Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error retrieving conference URL", ex)
            return Result.failure()
        }
    }

    @UnstableDefault
    private suspend fun initConference() {
        try {
            conference = conferenceService.getConference()
        } catch (ex: Exception) {
            applicationContext.assets.open(CONFERENCE_FILENAME).use { inputStream ->
                val buffer = String(inputStream.readBytes())
                val json = Json(JsonConfiguration.Stable)
                conference = json.parse(ConferenceDto.serializer(), buffer)
            }
        }
        context.getSharedPreferences(APP_PREFERENCE_FILE, MODE_PRIVATE)
            .edit()
            .putString("conference_name", conference.conferenceName)
            .putString(
                "workshop", LocalDate.parse(
                    conference.workshopDate,
                    DateTimeFormatter.ofPattern(CONFERENCE_PATTERN)
                ).format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))
            )
            .putString("dates", conference.conferenceDates
                .map {
                    LocalDate.parse(it, DateTimeFormatter.ofPattern(CONFERENCE_PATTERN))
                }
                .map {
                    it.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))
                }
                .joinToString())
            .putString("url", conference.conferenceUrl)
            .apply()
    }

    companion object {
        private const val TAG = "ConferenceUrlWorker"
        private const val CONFERENCE_PATTERN = "dd.MM.yyyy"
    }
}
