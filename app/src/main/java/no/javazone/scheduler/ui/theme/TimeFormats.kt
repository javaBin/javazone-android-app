package no.javazone.scheduler.ui.theme

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


val SessionTimeFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

val SessionDateFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

val SessionDateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

val SessionDayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE")
