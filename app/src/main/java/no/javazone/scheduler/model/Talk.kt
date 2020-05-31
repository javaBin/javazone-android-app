package no.javazone.scheduler.model

import java.time.OffsetDateTime


sealed class Talk(
    open val sessionId: String,
    open val title: String,
    open val startTime: OffsetDateTime,
    open val endTime: OffsetDateTime,
    open val length: Int,
    open val intendedAudience: String,
    open val language: String,
    open val video: String?,
    open val abstract: String,
    open val speakers: Set<Speaker>
) {
    abstract val format: String
}

data class Presentation(
    override val sessionId: String,
    override val title: String,
    override val startTime: OffsetDateTime,
    override val endTime: OffsetDateTime,
    override val length: Int,
    override val intendedAudience: String,
    override val language: String,
    override val video: String?,
    override val abstract: String,
    override val speakers: Set<Speaker>) : Talk(sessionId, title, startTime, endTime, length, intendedAudience, language, video, abstract, speakers) {

    override val format: String
        get() = "presentation"
}

data class Lightning(
    override val sessionId: String,
    override val title: String,
    override val startTime: OffsetDateTime,
    override val endTime: OffsetDateTime,
    override val length: Int,
    override val intendedAudience: String,
    override val language: String,
    override val video: String?,
    override val abstract: String,
    override val speakers: Set<Speaker>) : Talk(sessionId, title, startTime, endTime, length, intendedAudience, language, video, abstract, speakers) {

    override val format: String
        get() = "lightning-talk"
}

data class Workshop(
    override val sessionId: String,
    override val title: String,
    override val startTime: OffsetDateTime,
    override val endTime: OffsetDateTime,
    override val length: Int,
    override val intendedAudience: String,
    override val language: String,
    override val video: String?,
    override val abstract: String,
    override val speakers: Set<Speaker>) : Talk(sessionId, title, startTime, endTime, length, intendedAudience, language, video, abstract, speakers) {

    override val format: String
        get() = "workshop"
}