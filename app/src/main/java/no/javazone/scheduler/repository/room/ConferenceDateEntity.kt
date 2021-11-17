package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "conference_dates",
    foreignKeys = [
        ForeignKey(
            entity = ConferenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["fk_conference_id"],
            deferred = true,
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ConferenceDateEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "fk_conference_id", index = true)
    val conferenceId: Long
)
