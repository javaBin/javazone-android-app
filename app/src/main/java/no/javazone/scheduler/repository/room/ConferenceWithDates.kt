package no.javazone.scheduler.repository.room

import androidx.room.Embedded
import androidx.room.Relation

data class ConferenceWithDates(
    @Embedded
    val conference: ConferenceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "fk_conference_id"
    )
    val days: List<ConferenceDateEntity>
)
