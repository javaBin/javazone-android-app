package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo

data class Schedule(
    @ColumnInfo(name = "talk_id")
    val talkId: String
)
