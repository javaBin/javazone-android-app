# Publishing the JavaZone app

This document covers two things:

1. **One-time bootstrap** for the JavaZone 2026 listing (do this once, at the start of the
   handover).
2. **Per-release workflow** to ship a new build (use every time you push an update).

The 2026 release is being published as a **new app listing** in the javaBin Play Console
account, not as an update to an existing one. Past conferences have done the same — each
year is its own listing, since the app is conference-specific and there's no real cross-year
retention need.

| | Value |
|---|---|
| Target listing | **JavaZone 2026** (new) |
| applicationId | `no.javabin.javazone2026` |
| Launcher label (`app_name`) | `JavaZone` (year-agnostic, no change) |
| Initial versionCode / versionName | `1` / `2026.1.0` |
| Play account | Java Brukerforening i Norge (Account ID `5064436998460465164`) |
| Keystore storage | javaBin shared LastPass vault |

---

## Prerequisites

Before starting, confirm you have:

- [ ] Access to the **javaBin Google Play Console** (ask Øyvind to add you as a developer
      under Users and permissions if not).
- [ ] Access to the **javaBin shared LastPass vault** for storing the keystore.
- [ ] A working local build:
  - JDK 25 (the Gradle toolchain will download this automatically if missing — no manual
    install needed, but `JAVA_HOME` should not point at something incompatible).
  - Android SDK with platform 36 + build-tools 36.1.0.
  - The `./gradlew :app:assembleDebug` task succeeds on a clean checkout.
- [ ] Push access to this repo (`javaBin/javazone-android-app`).

---

## One-time bootstrap (JavaZone 2026 listing)

Do these tasks in order. Each section ends with a verification step — don't move on if it
doesn't pass.

### 1. Create the new app in Play Console

- [ ] Open the [javaBin Play Console](https://play.google.com/console/u/0/developers/5064436998460465164/app-list).
- [ ] Click **Create app**.
- [ ] Fill in:
  - **App name**: `JavaZone 2026`
  - **Default language**: English (United States) — `en-US`
  - **App or game**: App
  - **Free or paid**: Free
- [ ] Accept the declarations checkboxes.
- [ ] Click **Create app**. You'll land on the new app's Dashboard.
- [ ] Note the **package name** Google wants you to use will be set on first upload, not
      here. The package name is `no.javabin.javazone2026`.

**Verify**: The app appears in the developer's app list with status "Setup not complete".

### 2. Generate the upload keystore

We're using a fresh keystore for this listing. Once enrolled in Play App Signing (step 3),
this will be the **upload key** — losing it is recoverable via Play Console's "Request
upload key reset" flow.

Generate the keystore:

```bash
keytool -genkey -v \
  -keystore javazone-2026-upload.jks \
  -alias upload \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -dname "O=java Brukerforening i Norge, L=Oslo, C=NO"
```

You'll be prompted for:

- **Keystore password** (8+ chars, store in LastPass — see step 2b).
- **Key password** (you can press Enter to reuse the keystore password — common practice and
  fine for upload keys).

This produces `javazone-2026-upload.jks` in the current directory.

#### 2a. Verify the keystore

```bash
keytool -list -v -keystore javazone-2026-upload.jks -alias upload
```

Note the printed **SHA1** and **SHA256** fingerprints — you'll need them in step 3.

#### 2b. Upload to LastPass

- [ ] In the javaBin shared LastPass vault, create a new **Secure Note** entry:
  - **Name**: `JavaZone 2026 Android upload key`
  - **Notes**: include keystore password, key password, alias (`upload`), and the SHA-1
    fingerprint for cross-reference.
  - **Attach file**: the `javazone-2026-upload.jks` file.
- [ ] Move the local `.jks` file into a stable location *outside the repo* — e.g.,
      `~/.android/javazone-2026-upload.jks`. **Do not** put it inside the repo, even
      temporarily; it must never end up in `git status`.
- [ ] Delete any temporary copy from `~/Downloads` or wherever you initially generated it.

**Verify**: The keystore is in LastPass + at one local path outside the repo, and nowhere
else.

### 3. Wire up signing in `app/build.gradle`

Open `app/build.gradle` and make the following edits:

- [ ] Change `applicationId` from `"no.javazone.scheduler"` to `"no.javabin.javazone2026"`.
- [ ] Change `versionCode` from `18` to `1`.
- [ ] Change `versionName` from `"2023.1.8"` to `"2026.1.0"`.
- [ ] Add a `signingConfigs` block inside `android { }` (anywhere before `buildTypes`):

  ```groovy
  signingConfigs {
      release {
          if (project.hasProperty("JAVAZONE_KEYSTORE_PATH")) {
              storeFile file(project.findProperty("JAVAZONE_KEYSTORE_PATH"))
              storePassword project.findProperty("JAVAZONE_KEYSTORE_PASSWORD")
              keyAlias project.findProperty("JAVAZONE_KEY_ALIAS")
              keyPassword project.findProperty("JAVAZONE_KEY_PASSWORD")
          }
      }
  }
  ```

- [ ] Wire the release build type to use it. Inside `buildTypes.release`, add:

  ```groovy
  if (project.hasProperty("JAVAZONE_KEYSTORE_PATH")) {
      signingConfig signingConfigs.release
  }
  ```

  The `if` guard means debug builds and CI checkouts that don't have signing creds still
  work — they just won't produce a signable release artifact.

- [ ] Leave `namespace 'no.javazone.scheduler'` alone. The Kotlin package name is decoupled
      from `applicationId` in modern AGP, and changing it would force-rename every source
      file. The Play Store identity is fully controlled by `applicationId`.

#### 3a. Set Gradle properties locally

In `~/.gradle/gradle.properties` (create if it doesn't exist), add:

```properties
JAVAZONE_KEYSTORE_PATH=/Users/<you>/.android/javazone-2026-upload.jks
JAVAZONE_KEYSTORE_PASSWORD=<from LastPass>
JAVAZONE_KEY_ALIAS=upload
JAVAZONE_KEY_PASSWORD=<from LastPass>
```

**This file is in your home directory, not the repo, and must stay there.** Do not copy
these properties into the repo's `gradle.properties`.

**Verify**:

```bash
./gradlew :app:bundleRelease
```

Should produce `app/build/outputs/bundle/release/app-release.aab` without errors. If it
fails with "Keystore was tampered with, or password was incorrect", the password in
`~/.gradle/gradle.properties` doesn't match the one used during keystore generation.

### 4. Upload the first AAB and enroll in Play App Signing

- [ ] In Play Console, navigate to **Test and release → Testing → Internal testing**.
- [ ] Click **Create new release**.
- [ ] When prompted about Play App Signing, accept Google's default ("Use Google-generated
      key" — this enrolls the new app automatically). For a brand-new listing this is the
      only sensible option; the older "I'll provide the key" path is being phased out.
- [ ] Upload the AAB from step 3.
- [ ] Fill in **Release name** (defaults to versionName) and **Release notes** (one short
      paragraph in English; Norwegian translation can be added later).
- [ ] Click **Next**, review, then **Save** (don't roll out yet — we still need store
      listing assets).

**Verify**: The release shows as a **Draft** in Internal testing with the AAB attached. The
package name shown matches `no.javabin.javazone2026`.

### 5. Complete the store listing

This is the slowest part of new-app bootstrap because Play requires every form filled in
before any track (even Internal testing) can roll out.

The existing **Javazone 2025** listing (`no.javabin.javazone2025`) has all of this content
already. **Open it in a second tab and reuse text/assets verbatim where the year doesn't
change.** Update titles, descriptions, and screenshots to say "2026" instead of "2025".

Tasks (under **Grow users → Store presence → Main store listing**):

- [ ] **App name**: `JavaZone 2026` (≤30 chars).
- [ ] **Short description**: ≤80 chars, e.g., `Official conference app for JavaZone 2026`.
- [ ] **Full description**: ≤4000 chars. Copy from the 2025 listing, change year references.
- [ ] **App icon**: 512×512 PNG with alpha. Reuse 2025's or update for the 2026 "Under the
      Ocean" theme.
- [ ] **Feature graphic**: 1024×500 JPG/PNG.
- [ ] **Phone screenshots**: minimum 2, recommend 4–8. Capture from a real device or
      emulator running the new build (1080×2400 or similar).
- [ ] **Tablet screenshots**: optional but recommended.

Tasks (under **Policy → App content**):

- [ ] **Privacy policy**: URL required. Reuse the URL from the 2025 listing.
- [ ] **App access**: declare whether parts of the app are gated. If anyone can use it,
      select "All functionality is available without restrictions".
- [ ] **Ads**: select "No, my app does not contain ads".
- [ ] **Content rating questionnaire**: complete it; copy answers from 2025 listing.
- [ ] **Target audience and content**: select age range. Reuse 2025's selection.
- [ ] **News app**: No.
- [ ] **COVID-19 contact tracing and status apps**: No.
- [ ] **Data safety**: complete the form. Reuse 2025 answers.
- [ ] **Government apps**: No.
- [ ] **Financial features**: None.
- [ ] **Health**: None.

Tasks (under **Policy → Advanced**, if shown):

- [ ] Any remaining declarations Play has added since 2025. Read carefully — Play
      occasionally adds new mandatory questions.

**Verify**: Play Console's **Dashboard** shows green checkmarks for all setup tasks. If any
red exclamations remain, click into them and resolve.

### 6. Roll out to Internal testing and verify on a real device

- [ ] Add yourself (and Øyvind) as internal testers under **Internal testing → Testers**:
      either create an email list or add individual emails.
- [ ] Go back to **Internal testing → Releases**, find the draft release from step 4, click
      **Review release → Start rollout to internal testing**.
- [ ] Wait 5–10 minutes for Play's automated review.
- [ ] On your phone, sign in to Play Store with the email you added as a tester.
- [ ] Visit the **opt-in URL** shown on the Internal testing page, accept the invitation.
- [ ] Install the app from Play Store and confirm it launches and pulls 2026 conference
      data correctly.

**Verify**: The app installs from Play Store (not sideloaded), opens, and shows the
expected 2026 content.

### 7. Promote to Production

Only after step 6 passes on a real device:

- [ ] In Play Console, go to **Test and release → Production**.
- [ ] Click **Create new release**.
- [ ] Click **Promote release from internal testing** at the top — pick the release you
      just verified. This avoids re-uploading the same AAB.
- [ ] Fill in production release notes (can be the same as internal).
- [ ] Click **Next → Review release → Start rollout to production**.
- [ ] Production review takes anywhere from a few hours to ~3 days for a brand-new app.
      Subsequent updates are usually under an hour.

**Verify**: Once review completes, search for "JavaZone 2026" on Play Store from a phone
not associated with the developer account. The listing should appear publicly.

### 8. Document and commit code changes

- [ ] Commit the `app/build.gradle` changes from step 3 with a message like
      `chore: bump applicationId, versionCode, versionName for 2026 release`.
- [ ] Open a PR against `master`, get a review (Øyvind can ack), and merge.
- [ ] Tag the merged commit: `git tag v2026.1.0 && git push --tags`.

---

## Per-release workflow (going forward)

Use this checklist for every subsequent release after the bootstrap is done.

### Pre-release

- [ ] Confirm `~/.gradle/gradle.properties` still has the four `JAVAZONE_*` properties.
- [ ] Pull latest `master`, ensure tests pass: `./gradlew :app:test`.
- [ ] Decide the new `versionName`. Convention is `<year>.<minor>.<patch>`:
      - **Patch bump** for hotfixes: `2026.1.0` → `2026.1.1`.
      - **Minor bump** for new features: `2026.1.x` → `2026.2.0`.
- [ ] Always increment `versionCode` by 1 (Play rejects re-uploads of the same code).

### Build

- [ ] Edit `app/build.gradle`: bump `versionCode` and `versionName`.
- [ ] `./gradlew clean :app:bundleRelease`
- [ ] AAB is at `app/build/outputs/bundle/release/app-release.aab`.

### Upload

- [ ] In Play Console → **JavaZone 2026** → **Internal testing → Create new release**.
- [ ] Upload the AAB.
- [ ] Add release notes (English at minimum; Norwegian appreciated).
- [ ] **Save → Review → Start rollout to internal testing**.
- [ ] Verify on device.
- [ ] **Production → Create new release → Promote from internal testing**.
- [ ] Roll out to **100% of users** (or staged rollout — 20% then 100% — for risky
      releases).

### Commit

- [ ] Commit the version bumps with a message like `chore: release 2026.1.1`.
- [ ] Tag: `git tag v2026.1.1 && git push --tags`.

---

## Troubleshooting

### "You uploaded an APK or Android App Bundle that was signed with a key that is also used to sign APKs that are delivered to users"

You uploaded an AAB signed with a **different key than this app's upload key**. Don't try
to "fix" the keystore from the other app — Play won't let you cross-pollinate keys. Ensure
your `~/.gradle/gradle.properties` points at the JavaZone 2026 keystore, not an older one.

### "Keystore was tampered with, or password was incorrect"

The keystore password in `~/.gradle/gradle.properties` doesn't match. Re-fetch from
LastPass.

### Release build succeeds but is unsigned

Your `~/.gradle/gradle.properties` is missing the four `JAVAZONE_*` properties, or has typos
in the names. The `signingConfigs.release` block in `app/build.gradle` is gated on
`project.hasProperty("JAVAZONE_KEYSTORE_PATH")` — if that's false, AGP produces an unsigned
release. Fix the properties file, then `./gradlew clean :app:bundleRelease`.

### "Lost the upload keystore"

Don't panic. In Play Console for JavaZone 2026:
**Test and release → Setup → App integrity → App signing → Request upload key reset**.
Generate a new keystore (step 2 of bootstrap), email Google the new public certificate as
they instruct. Within 24–48h they swap it in. Production users keep updating throughout —
this is the whole point of Play App Signing.
