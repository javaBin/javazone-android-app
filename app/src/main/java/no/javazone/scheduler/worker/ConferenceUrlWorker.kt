package no.javazone.scheduler.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.utils.CONFERENCE_FILENAME


class ConferenceUrlWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            applicationContext.assets.open(CONFERENCE_FILENAME).use { inputStream ->
                val buffer = String(inputStream.readBytes())
                val json = Json(JsonConfiguration.Stable)
                val dto = json.parse(ConferenceDto.serializer(), buffer)

                return Result.success(
                    Data.Builder()
                        .putString("conferenceName", dto.conferenceName)
                        .putString("url", dto.conferenceUrl)
                        .build()
                )
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error retrieving conference URL", ex)
            return Result.failure()
        }
    }

    companion object {
        const val TAG = "ConferenceUrlWorker"
    }
}
