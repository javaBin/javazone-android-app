package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConferenceDto(
    val conferenceName: String,
    val workshopDate: String,
    val conferenceDates: List<String>,
    val conferenceUrl: String)