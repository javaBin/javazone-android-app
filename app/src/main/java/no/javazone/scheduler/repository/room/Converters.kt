package no.javazone.scheduler.repository.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Converters {
    @TypeConverter
    fun localDateToString(date: LocalDate): String =
        date.format(DateTimeFormatter.ISO_LOCAL_DATE)

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate =
        try {
            LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (ex: DateTimeParseException) {
            LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"))
        }

    @TypeConverter
    fun offsetDateTimeToTimestamp(date: OffsetDateTime): String =
        date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @TypeConverter
    fun timeStampToOffsetDateTime(value: String): OffsetDateTime =
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
