# SQLiteDatabase (WaKaWaKa)

Simple Android example app that demonstrates using a local SQLite database to store basic profile records (first/middle/last name, address, email).

This repository contains a small Android project (app module under `New/app`) with the following features:
- Add, view, update and delete records stored in a local SQLite database.
- Email format validation on add/update.
- Prevents inserting/updating records with duplicate email addresses.
- DB helper class implemented as `DBHelper` (wraps Android's `SQLiteOpenHelper`).
- Activities:
  - `MainActivity` — add a new record
  - `RecordsActivity` — view list of records
  - `UpdateActivity` — update or delete a selected record

Status
- A branch `fix/sqlite-helper` exists with a number of fixes (rename of helper class, explicit intents, improved DB handling, email validation, duplicate-email checks, and UI improvements). Use that branch as a starting point for development.

Prerequisites
- Android Studio (recommended) or the Android SDK + Gradle command line tools
- Minimum Android SDK configured in the project (open the project with Android Studio and let it sync)

How to build and run (Android Studio)
1. Clone the repo:
   git clone https://github.com/NikkiCutie4405/SQLiteDatabase.git
2. Open the project in Android Studio (`File → Open...` and select the repository).
3. If prompted, let Android Studio sync Gradle and download any required SDK components.
4. Run the app on an emulator or device.

How to build from the command line
1. From the repository root (the folder that contains `New/`):
   - cd New
   - ./gradlew assembleDebug   (Unix/macOS)
   - gradlew.bat assembleDebug (Windows)
2. Install the generated APK on a device/emulator if desired.

Important implementation notes
- The helper class is `DBHelper` (located at `New/app/src/main/java/com/matibag/anew/DBHelper.java`). It provides:
  - AddRecords(...)
  - UpdateRecords(...) (two overloads)
  - DeleteRecord(id)
  - ClearRecord()
  - GetAllData()
  - getData(id)
  - emailExists(email) and emailExistsExcludingId(email, id) to prevent duplicates
- Activities now pass the selected record id to `UpdateActivity` via Intent extras (`"record_id"`) instead of a static field.
- `UpdateActivity` UI includes fields for Address and Email and validates email format using `Patterns.EMAIL_ADDRESS`.

Recommended fixes / checks (if you run into issues)
- Ensure `RecordsActivity` and `UpdateActivity` are declared in `New/app/src/main/AndroidManifest.xml` (inside `<application>`). Example:
  ```
  <activity android:name=".RecordsActivity" />
  <activity android:name=".UpdateActivity" />
  ```
- If you see crashes when opening Update, check logcat for:
  - ActivityNotFoundException (activity not declared in manifest)
  - IllegalArgumentException / NullPointerException (invalid/missing "record_id" extra)
- Use Android Studio to inspect and run; it will show compilation/runtime errors and line numbers.

Contributing
- Create a branch from `fix/sqlite-helper` or `main`:
  - git checkout -b fix/your-change
- Make changes, commit, push, and open a Pull Request back to `main`.

Example TODOs you might want to add
- Add proper list item layout and click-to-edit UX.
- Use RecyclerView instead of ListActivity and ArrayAdapter.
- Add input sanitization and more robust error handling.
- Add unit tests for DB helper logic (instrumented tests).

License
- No license file included. Add a LICENSE if you want to specify reuse terms.

Contact / Support
- If you want me (the assistant) to push prepared changes (I prepared fixes on branch `fix/sqlite-helper`), instruct me to "Push and open PR" and I'll push the changes and open a PR for you. Alternatively, apply the provided files locally and push from your machine.
