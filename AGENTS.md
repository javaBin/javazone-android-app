# JavaZone Android App — Agent Context

JavaZone is the annual Java conference in Norway, run by javaBin. This is the official Android attendee app for the **2026 edition**, themed **"Under The Sea"** (ocean blue palette). Attendees use it to browse the program, manage a personal schedule, view sponsors, and access venue info.

See **`IMPROVEMENTS.md`** for the full bug/improvement backlog.

---

## RULES

> **MUST follow — non-negotiable.**

1. **Always run `./gradlew clean` after every Gradle build.** This project builds inside a Docker container as `root`. Any Gradle invocation that compiles or assembles (`assembleDebug`, `bundleRelease`, `installDebug`, `test`, `connectedAndroidTest`, `compile*`, etc.) creates a `build/` directory owned by `root`. If it is left behind, the host/outside user cannot build (permission denied on the root-owned `build/` directory). Running `./gradlew clean` immediately afterwards deletes the Gradle build directory and prevents these permission/ownership problems. Chain it directly, e.g. `./gradlew assembleDebug && ./gradlew clean`.

---

## Identity

| Key | Value |
|---|---|
| Package name | `no.javazone.scheduler` |
| Application ID | `no.javabin.javazone2026` |
| Version name | `2026.1.0` (versionCode 2) |
| Min / Target / Compile SDK | 36 |
| Kotlin | 2.3.20 |
| JDK | 25 (compile) → JVM **21** bytecode target — do not change |

---

## Architecture

Single-Activity (`MainActivity`) + pure Jetpack Compose + Material 3. No XML layouts, no DataBinding, no Views.

```
UI (Composables)
    └── ViewModels (StateFlow / Compose State)
            └── Repository interfaces
                    ├── Room (local cache — source of truth)
                    └── Retrofit + kotlinx.serialization (network)
```

**Dependency injection:** manual, via `AppContainer` / `AppContainerImpl`. No Hilt, no Dagger, no Koin. The container lives on `JavaZoneApplication.container`; ViewModels receive repositories through `provideFactory()` companion factories.

**State wrapper:** `Resource<T>` sealed interface (`SuccessResource`, `LoadingResource`, `ErrorResource`) — all async states carry their last-known data even when loading or in error.

**Cache pattern:** `networkBoundResource { query / fetch / saveFetchResult / shouldFetch }` — emits Room cache first, optionally fetches from network, saves atomically, re-emits live Room stream.

**StateFlow convention:** `stateIn(viewModelScope, WhileSubscribed(5000), initial)` for all public flows. `Eagerly` only for `isReady` (drives the splash screen).

---

## Module / Package Structure

```
app/src/main/java/no/javazone/scheduler/
├── api/                    # Network & asset data sources
│   ├── ConferenceSessionApi.kt      # Interface: fetchSessions, fetchConference, fetchPartners
│   ├── NetworkConferenceSession.kt  # Live Retrofit implementation
│   ├── AssetConferenceSession.kt    # Asset-file implementation (partners + test fixtures)
│   └── NetworkClient.kt             # Retrofit / OkHttp factory
├── dto/                    # Raw JSON deserialisation models (@Serializable)
│   ├── ConferenceDto.kt
│   ├── SessionDto.kt        # Also contains SessionsDto, SpeakerDto
│   └── PartnerDto.kt
├── model/                  # Domain models
│   ├── Conference.kt
│   ├── ConferenceDate.kt
│   ├── ConferenceFormat.kt   # Enum: WORKSHOP, PRESENTATION, LIGHTNING_TALK
│   ├── ConferenceLanguage.kt # Enum: ENGLISH, NORWEGIAN (apiValue + label); toConferenceLanguage() ext
│   ├── ConferenceRoom.kt
│   ├── ConferenceSession.kt
│   ├── ConferenceSpeaker.kt
│   ├── ConferenceTalk.kt
│   ├── ConferenceTimeSlot.kt  # DEAD CODE — pending deletion (DEAD-L in IMPROVEMENTS.md)
│   ├── DtoToModel.kt          # DTO → domain model transformers
│   └── Partner.kt
├── repository/             # Repository interfaces & Room DB
│   ├── ConferenceRepository.kt
│   ├── PartnersRepository.kt
│   ├── ConferenceDao.kt
│   ├── AppDatabase.kt        # Room DB (version 8, auto-migrations, schema at app/schemas/)
│   ├── impl/
│   │   ├── ConferenceRepositoryImpl.kt
│   │   ├── PartnersRepositoryImpl.kt
│   │   └── Transformers.kt   # Room entities → domain models
│   └── room/                 # Room entity classes & relation classes
├── ui/
│   ├── MainActivity.kt       # Single Activity; installs splash screen; sets Compose content
│   ├── ConferenceApp.kt      # Root composable — JavaZoneTheme, Scaffold, bottom nav, NavHost
│   ├── JavaZoneNavGraph.kt   # NavHost with all route destinations
│   ├── components/
│   │   ├── ConferenceScreen.kt    # Sealed interface + destination objects + route constants
│   │   ├── ConferenceTabRow.kt    # Bottom NavigationBar composable
│   │   ├── ConferenceChip.kt      # Toggleable filter chip
│   │   ├── JavaZoneIcons.kt       # MyScheduleButton (IconToggleButton)
│   │   ├── TalkCard.kt            # Shared session card (used by SessionsRoute + MyScheduleRoute)
│   │   ├── SessionFilter.kt       # Filter bar: language/day/format chips + search field
│   │   ├── SessionSectionHeader.kt # Sticky time/date header for session lists
│   │   └── FullSceeenLoading.kt   # Full-screen loading indicator (filename typo — see IMPROVEMENTS.md Part 4)
│   ├── sessions/
│   │   ├── SessionsRoute.kt   # Program screen (all sessions, filters, search)
│   │   └── DetailsRoute.kt    # Session detail screen
│   ├── schedules/
│   │   └── MyScheduleRoute.kt # Bookmarked sessions screen
│   ├── partners/
│   │   └── PartnersRoute.kt   # Partners logo grid
│   ├── info/
│   │   └── InfoRoute.kt       # Venue, transport, Wi-Fi, links — static strings, no ViewModel
│   └── theme/
│       ├── Theme.kt           # JavaZoneTheme — LightOceanColors / DarkOceanColors, dynamic color disabled
│       ├── Color.kt           # All color tokens
│       ├── Type.kt            # JavaZoneTypography (Roboto; custom fonts bundled but unused)
│       ├── Shape.kt
│       └── TimeFormats.kt     # DateTimeFormatter constants
├── utils/
│   ├── Constants.kt           # API URL, date constants (hardcoded 2019 — see LOGIC-E), log tag
│   ├── Resource.kt            # SuccessResource / LoadingResource / ErrorResource
│   ├── NetworkBoundResource.kt
│   ├── DispatchersProvider.kt # Interface + DefaultDispatchersProvider; swap in tests
│   └── Utility.kt             # sampleTalks preview data + date/time helpers
├── viewmodels/
│   ├── ConferenceListViewModel.kt  # Main VM — shared across sessions, schedule, details
│   └── PartnersViewModel.kt
├── AppContainerImpl.kt        # AppContainer interface + AppContainerImpl (manual DI)
└── JavaZoneApplication.kt     # Application class; instantiates AppContainerImpl
```

---

## Navigation

Jetpack Navigation Compose (`navVersion = 2.9.7`).

| Object | Route | Bottom nav label |
|---|---|---|
| `SessionsScreen` | `sessions` | Sessions |
| `MyScheduleScreen` | `schedule` | My Schedule |
| `PartnerScreen` | `partners` | Partners |
| `InfoScreen` | `info` | Info |
| `DetailsScreen` | `detail_session/{id}` | (not in bottom nav) |

- Destinations are `object`s implementing the `ConferenceScreen` sealed interface in `ui/components/ConferenceScreen.kt`.
- `JavaZoneNavGraph.kt` holds the `NavHost`; a single shared `ConferenceListViewModel` instance is created here and passed down to all relevant routes.
- To add a screen: create a composable in a new `ui/<name>/` package → add object to `ConferenceScreen.kt` → add `composable(route)` in `JavaZoneNavGraph.kt` → optionally add to `allScreens` in `ConferenceApp.kt`.

---

## Theme

Defined in `ui/theme/`. Dynamic color (Material You) is **intentionally disabled** — the ocean palette always applies regardless of wallpaper.

```kotlin
// Theme.kt
internal val LightOceanColors = lightColorScheme(...)   // light: #CCE0FF bg, #006BC4 primary
internal val DarkOceanColors  = darkColorScheme(...)    // dark:  #00193D bg, #AECFFF primary
```

- Both color scheme vals are `internal` (accessible within the module). Do not make them `private`.
- `JavaZoneTypography` and `JavaZoneShapes` are `public val` in `Type.kt` / `Shape.kt`.
- `isSystemInDarkTheme()` still selects which scheme is active — only Material You dynamic color is disabled.
- Custom font files (Domine, Montserrat) are bundled in `res/font/` but **not yet wired** into `JavaZoneTypography` — `FontFamily.Default` (Roboto) is currently used. See UX-S in `IMPROVEMENTS.md`.

---

## Data Flow

```
Network (Retrofit) ──┐
                     ├── ConferenceRepositoryImpl ── Room DB ── ViewModel ── UI
Asset file (JSON) ───┘
```

`networkBoundResource` orchestrates the cache-then-network pattern:
1. Emits cached Room data immediately.
2. Fetches fresh data from the network (or asset fallback).
3. Saves the response to Room atomically and re-emits the live DB stream.

`ConferenceListViewModel` exposes:
- `sessions: StateFlow<Resource<List<ConferenceSession>>>` — all sessions
- `conference: StateFlow<Resource<Conference>>` — conference metadata (name, dates)
- `mySchedule: StateFlow<List<String>>` — bookmarked talk IDs
- `selectedDay: State<LocalDate?>` — day filter (Compose `State`)
- `selectedFormat: State<ConferenceFormat?>` — format filter (Compose `State`)
- `selectedLanguage: State<ConferenceLanguage?>` — language filter (Compose `State`)
- `searchQuery: State<String>` — free-text search (Compose `State`)
- `isReady: StateFlow<Boolean>` — `true` once sessions data is non-empty; drives splash screen

---

## Key Libraries

| Library | Purpose |
|---|---|
| Jetpack Compose + Material 3 | All UI |
| Jetpack Navigation Compose | Screen routing |
| Room 2.8 | Local SQLite database |
| Retrofit 3 + kotlinx-serialization | HTTP client & JSON parsing |
| Coil 3 (OkHttp + SVG) | Async image loading (partner logos) |
| AndroidX Lifecycle / ViewModel | State management |
| Core SplashScreen | Animated splash screen |
| `play-services-oss-licenses` v2 | Open-source licence display — **must not be removed**: `InfoRoute.kt` launches `OssLicensesMenuActivity` directly |
| Kotlin Coroutines + Flow | Async data streams |

---

## Localization Rules

- All user-visible strings must be defined in **both** locale files:
  - `res/values/strings.xml` — English (default)
  - `res/values-no-rNO/strings.xml` — Norwegian
- Brand names, addresses, proper nouns, and technical identifiers (e.g. Wi-Fi SSID) go in `values/strings.xml` only, marked `translatable="false"`.
- Never hardcode user-visible strings in Kotlin — always use `stringResource(R.string.xxx)`.
- String name prefixes by screen: `info_` (Info screen), `nav_` (navigation labels). Follow existing conventions.

---

## Coding Conventions

### KTX / API choices

| Do | Don't |
|---|---|
| `"https://...".toUri()` | `Uri.parse("https://...")` |
| `com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity` | `...oss.licenses.OssLicensesMenuActivity` (v1 — deprecated) |
| `ConferenceFormat.entries` | `ConferenceFormat.values()` (soft-deprecated in Kotlin 1.9+) |

### OSS Licenses screen

Always call `setTheme` before launching the v2 activity so it matches the app's ocean palette:

```kotlin
OssLicensesMenuActivity.setTheme(LightOceanColors, DarkOceanColors, JavaZoneTypography)
context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
```

### Library versions

All library versions are declared in the **root `build.gradle` `ext {}` block**. There is no `libs.versions.toml`. When adding or updating a dependency, add/update the version variable there and reference it in `app/build.gradle`.

### Room

- Database: `AppDatabase`, version **8**, schema exports at `app/schemas/`.
- Any schema change requires an `AutoMigration` or a manual `Migration` class, plus a new schema export.
- See LOGIC-H in `IMPROVEMENTS.md` before touching `RoomEntity`/`ConferenceRoom`.

---

## Key Files Quick Reference

| File | Purpose |
|---|---|
| `ui/info/InfoRoute.kt` | Info screen — venue, transport, Wi-Fi, links. **Static strings only** (no ViewModel). |
| `Info.md` (project root) | Source of truth for all Info screen text content. |
| `ui/JavaZoneNavGraph.kt` | All routes; shared `ConferenceListViewModel` created here. |
| `ui/ConferenceApp.kt` | Root composable — `JavaZoneTheme` wrapper, `Scaffold`, bottom nav, `NavHost`. |
| `viewmodels/ConferenceListViewModel.kt` | Main ViewModel — sessions, mySchedule, day/format/search filters, `isReady`. |
| `utils/Resource.kt` | `Resource<T>` sealed interface for async state. |
| `utils/NetworkBoundResource.kt` | Cache-then-network helper used by repositories. |
| `repository/AppDatabase.kt` | Room DB declaration, migrations, converters. |
| `ui/theme/Theme.kt` | `JavaZoneTheme`, `LightOceanColors`, `DarkOceanColors`. |
| `AppContainerImpl.kt` | Manual DI container — all singleton dependencies. |
| `IMPROVEMENTS.md` | Full backlog: bugs, logic issues, dead code, UX improvements, open decisions. |

---

## Build & Run

> **Docker / root note:** This project runs in a Docker container as `root`. Always run `./gradlew clean` after every build to avoid stale lock files and permission issues caused by running Gradle as root.

```bash
# Debug build
./gradlew assembleDebug && ./gradlew clean

# Release bundle (AAB)
./gradlew bundleRelease && ./gradlew clean

# Install on connected device / emulator
./gradlew installDebug && ./gradlew clean

# Unit tests (Robolectric)
./gradlew test && ./gradlew clean

# Instrumented tests
./gradlew connectedAndroidTest && ./gradlew clean
```

Prerequisites: Android Studio Meerkat (or newer), JDK 25, Android SDK 36.

---

## Design Decisions & Quirks

1. **Ocean theme — dynamic color disabled:** Material You dynamic color is intentionally off. `isSystemInDarkTheme()` still selects `LightOceanColors` vs `DarkOceanColors`; wallpaper never influences colours.

2. **Bottom nav indicator matches sessions sticky header:** `ConferenceTabRow` uses `indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)`. This is deliberate — it matches the tonal surface colour of the `Surface(tonalElevation = 10.dp)` used for time sticky headers in the sessions list.

3. **Static conference dates:** `WORKSHOP_DAY`, `FIRST_CONFERENCE_DAY`, `LAST_CONFERENCE_DAY` in `Constants.kt` are hardcoded to 2019. Actual dates come from the network endpoint at runtime; these constants are only used as fallback defaults and preview data. Updating them requires a team decision on exact 2026 dates (see LOGIC-E in `IMPROVEMENTS.md`).

4. **Singleton repositories:** `ConferenceRepositoryImpl` and `PartnersRepositoryImpl` use `getInstance()`. This prevents multiple DB connections but means tests must clear shared state between runs.

5. **`configChanges="uiMode"`** is declared in `AndroidManifest.xml`, preventing Activity recreation on dark/light mode toggle — important for not resetting Compose state on theme switches.

6. **OSS Licenses plugin must be kept:** `InfoRoute.kt` calls `startActivity(Intent(context, OssLicensesMenuActivity::class.java))` directly. Removing the `play-services-oss-licenses` dependency or its Gradle plugin causes a build failure.

7. **`slotTime` mutable side-effect in `ConferenceTalk`:** `var slotTime` is a non-constructor property mutated inside `ConferenceSession.init` to stamp all talks with their session's group time. It is excluded from `equals()`/`hashCode()`/`copy()`. A fix is planned (see LOGIC-G in `IMPROVEMENTS.md`).

8. **Splash screen assets:** `res/drawable/jz26_icon_400x400.png` is the 2026 app icon, wrapped by `splash_center_icon.xml` with an 18% inset so the square image clears the circular clip that the Splash Screen API applies. `res/drawable/jz26_ocean.png` is the ocean wave branding image, referenced by `splash_branding.xml` (scales to fill the branding slot). Both are wired into `SplashScreenTheme` in `res/values/styles.xml`.

---

## Deferred / In-Progress Work

### InfoRoute ViewModel integration
`InfoRoute()` currently takes no parameters and uses static localized strings for the conference name and date header. A ViewModel-integrated version (deriving the name and date range from `ConferenceListViewModel.conference`) has been implemented and saved externally by the developer for future use. **Do not add ViewModel dependency to `InfoRoute` without explicit instruction.**

### Items requiring a team decision before implementation
See `IMPROVEMENTS.md` Part 3 — these items are blocked on product/team input:
- **LOGIC-E** — hardcoded 2019 fallback dates (needs confirmed 2026 dates)
- **LOGIC-F** — cache invalidation strategy for session data
- **LOGIC-H** — Room PK change for `RoomEntity` (requires migration planning)
- **DEAD-N** — whether to restore `dao.deleteAllRooms()`
- **UX-S** — whether to wire Domine/Montserrat fonts or delete them

---

## Known Bugs (do not silently "fix" without direction)

See `IMPROVEMENTS.md` Group 1 for full details. Short list:

| ID | Location | Summary |
|---|---|---|
| BUG-A | `model/DtoToModel.kt` | `UninitializedPropertyAccessException` in `mergeLightningTalks` when room has empty talk list |
| BUG-B | `ui/components/TalkCard.kt` | Nested `IconToggleButton` inside `IconButton` may block schedule toggle |
| BUG-C | `SessionsRoute.kt` | Blank screen on network error with no cached data |
| BUG-D | `DetailsRoute.kt` | Silent early return when talk not found; no loading/error state shown |

---

## Testing

- **Unit tests:** JUnit 4 + Google Truth + Robolectric (in `app/src/test/`)
- **Coroutine testing:** `TestDispatchersProvider` in `utils/` replaces `DefaultDispatchersProvider`
- **Test fixtures:** `app/src/test/res/sessions.json`
- Run unit tests: `./gradlew test`
- Run instrumented tests: `./gradlew connectedAndroidTest`
