package no.javazone.scheduler.model

import androidx.room.Embedded
import androidx.room.Relation

data class ConferenceSession(
    @Embedded
    val sessionSlot: ConferenceSlot,
    @Relation(
        entity = Talk::class,
        parentColumn = "id",
        entityColumn = "fk_session_slot"
    )
    val talks: List<SessionTalk>
)
