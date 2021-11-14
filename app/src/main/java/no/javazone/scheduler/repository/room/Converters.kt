package no.javazone.scheduler.repository.room

import androidx.room.TypeConverter
import no.javazone.scheduler.utils.JAVAZONE_DATE_PATTERN
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "Converters"

class Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun localDateToString(date: LocalDate): String =
        date.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate =
        LocalDate.parse(value, DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

    @TypeConverter
    fun offsetDateTimeToTimestamp(date: OffsetDateTime): String =
        date.format(formatter)

    @TypeConverter
    fun timeStampToOffsetDateTime(value: String): OffsetDateTime =
        OffsetDateTime.parse(value, formatter)
}
