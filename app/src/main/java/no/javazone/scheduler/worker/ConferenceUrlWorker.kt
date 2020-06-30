package no.javazone.scheduler.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.model.toModel
import no.javazone.scheduler.service.ConferenceService
import no.javazone.scheduler.service.SessionService
import no.javazone.scheduler.utils.CONFERENCE_FILENAME


class ConferenceUrlWorker(private val context: Context, private val workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val conferenceService: ConferenceService by lazy {
        ConferenceService.create()
    }
    private lateinit var conference: ConferenceDto

    override suspend fun doWork(): Result {
        try {
            if (!this::conference.isInitialized) {
                initConference()
            }
            val sessionService = SessionService.create()
            val sessionsDto = sessionService.getSessions(conference.conferenceUrlPath)
            val sessions = sessionsDto.toModel()

            return Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error retrieving conference URL", ex)
            return Result.failure()
        }
    }

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
    }

    companion object {
        const val TAG = "ConferenceUrlWorker"
    }
}
