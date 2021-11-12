package no.javazone.scheduler.repository.room

import androidx.room.Embedded
import androidx.room.Relation

data class Session(
    @Embedded
    val timeSlot: TimeSlotEntity,
    @Relation(
        entity = TalkEntity::class,
        parentColumn = "time_slot_id",
        entityColumn = "fk_session_slot"
    )
    val talks: List<RoomWithTalk>
)
