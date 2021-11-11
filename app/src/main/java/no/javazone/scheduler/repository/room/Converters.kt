package no.javazone.scheduler.repository.room

import androidx.room.TypeConverter
import no.javazone.scheduler.utils.JAVAZONE_DATE_PATTERN
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "Converters"

class Converters {
    @TypeConverter
    fun localDateToString(date: LocalDate): Long =
        date.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN)).toLong()

    @TypeConverter
    fun stringToLocalDate(value: Long): LocalDate =
        LocalDate.parse(value.toString(), DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

    @TypeConverter
    fun offsetDateTimeToTimestamp(date: OffsetDateTime): Long =
        date.toEpochSecond()

    @TypeConverter
    fun timeStampToOffsetDateTime(value: Long): OffsetDateTime =
        OffsetDateTime.from(Instant.ofEpochSecond(value))
}
