package no.javazone.scheduler.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toJzString(): String = this.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

fun String.toJzLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))