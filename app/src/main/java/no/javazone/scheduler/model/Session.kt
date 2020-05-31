package no.javazone.scheduler.model

import no.javazone.scheduler.BuildConfig
import java.time.OffsetDateTime

data class Session(
    val room: Room,
    val talks: List<Talk>) {
    init {
        if (BuildConfig.DEBUG && talks.isEmpty()) {
            error("session must have at least one talk")
        }
    }

    constructor(room: Room, talk: Talk) : this(room = room, talks = listOf(talk))

    val startTime: OffsetDateTime
        get() = talks.map { it.startTime }.min() ?: OffsetDateTime.MIN

    val endTime: OffsetDateTime
        get() = talks.map { it.endTime }.max() ?: OffsetDateTime.MAX

    val length: Int
        get() = talks.map { it.length }.sum()
}