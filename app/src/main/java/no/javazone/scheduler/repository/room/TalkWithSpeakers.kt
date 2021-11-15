package no.javazone.scheduler.repository.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TalkWithSpeakers(
    @Embedded
    val talk: TalkEntity,
    @Relation(
        parentColumn = "talk_id",
        entityColumn = "speaker_id",
        associateBy = Junction(TalkSpeakerCrossRef::class)
    )
    val speakers: List<SpeakerEntity>
)
