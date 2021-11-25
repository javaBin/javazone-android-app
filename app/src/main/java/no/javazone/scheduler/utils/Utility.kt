package no.javazone.scheduler.utils

import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceRoom
import no.javazone.scheduler.model.ConferenceSpeaker
import no.javazone.scheduler.model.ConferenceTalk
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


val sampleTalks = listOf(
    ConferenceTalk(
        "19F59B3A-2DF9-499B-940E-D6CA20E00840",
        title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        startTime = OffsetDateTime.now().minusHours(1),
        endTime = OffsetDateTime.now().plusHours(1),
        length = 120,
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
        scheduled = true
    ),
    ConferenceTalk(
        "19F59B3A-2DF9-499B-940E-D6CA20E00840",
        title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        startTime = OffsetDateTime.now().plusHours(1),
        endTime = OffsetDateTime.now().plusHours(3),
        length = 120,
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
        scheduled = false
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
        format = ConferenceFormat.PRESENTATION,
        room = ConferenceRoom.create("Room 1"),
        scheduled = false
    )
)