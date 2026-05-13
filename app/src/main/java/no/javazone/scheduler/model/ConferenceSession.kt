package no.javazone.scheduler.model

import java.time.OffsetDateTime

data class ConferenceSession(
    val time: OffsetDateTime,
    val talks: List<ConferenceTalk>
) {
    init {
        require(talks.isNotEmpty()) { "ConferenceSession must contain at least one talk" }

        talks.forEach {
            it.slotTime = time
        }
    }

    constructor(talk: ConferenceTalk) : this(talk.startTime, listOf(talk))

    val room: ConferenceRoom
        get() = talks.firstOrNull()?.room ?: ConferenceRoom.DEFAULT
}
