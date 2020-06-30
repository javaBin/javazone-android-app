package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.net.URI

@Serializable
data class ConferenceDto(
    val conferenceName: String,
    val workshopDate: String,
    val conferenceDates: List<String>,
    val conferenceUrl: String) {

    @Transient
    val conferenceUri: URI by lazy {
        URI.create(conferenceUrl)
    }

    @Transient
    val conferenceUrlPath: String by lazy {
        conferenceUri.path.substringAfterLast('/')
    }
}