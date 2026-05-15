package no.javazone.scheduler.utils

import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceRoom
import no.javazone.scheduler.model.ConferenceSpeaker
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.model.Partner
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDate.toJzString(): String = this.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

fun String.toJzLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

fun OffsetDateTime.toLocalString(formatter: DateTimeFormatter): String {
    val zoned = this.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
    return zoned.format(formatter)
}

/**
 * Formats all conference days (workshop + conference) as a compact date range.
 * Same-month example : "2–4 September 2025"
 * Cross-month example : "31 Aug – 2 Sep 2025"
 * Returns an empty string while data is still loading.
 */
fun List<ConferenceDate>.formatDateRange(): String {
    if (isEmpty()) return ""
    val sorted = sortedBy { it.date }
    val first = sorted.first().date
    val last = sorted.last().date
    return if (first.month == last.month && first.year == last.year) {
        val month = first.format(DateTimeFormatter.ofPattern("MMMM"))
        "${first.dayOfMonth}–${last.dayOfMonth} $month ${first.year}"
    } else {
        val fmt = DateTimeFormatter.ofPattern("d MMM")
        "${first.format(fmt)} – ${last.format(fmt)} ${last.year}"
    }
}

/**
 * Finds the workshop day (label == "workshop") and formats it as
 * "Tuesday, 2 September 2025". Returns an empty string while loading.
 */
fun List<ConferenceDate>.formatWorkshopDate(): String {
    val workshop = firstOrNull { it.label == "workshop" } ?: return ""
    return workshop.date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))
}

val samplePartners = listOf(
    Partner(
        name = "Foo Inc",
        homepageUrl = "https://wwww.vg.no",
        logoUrl = "https://d3o108dy577i1m.cloudfront.net/2019/logos/systek.svg"
    ),
    Partner(
        name = "Bar Inc",
        homepageUrl = "https://www.nettavisen.no",
        logoUrl = "https://d3o108dy577i1m.cloudfront.net/2020/logos/storebrand.png"
    ),
    Partner(
        name = "Foo Inc",
        homepageUrl = "https://wwww.vg.no",
        logoUrl = ""
    ),
    Partner(
        name = "Bar Inc",
        homepageUrl = "https://www.nettavisen.no",
        logoUrl = ""
    ),
    Partner(
        name = "Foo Inc",
        homepageUrl = "https://wwww.vg.no",
        logoUrl = ""
    ),
    Partner(
        name = "Bar Inc",
        homepageUrl = "https://www.nettavisen.no",
        logoUrl = ""
    ),
    Partner(
        name = "Foo Inc",
        homepageUrl = "https://wwww.vg.no",
        logoUrl = ""
    ),
    Partner(
        name = "Bar Inc",
        homepageUrl = "https://www.nettavisen.no",
        logoUrl = ""
    ),
)

val sampleTalks = listOf(
    ConferenceTalk(
        "19F59B3A-2DF9-499B-940E-D6CA20E00840",
        title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        startTime = OffsetDateTime.now().minusHours(1),
        endTime = OffsetDateTime.now().plusHours(1),
        length = 45,
        intendedAudience = "Beginner",
        language = "Latin",
        video = "https://vimeo.com/253989945",
        summary = "Cras posuere hendrerit lorem a lacinia. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur dictum rutrum elit, eu dictum arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus non porta purus, et molestie ipsum. Sed iaculis faucibus maximus. Duis ut arcu lacinia, porta metus at, dignissim neque. Nam ultrices semper ex a pharetra. Donec lacinia condimentum elit, a hendrerit quam scelerisque vulputate. Quisque dui dolor, pharetra sit amet dictum eu, vehicula a turpis. Nunc pellentesque, erat non egestas viverra, mauris augue vulputate tellus, nec sagittis risus magna et erat. Proin enim sapien, elementum id sapien nec, auctor molestie orci. Pellentesque mattis leo et blandit aliquet.",
        speakers = setOf(
            ConferenceSpeaker(
                name = "Navn Nevnes",
                bio = "Mauris pharetra faucibus lorem, id aliquet est egestas eget. In posuere eros nibh, porta iaculis risus laoreet vitae. Quisque vulputate tincidunt mauris in pretium. Phasellus congue sodales rhoncus. Nullam fringilla nisi sapien. Fusce eget ex leo. Fusce non augue augue. Aliquam dictum mattis auctor.",
                avatarUrl = "https://www.gravatar.com/avatar/333a3587d4c6757b04c86b47fbafc64a?d=mp",
                twitter = "javabin"
            )
        ),
        format = ConferenceFormat.PRESENTATION,
        room = ConferenceRoom.create("Room 1"),
        scheduled = true,
        registrationLink = null,
        suggestedKeywords = listOf("Kotlin", "Android", "Jetpack Compose")
    ),
    ConferenceTalk(
        "19F59B3A-2DF9-499B-940E-D6CA20E00840",
        title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        startTime = OffsetDateTime.now().plusHours(1),
        endTime = OffsetDateTime.now().plusHours(3),
        length = 60,
        intendedAudience = "Beginner",
        language = "Latin",
        video = "https://vimeo.com/253989945",
        summary = "Cras posuere hendrerit lorem a lacinia. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur dictum rutrum elit, eu dictum arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus non porta purus, et molestie ipsum. Sed iaculis faucibus maximus. Duis ut arcu lacinia, porta metus at, dignissim neque. Nam ultrices semper ex a pharetra. Donec lacinia condimentum elit, a hendrerit quam scelerisque vulputate. Quisque dui dolor, pharetra sit amet dictum eu, vehicula a turpis. Nunc pellentesque, erat non egestas viverra, mauris augue vulputate tellus, nec sagittis risus magna et erat. Proin enim sapien, elementum id sapien nec, auctor molestie orci. Pellentesque mattis leo et blandit aliquet.",
        speakers = setOf(
            ConferenceSpeaker(
                name = "Navn Nevnes",
                bio = "Mauris pharetra faucibus lorem, id aliquet est egestas eget. In posuere eros nibh, porta iaculis risus laoreet vitae. Quisque vulputate tincidunt mauris in pretium. Phasellus congue sodales rhoncus. Nullam fringilla nisi sapien. Fusce eget ex leo. Fusce non augue augue. Aliquam dictum mattis auctor.",
                avatarUrl = "https://www.gravatar.com/avatar/333a3587d4c6757b04c86b47fbafc64a?d=mp",
                twitter = "javabin"
            )
        ),
        format = ConferenceFormat.PRESENTATION,
        room = ConferenceRoom.create("Room 1"),
        scheduled = false,
        registrationLink = null,
        suggestedKeywords = listOf("Java", "JVM", "Core")
    ),
    ConferenceTalk(
        "19F59B3A-2DF9-499B-940E-D6CA20E00840",
        title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        startTime = OffsetDateTime.now().plusHours(3),
        endTime = OffsetDateTime.now().plusHours(5),
        length = 120,
        intendedAudience = "Beginner",
        language = "Latin",
        video = null,
        summary = "Cras posuere hendrerit lorem a lacinia. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur dictum rutrum elit, eu dictum arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus non porta purus, et molestie ipsum. Sed iaculis faucibus maximus. Duis ut arcu lacinia, porta metus at, dignissim neque. Nam ultrices semper ex a pharetra. Donec lacinia condimentum elit, a hendrerit quam scelerisque vulputate. Quisque dui dolor, pharetra sit amet dictum eu, vehicula a turpis. Nunc pellentesque, erat non egestas viverra, mauris augue vulputate tellus, nec sagittis risus magna et erat. Proin enim sapien, elementum id sapien nec, auctor molestie orci. Pellentesque mattis leo et blandit aliquet.",
        speakers = setOf(
            ConferenceSpeaker(
                name = "Navn Nevnes",
                bio = "Mauris pharetra faucibus lorem, id aliquet est egestas eget. In posuere eros nibh, porta iaculis risus laoreet vitae. Quisque vulputate tincidunt mauris in pretium. Phasellus congue sodales rhoncus. Nullam fringilla nisi sapien. Fusce eget ex leo. Fusce non augue augue. Aliquam dictum mattis auctor.",
                avatarUrl = null,
                twitter = null
            )
        ),
        format = ConferenceFormat.WORKSHOP,
        room = ConferenceRoom.create("Room 1"),
        scheduled = false,
        registrationLink = "http://example.com",
        suggestedKeywords = listOf("Java", "Spring Boot", "Microservices")
    )
)