package no.javazone.scheduler.ui.theme
import androidx.compose.ui.graphics.Color

// =============================================================================
// JavaZone 2026 — "Under The Ocean"
// Base palette
//   #E6F0FF  ocean surface shimmer (palest blue)
//   #AECFFF  shallow-water blue
//   #5C9EFF  mid-ocean blue
//   #006BC4  deep ocean (primary brand blue)
//   #02DFFF  bioluminescent glow (cyan)
//   #F0567A  coral / deep-sea life (warm accent)
// =============================================================================

// Light Ocean Theme  (daylight view — sky reflected on the water)

val md_theme_light_primary             = Color(0xFF006BC4) // deep ocean blue
val md_theme_light_onPrimary           = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer    = Color(0xFFAECFFF) // shallow-water blue
val md_theme_light_onPrimaryContainer  = Color(0xFF001D3D) // abyssal near-black

val md_theme_light_secondary           = Color(0xFF5C9EFF) // mid-ocean blue
val md_theme_light_onSecondary         = Color(0xFF001D3D)
val md_theme_light_secondaryContainer  = Color(0xFFE6F0FF) // ocean surface shimmer
val md_theme_light_onSecondaryContainer= Color(0xFF001838)

val md_theme_light_tertiary            = Color(0xFF005A68) // deep teal — abyssal glow
val md_theme_light_onTertiary          = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer   = Color(0xFF02DFFF) // bioluminescent glow
val md_theme_light_onTertiaryContainer = Color(0xFF001F26)

val md_theme_light_error               = Color(0xFFF0567A) // coral / sea-life accent
val md_theme_light_errorContainer      = Color(0xFFFFD9E2)
val md_theme_light_onError             = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer    = Color(0xFF3B0016)

val md_theme_light_background          = Color(0xFFCCE0FF) // ocean surface shimmer — deeper blue
val md_theme_light_onBackground        = Color(0xFF001838)
val md_theme_light_surface             = Color(0xFFCCE0FF)
val md_theme_light_onSurface           = Color(0xFF001838)
val md_theme_light_surfaceVariant      = Color(0xFFAECFFF) // shallow-water blue
val md_theme_light_onSurfaceVariant    = Color(0xFF003366)
val md_theme_light_outline             = Color(0xFF006BC4) // deep ocean blue
val md_theme_light_inverseOnSurface    = Color(0xFFCCE0FF)
val md_theme_light_inverseSurface      = Color(0xFF001838)

// =============================================================================
// Dark Ocean Theme  (underwater at night — depths, bioluminescence, coral glow)
// =============================================================================

val md_theme_dark_primary              = Color(0xFFAECFFF) // moonlit shallow water
val md_theme_dark_onPrimary            = Color(0xFF001D3D) // midnight deep
val md_theme_dark_primaryContainer     = Color(0xFF006BC4) // deep ocean blue as container
val md_theme_dark_onPrimaryContainer   = Color(0xFFE6F0FF) // surface shimmer text

val md_theme_dark_secondary            = Color(0xFF02DFFF) // bioluminescent glow
val md_theme_dark_onSecondary          = Color(0xFF001F26)
val md_theme_dark_secondaryContainer   = Color(0xFF004D5B) // dark abyss teal
val md_theme_dark_onSecondaryContainer = Color(0xFFAECFFF)

val md_theme_dark_tertiary             = Color(0xFFF0567A) // coral glow in the dark
val md_theme_dark_onTertiary           = Color(0xFF3B0016)
val md_theme_dark_tertiaryContainer    = Color(0xFF7A0030) // deep-sea dark coral
val md_theme_dark_onTertiaryContainer  = Color(0xFFFFD9E2)

val md_theme_dark_error                = Color(0xFFFFB3C1) // softened coral for dark bg
val md_theme_dark_errorContainer       = Color(0xFF93002A)
val md_theme_dark_onError              = Color(0xFF66001A)
val md_theme_dark_onErrorContainer     = Color(0xFFFFD9E2)

val md_theme_dark_background           = Color(0xFF00193D) // midnight ocean floor
val md_theme_dark_onBackground         = Color(0xFFE6F0FF)
val md_theme_dark_surface              = Color(0xFF00193D)
val md_theme_dark_onSurface            = Color(0xFFE6F0FF)
val md_theme_dark_surfaceVariant       = Color(0xFF002657) // deep ocean mid-layer
val md_theme_dark_onSurfaceVariant     = Color(0xFFAECFFF)
val md_theme_dark_outline              = Color(0xFF5C9EFF) // mid-ocean glow outline
val md_theme_dark_inverseOnSurface     = Color(0xFF001838)
val md_theme_dark_inverseSurface       = Color(0xFFE6F0FF)

val seed  = Color(0xFF006BC4) // source hue — deep ocean blue
val error = Color(0xFFF0567A) // coral accent
