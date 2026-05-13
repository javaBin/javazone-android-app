package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionsDto(val sessions: List<SessionDto>)

@Serializable
data class SessionDto(
    val intendedAudience: String,
    val registerLoc: String? = null,
    val format: String,
    val length: Int,
    val language: String,
    val sessionId: String,
    val id: String? = null,
    val video: String? = null,
    val abstract: String,
    val title: String,
    val room: String? = null,
    val conferenceId: String? = null,
    val startTimeZulu: String? = null,
    val endTimeZulu: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val suggestedKeywords: String? = null,
    val speakers: List<SpeakerDto>
)

@Serializable
data class SpeakerDto(
    val twitter: String? = null,
    val pictureUrl: String? = null,
    val name: String,
    val bio: String
)
