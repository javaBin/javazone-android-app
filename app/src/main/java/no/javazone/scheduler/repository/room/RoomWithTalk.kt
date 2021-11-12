package no.javazone.scheduler.repository.room

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithTalk(
    @Embedded
    val talk: TalkSpeakers,
    @Relation(
        parentColumn = "fk_room",
        entityColumn = "room_id"
    )
    val room: RoomEntity
)