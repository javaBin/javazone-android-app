# JavaZone Android App — Change Log & Improvement Backlog

Last updated: 2026-04-25
Maintained by: OpenCode

---

## Part 1 — Changes Already Implemented

A record of every change applied to the codebase since the improvement effort began.

---

### UI-01 — Bottom Nav Bar: text labels added
- **Files:** `ui/components/ConferenceTabRow.kt`, `res/values/strings.xml`
- **What:** Added `label` slot to each `NavigationBarItem` using `stringResource(navItem.label)`.
  Fixed `R.string.sessions` value from `"Schedule"` → `"Sessions"`.
- **Labels shown:** Sessions · My Schedule · Partners · Info

### UI-02 — Partners card: white background for dark mode logo visibility
- **File:** `ui/partners/PartnersRoute.kt`
- **What:** Set `CardDefaults.cardColors(containerColor = Color.White)` on each partner card.
  Changed grid background from hardcoded `Color.LightGray` to `MaterialTheme.colorScheme.background`.
- **Why:** SVG logos are designed for white backgrounds; the dark Material3 surface made them invisible in dark mode.

### UI-03 — Theme: full "Under The Sea" ocean palette for JavaZone 2026
- **Files:** `ui/theme/Color.kt`, `ui/theme/Theme.kt`
- **What:** Replaced the old purple-based M3 palette with a complete ocean blue palette.
  - Light: seafoam whites, deep teal primary (`#00687A`), aquamarine container (`#9FEFFF`)
  - Dark: midnight navy background (`#090E1B`), bioluminescent teal primary (`#51D9EC`)
  - Disabled Material You (dynamic color) so the ocean palette is always applied regardless of wallpaper.
- **Scheme names:** `LightOceanColors` / `DarkOceanColors`

### UI-04 — Theme: background made more distinctly blue
- **File:** `ui/theme/Color.kt`
- **What:** Shifted `background` and `surface` tokens to more saturated blues:
  - Light: `#F4FBFC` → `#E8F3FF` (open sky blue)
  - Dark: `#0D1415` → `#090E1B` (midnight navy)

### UI-05 — Bottom Nav Bar: ocean theme colors applied
- **File:** `ui/components/ConferenceTabRow.kt`
- **What:**
  - `NavigationBar containerColor` = `MaterialTheme.colorScheme.background`
  - `NavigationBarItem indicatorColor` = `MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)`
    (matches the tonal color used by the sessions sticky time header `Surface(tonalElevation = 10.dp)`)

### UI-06 — Filter chips: reduced height
- **File:** `ui/components/ConferenceChip.kt`
- **What:** Changed `Text` padding from `Modifier.padding(8.dp)` (uniform) to
  `Modifier.padding(horizontal = 10.dp, vertical = 4.dp)`, reducing chip height by 8dp.

### UI-07 — Sessions filter: search field added
- **Files:** `ui/sessions/SessionsRoute.kt`, `viewmodels/ConferenceListViewModel.kt`,
  `res/values/strings.xml`, `res/values-no-rNO/strings.xml`
- **What:**
  - Added `_searchQuery` / `searchQuery` / `updateSearchQuery()` to `ConferenceListViewModel`.
  - Extended `updateSessionsWithMySchedule()` with a `searchQuery: String = ""` parameter;
    filters by `talk.title`, `speaker.name`, and `talk.summary` (case-insensitive).
  - Added `OutlinedTextField` in `AllSessionsScreen` with:
    - Leading `Icons.Filled.Search` icon
    - Placeholder: `"Search title, speaker, keyword"` (from `R.string.search_hint`)
    - Trailing `Icons.Filled.Close` clear button (visible only when query is non-empty)
    - `RoundedCornerShape(18.dp)`, `singleLine = true`, `ImeAction.Search`
  - Added `search_hint` string to both `strings.xml` (EN) and `strings-no-rNO/strings.xml` (NO: `"Søk tittel, foredragsholder, nøkkelord"`).

### UI-08 — Sessions filter: search field positioned below format chips
- **File:** `ui/sessions/SessionsRoute.kt`
- **What:** Moved `OutlinedTextField` from the top of the filter section to between the format
  chip row and the workshop registration warning row.
- **Order is now:** Day chips → Format chips → Search field → Warning text → Session list

### BUILD-01 — Dependency cleanup: removed unused dependencies
- **Files:** `app/build.gradle`, `build.gradle`
- **Removed:**
  - `androidx.legacy:legacy-support-v4` — no imports
  - `androidx.compose.animation:animation` — no direct imports; provided transitively
  - `androidx.compose.runtime:runtime-livedata` — no `observeAsState` usage
  - `androidx.lifecycle:lifecycle-viewmodel-savedstate` — no `SavedStateHandle` usage
  - `androidx.lifecycle:lifecycle-livedata-ktx` — no `LiveData` usage
  - `androidx.window:window` — no `WindowMetrics`/`WindowSizeClass` usage
  - `fileTree(dir: "libs")` — `app/libs/` does not exist
  - `dataBinding = true` in `buildFeatures` — no DataBinding usage
- **Removed from root `build.gradle` ext vars:**
  - `constraintLayoutVersion`, `uiToolingVersion`, `workVersion`, `viewPagerVersion`
    (none referenced by any dependency declaration)
- **Note:** `play-services-oss-licenses` and its plugin were initially removed but restored
  after build failure — `InfoRoute.kt` actively launches `OssLicensesMenuActivity`.

---

## Part 2 — Improvement Backlog

All items confirmed pending. Nothing below has been implemented yet.

---

### Group 1 — Critical Bugs (Fix First)

#### BUG-A — Latent crash in `mergeLightningTalks`
- **File:** `model/DtoToModel.kt`
- **Issue:** `lateinit var current` (line 104) is never assigned when a room's talk list is
  empty. The outer `roomTalks.forEach` loop reaches line 123
  (`sessions.add(ConferenceSession(time = current.first().startTime, talks = current))`)
  without `current` ever having been initialised, throwing
  `UninitializedPropertyAccessException`. A secondary variant: if a previous room's loop
  reset `current` to an empty `mutableListOf()`, `current.first()` throws
  `NoSuchElementException`.
- **Fix:** Add `if (roomTalk.value.isEmpty()) return@forEach` at the top of the outer
  `roomTalks.forEach` lambda body (before the inner for-loop).

#### BUG-B — Nested `IconButton` anti-pattern blocks schedule toggle
- **Files:** `ui/sessions/SessionsRoute.kt` (line 302), `ui/schedules/MyScheduleRoute.kt` (line 143)
- **Issue:** `MyScheduleButton` (an `IconToggleButton`) is wrapped inside an outer
  `IconButton(onClick = {})`. The outer no-op click target intercepts touches and
  may prevent the inner toggle from firing reliably.
- **Fix:** Remove the outer `IconButton` wrapper; let `MyScheduleButton` stand alone.

#### BUG-C — Blank screen on network error with no cached data
- **File:** `ui/sessions/SessionsRoute.kt`
- **Issue:** The `is ErrorResource` branch renders nothing when `resource.data.isEmpty()`.
  User sees a blank white/dark screen with no message and no recovery path.
- **Fix:** Add an error composable (message + retry button) for the empty-data error case.

#### BUG-D — Blank screen when talk not found in DetailsRoute
- **File:** `ui/sessions/DetailsRoute.kt`
- **Issue:** `val session = ... .find { it.id == talkId } ?: return` silently exits the
  composable if sessions haven't loaded or the ID doesn't match.
  No loading indicator, no error message, no back button — user is stranded.
- **Fix:** Check the resource state before searching:
  - `LoadingResource` with empty data → show `FullScreenLoading()`
  - `SuccessResource` or `ErrorResource` with talk not found → show an error message
    with a back button

---

### Group 2 — Data & Logic Correctness

#### LOGIC-E — Stale 2019 conference dates used as fallback
- **Files:** `utils/Constants.kt`, `model/DtoToModel.kt`, `viewmodels/ConferenceListViewModel.kt`
- **Issue:** `WORKSHOP_DAY`, `FIRST_CONFERENCE_DAY`, `LAST_CONFERENCE_DAY` are hardcoded to
  JavaZone 2019. Sessions with missing DTO time data will appear 7 years in the past.
  Also used as the initial `_selectedDay` value before async conference data loads.
- **Decision needed:** Update to 2026 dates (confirm exact dates with team), or remove
  hardcoded fallbacks entirely so the API is the sole source of truth?

#### LOGIC-F — Cache never used; network always fetched unconditionally
- **File:** `repository/impl/ConferenceRepositoryImpl.kt`
- **Issue:** `shouldFetch = { true }` — `networkBoundResource` always hits the network on
  every app start regardless of cached data age. The `lastUpdated` field (see DEAD-M)
  was presumably intended to guard this.
- **Decision needed:** Implement a time-based cache window (e.g. re-fetch only if data is
  older than N minutes), or keep always-fetch as intentional for a live schedule?

#### LOGIC-G — Mutable `var slotTime` on a `data class` breaks value semantics
- **Files:** `model/ConferenceTalk.kt`, `model/ConferenceSession.kt`, `model/DtoToModel.kt`
- **Issue:** `var slotTime` is not a constructor parameter, so it is excluded from
  `equals()`, `hashCode()`, and `copy()`. It is mutated as a side-effect inside
  `ConferenceSession.init` — a hidden violation of `data class` value-semantics contract.
  A concrete consequence: in `selectMySchedule`, `talk.copy(scheduled = true)` creates a
  new talk without going through `ConferenceSession.init`, so the copy's `slotTime` reverts
  to `startTime`, breaking sort/grouping for lightning talk groups.
- **`slotTime` is NOT a DB column** in `TalkEntity` — this change does not require a Room
  migration.
- **Fix (three files):**
  1. `ConferenceTalk`: promote `slotTime` to a constructor parameter
     (`val slotTime: OffsetDateTime = startTime`); remove the body-level `var slotTime`.
  2. `ConferenceSession.init`: remove `talks.forEach { it.slotTime = time }`.
  3. `DtoToModel.kt` — `mergeLightningTalks`: when creating the `ConferenceSession` for
     each group, map the talks with the session time explicitly:
     ```kotlin
     val sessionTime = current.first().startTime
     sessions.add(ConferenceSession(
         time = sessionTime,
         talks = current.map { it.copy(slotTime = sessionTime) }
     ))
     ```
     Regular (non-lightning) talks use `ConferenceSession(talk)` whose secondary constructor
     sets `time = talk.startTime`, matching the default `slotTime = startTime`. No change
     needed there.

#### LOGIC-H — `hashCode()` used as Room database primary key for rooms
- **Files:** `model/ConferenceRoom.kt`, `repository/room/RoomEntity.kt`
- **Issue:** `abs(name.hashCode())` used as PK. Two problems:
  1. `abs(Int.MIN_VALUE) == Int.MIN_VALUE` — produces a negative PK for one specific hash value.
  2. Hash collisions between room names are possible.
- **Fix:** Use the room name string itself as PK, or switch to `autoGenerate = true`.
- **Note:** Requires a Room DB migration (schema change). See Part 3.

#### LOGIC-I — Broken singleton pattern in API session classes
- **Files:** `api/AssetConferenceSession.kt`, `api/NetworkConferenceSession.kt`
- **Issue:** Both classes have the same defect:
  ```kotlin
  instance ?: synchronized(this) {
      instance ?: SomeClass(...)   // created but never stored
  }
  ```
  The new instance is returned but never assigned back to `instance`. Under concurrent
  access, a new object is created on every call.
  (`AppDatabase` and `ConferenceRepositoryImpl` both correctly use `.also { instance = it }`
  and are unaffected.)
- **Fix:** `instance ?: SomeClass(...).also { instance = it }` in both classes.

#### LOGIC-J — `autoMigrations` versions overlap `fallbackToDestructiveMigration`
- **File:** `repository/AppDatabase.kt`
- **Issue:** Versions 4 and 5 appear in both `autoMigrations` and
  `fallbackToDestructiveMigrationFrom(true, 1, 2, 3, 4, 5)`.
  For users upgrading from v4 or v5, the destructive fallback silently
  bypasses the defined auto-migration, wiping their data.
- **Fix:** Remove versions 4 and 5 from the destructive fallback list,
  keeping only `fallbackToDestructiveMigrationFrom(true, 1, 2, 3)`.

#### LOGIC-K — `ConferenceDate.compareTo` sorts by label string, not by date
- **File:** `model/ConferenceDate.kt`
- **Issue:** Primary sort key is `label.compareTo(other.label)`. The workshop day label
  `"workshop"` happens to sort after ISO date strings alphabetically, producing
  accidentally correct order — but this is a fragile, undocumented contract.
- **Fix:** Compare by `date` first (chronological), then `label` as tiebreaker.

---

### Group 3 — Dead Code Removal

#### DEAD-L — Unused class `ConferenceTimeSlot`
- **File:** `model/ConferenceTimeSlot.kt`
- **Fix:** Delete the file. No references anywhere in the codebase.

#### DEAD-M — Unused field `lastUpdated`
- **File:** `repository/impl/ConferenceRepositoryImpl.kt`
- **Fix:** Remove the field and its `lastUpdated = OffsetDateTime.now()` assignment.
  (Related to LOGIC-F.)

#### DEAD-N — Commented-out `dao.deleteAllRooms()`
- **File:** `repository/impl/ConferenceRepositoryImpl.kt`
- **Issue:** `//dao.deleteAllRooms()` left in production code. Without it, stale room
  entities accumulate across refreshes if room names change between conference editions.
- **Decision needed:** Restore the call (correct behaviour), or document why it is intentionally omitted.

#### DEAD-O — Dead DAO method `getIdFromRowId`
- **File:** `repository/ConferenceDao.kt`
- **Fix:** Remove `suspend fun getIdFromRowId(rowId: Long): Long` — never called.

#### DEAD-P — Unused import `toMediaTypeOrNull`
- **File:** `utils/Constants.kt`
- **Fix:** Remove `import okhttp3.MediaType.Companion.toMediaTypeOrNull`.

#### DEAD-Q — Unused import `JavaZoneTypography` in `ConferenceApp`
- **File:** `ui/ConferenceApp.kt`
- **Fix:** Remove `import no.javazone.scheduler.ui.theme.JavaZoneTypography`.

---

### Group 4 — UI / UX Improvements

#### UX-R — `Spacer(Modifier.padding(...))` is a visual no-op
- **File:** `ui/info/InfoRoute.kt`
- **Issue:** `Modifier.padding` on a `Spacer` applies padding *inside* the zero-size box —
  it produces no visible gap. The correct modifier is `Modifier.height(Ndp)`.
- **Fix:** Replace all instances with `Spacer(modifier = Modifier.height(Ndp))`.

#### UX-S — Bundled custom fonts never applied (~600 KB dead weight)
- **Files:** `ui/theme/Type.kt`, `res/font/`
  (domine_bold, domine_regular, montserrat_medium, montserrat_regular, montserrat_semibold)
- **Issue:** `JavaZoneTypography` uses `FontFamily.Default` throughout despite 5 custom font
  files being bundled in the APK, adding ~600 KB for no effect.
- **Decision needed:** Wire Domine/Montserrat into the typography (changes visual appearance),
  or delete the font files to reclaim the space?

#### UX-T — Hardcoded strings not localised (missing Norwegian translations)
- **Files:** `ui/sessions/SessionsRoute.kt`, `ui/info/InfoRoute.kt`, `ui/sessions/DetailsRoute.kt`
- **Issue:** The following strings are hardcoded in Kotlin and never translated:
  - `"All"` — day filter chip (`SessionsRoute.kt`)
  - `"All"` — format filter chip (`SessionsRoute.kt`)
  - `"Workshops require registration ahead of time"` (`SessionsRoute.kt`)
  - `"Twitter: $twitter"` — speaker twitter handle (`DetailsRoute.kt`)
  - `"JavaZone"`, `"WI-FI SSID: JavaZone"`, `"Code of conduct"`, `"javaBin"`,
    `"Terms and Condition"` *(typo — should be "Conditions")*, `"Open source licences"`,
    `"github"` — `InfoRoute.kt`
- **Fix:** Move all to `res/values/strings.xml` and `res/values-no-rNO/strings.xml`.
  Use a format string (`%s`) for the twitter/X handle.

#### UX-U — Trailing `#` in policy URL (copy-paste typo)
- **File:** `ui/info/InfoRoute.kt`
- **Fix:** Change `"https://www.java.no/policy.html#"` → `"https://www.java.no/policy.html"`.

---

### Group 5 — Code Quality / Style

#### STYLE-V — Magic string `"NULLNULLNULL"` as null sentinel in navigation
- **File:** `ui/JavaZoneNavGraph.kt`
- **Issue:** `defaultValue = "NULLNULLNULL"` and `if (entryArg != null && entryArg != "NULLNULLNULL")` used as
  a null stand-in. Fragile and opaque.
- **Fix:** Use `nullable = true` and `defaultValue = null` on the `navArgument`; simplify
  the session-ID resolution to a single `?: run { ... }` null-coalescing chain.

#### STYLE-W — Wrong log tag and wrong severity level
- **Files:** `ui/sessions/SessionsRoute.kt` (line 256), `ui/schedules/MyScheduleRoute.kt` (line 54)
- **Issue:** `Log.w("SessionviewDebug", ...)` uses a raw hardcoded tag instead of the
  project-wide `LOG_TAG` constant, and uses warning-level for routine navigation events.
- **Fix:** Replace with `Log.d(LOG_TAG, ...)`.

#### STYLE-X — Two import statements concatenated on one line
- **File:** `viewmodels/ConferenceListViewModel.kt`
- **Issue:** `import kotlinx.coroutines.flow.*import kotlinx.coroutines.launch` on one line.
- **Fix:** Split onto two separate lines.

#### STYLE-Y — Deprecated `Enum.values()` API
- **File:** `ui/sessions/SessionsRoute.kt`
- **Issue:** `ConferenceFormat.values()` is soft-deprecated in Kotlin 1.9+.
- **Fix:** Replace with `ConferenceFormat.entries`.

#### STYLE-Z — Ineffective `Modifier.layoutId` in plain Column
- **File:** `ui/info/InfoRoute.kt`
- **Issue:** `Modifier.layoutId("icon")` on the `Box` wrapping each icon inside a plain
  `Column` has no effect. `layoutId` is only meaningful inside a custom `Layout` composable.
- **Fix:** Remove the `Box(modifier = Modifier.layoutId("icon"))` wrapper; use the `Icon`
  directly.

#### STYLE-AA — Misleading `startDestination = currentRoute` passed to NavHost
- **File:** `ui/ConferenceApp.kt`
- **Issue:** `startDestination` in Compose Navigation is consumed only once at graph
  creation. Passing the reactive `currentRoute` reads as if it re-routes on recomposition,
  which it does not.
- **Fix:** Pass the fixed constant `SessionsScreen.route` directly.

#### STYLE-BB — Duplicate talk IDs in `sampleTalks` preview data
- **File:** `utils/Utility.kt`
- **Issue:** All three sample talks share the same `id` string
  (`"19F59B3A-2DF9-499B-940E-D6CA20E00840"`), which can cause silent data loss in any
  `Set<ConferenceTalk>` or ID-keyed preview structure.
- **Fix:** Assign unique IDs to each sample talk entry.

#### STYLE-CC — XML `colors.xml` and `dimens.xml` out of sync with Compose theme
- **Files:** `res/values/colors.xml`, `res/values/dimens.xml`
- **Issue:** `colors.xml` still contains the old purple palette (`#FF5454ac`, `#FFc1c1ff`,
  `#FF566500`). `dimens.xml` defines `text_margin = 16dp` which is not referenced anywhere
  in Kotlin. Both are misleading to future developers.
- **Fix:** Update `colors.xml` to reflect the ocean palette primary/accent; remove
  `text_margin` from `dimens.xml`.

#### STYLE-DD — Unnecessary `count` override in preview provider
- **File:** `ui/schedules/MyScheduleRoute.kt`
- **Issue:** `override val count: Int get() = values.count()` is identical to the
  default implementation in `PreviewParameterProvider`.
- **Fix:** Remove the override.

---

## Part 3 — Open Decisions Required Before Implementation

The following items cannot be implemented without a product/team decision:

| ID | Question | Affected Files |
|---|---|---|
| LOGIC-E | Update hardcoded 2019 dates to 2026 JavaZone dates (confirm exact dates), or remove fallbacks entirely? | `Constants.kt`, `DtoToModel.kt`, `ConferenceListViewModel.kt` |
| LOGIC-F | Implement time-based cache window for session data, or keep always-fetch as intentional? | `ConferenceRepositoryImpl.kt` |
| LOGIC-H | Room hashCode PK change requires a Room DB migration — confirm schema change before touching | `ConferenceRoom.kt`, `RoomEntity.kt` |
| DEAD-N | Restore `dao.deleteAllRooms()` call (prevents stale room accumulation), or document why it is intentionally commented out? | `ConferenceRepositoryImpl.kt` |
| UX-S | Wire Domine/Montserrat fonts into typography (visual change), or delete font files to save ~600 KB? | `Type.kt`, `res/font/` |

---

## Part 4 — File Rename

| Current filename | Correct filename |
|---|---|
| `ui/components/FullSceeenLoading.kt` | `ui/components/FullScreenLoading.kt` |
