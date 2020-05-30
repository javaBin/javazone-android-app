package no.javazone.scheduler.model

import java.time.OffsetDateTime

data class Session(
    val room: Room,
    val talks: List<Talk>) {
    init {
        assert(talks.isNotEmpty()) {
            "session must have at least one talk"
        }
    }

    constructor(room: Room, talk: Talk) : this(room = room, talks = listOf(talk))

    val startTime: OffsetDateTime?
        get() = talks.map { it.startTime }.min()

    val endTime: OffsetDateTime?
        get() = talks.map { it.endTime }.max()

    val length: Int
        get() = talks.map { it.length }.sum()
}