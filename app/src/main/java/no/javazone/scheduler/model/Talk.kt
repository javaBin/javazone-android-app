package no.javazone.scheduler.model

interface Talk {
}

data class Presentation(val speaker: Speaker) : Talk

data class Lightning(val speaker: Speaker) : Talk

data class Workshop(val speaker: Speaker) : Talk