package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable

private const val TAG = "dto"

@Serializable
data class SessionsDto(val sessions: List<SessionDto>)

@Serializable
data class SessionDto(
    val intendedAudience: String,
    val endTimeZulu: String,
    val registerLoc: String? = null,
    val format: String,
    val length: Int,
    val language: String,
    val sessionId: String,
    val video: String? = null,
    val abstract: String,
    val title: String,
    val room: String,
    val conferenceId: String,
    val startTimeZulu: String,
    val speakers: List<SpeakerDto>,
    val startTime: String,
    val endTime: String)

@Serializable
data class SpeakerDto(
    val twitter: String? = null,
    val pictureUrl: String? = null,
    val name: String,
    val bio: String)
