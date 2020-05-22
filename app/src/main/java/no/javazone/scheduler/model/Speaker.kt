package no.javazone.scheduler.model

import java.net.URL

data class Speaker(
    val name: String,
    val bio: String,
    val avatar: String?,
    val twitter: String?,
    val session: Session) {

    val avatarUrl: URL?
        get() = avatar?.let { URL(avatar) }
}
