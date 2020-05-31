package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConferenceConfigDto(
    val conferenceName: String,
    val workshopDate: String,
    val conferenceDates: List<String>,
    val conferenceUrl: String)