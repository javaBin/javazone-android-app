package no.javazone.scheduler.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ConferenceTalk(
    @Embedded
    val talk: Talk,
    @Relation(
        parentColumn = "fk_room",
        entityColumn = "key"
    )
    val room: ConferenceRoom,
    @Relation(
        parentColumn = "fk_session_start_time",
        entityColumn = "id"
    )
    val sessionSlot: ConferenceSlot,
    @Relation(
        parentColumn = "id",
        entityColumn = "talk_id"
    )
    val mySchedule: Schedule?,
    @Relation(
        parentColumn = "talk_id",
        entityColumn = "speaker_id",
        associateBy = Junction(TalkSpeakerCrossRef::class)
    )
    val speakers: List<Speaker>
)
