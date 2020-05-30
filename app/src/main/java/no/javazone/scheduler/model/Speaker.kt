package no.javazone.scheduler.model

import java.net.URL

data class Speaker(
    val name: String,
    val bio: String,
    val avatar: String? = null,
    val twitter: String? = null) {

    val avatarUrl: URL?
        get() = avatar?.let { URL(avatar) }
}
