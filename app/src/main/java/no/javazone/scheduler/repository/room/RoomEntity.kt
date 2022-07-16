package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.math.abs

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "name")
    val name: String
) {
    @Ignore
    constructor(
        name: String
    ) : this(roomId = abs(name.hashCode()), name = name)
}
