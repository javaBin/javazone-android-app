package no.javazone.scheduler.dto

import kotlinx.serialization.Serializable

@Serializable
data class PartnerDto(
    val name: String,
    val logoUrl: String,
    val homepageUrl: String
)
