[![Latest Release](https://img.shields.io/github/release/radusalagean/open-live-trivia-app-android.svg)](https://github.com/radusalagean/open-live-trivia-app-android/releases)

# Open Live Trivia (Android)
![app_icon](app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png)

**Client-side** part of the **Open Live Trivia** project. For the **server** repository, please check [this link](https://github.com/radusalagean/open-live-trivia-api).

## Screenshots
![screenshot](https://lh3.googleusercontent.com/tEVsZS12aaCzkykI-i0KUkOvkuWOsD7tvAG6yGCp-pKWkHVwbrLwjDgGAZafQY9wUEM=w720-h310-rw)
![screenshot](https://lh3.googleusercontent.com/CHtB2kby25SfkzCum9ihUMCyP7r19SuLVhWv_B3D_-R1Cs734t5poxwHQiCNP8V3Trk=w720-h310-rw)
![screenshot](https://lh3.googleusercontent.com/WNBzGtB1-PLCI2x_kStLeDO8xsF4M2elEmgqksnqXpvbZ_jGkjadlMKQaHlVmQFLny8=w720-h310-rw)
![screenshot](https://lh3.googleusercontent.com/3t3Jc0d9cdngQXgdlrsP0SRlBDnWj0bhCYa0x4EQKARL3Cs6oPV6_DKMsvpJXuh4CCNq=w720-h310-rw)

## Features
Open Live Trivia is an online, real-time multiplayer trivia game. The game offers the ability to compete with other players by answering clues and questions provided by [jservice](http://jservice.io/). The main features are:
- Quick authentication process (no password required)
- Over 156k available questions and clues
- Offline leaderboard persistence
- Entries reported by players as invalid / incorrect can be reviewed and banned by moderators / admin in the app

## Under the hood
For an in-depth look of how the client app and the server work together, please check the [server documentation](https://github.com/radusalagean/open-live-trivia-api/blob/master/README.md).

## Why does this app exist?
The app was built for demonstrative purposes, it's the concrete result of the most recent development knowledge and skills I acquired in the first half of 2019.

## Bug reports
Found a bug in the app? Please open an [issue](https://github.com/radusalagean/open-live-trivia-app-android/issues) or send me an [email](mailto:busytrack@gmail.com). If you can provide a reproduction pattern and/or some screenshots, that would be helpful and appreciated.

## Libraries
- [Socket.io](https://socket.io/) - Real-time, bidirectional server-client communication
- [Retrofit](https://square.github.io/retrofit/) - Networking
- [RxJava](https://github.com/ReactiveX/RxJava) - Concurrency
- [Gson](https://github.com/google/gson) - Json serialization / deserialization
- [Dagger2](https://github.com/google/dagger) - Dependency Injection
- [Room](https://developer.android.com/topic/libraries/architecture/room) - Abstraction layer over SQLite (data persistence for offline use)
- [Glide](https://github.com/bumptech/glide) - Image Loading
- [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics) - Metrics and crash reporting
- [Firebase Auth](https://firebase.google.com/docs/auth) - Authentication
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Kotlin implementation of [coroutines](https://en.wikipedia.org/wiki/Coroutine)
- [Calligraphy](https://github.com/InflationX/Calligraphy/) - Custom fonts support lib
- [Shimmer](https://github.com/facebook/shimmer-android) - Shimmer effects on Android Views
- [Stetho](http://facebook.github.io/stetho/) (**Debug variant only**) - Debugging
- [Timber](https://github.com/JakeWharton/timber) (**Debug variant only**) - Logging (Android Logcat wrapper)
- [JUnit4](https://junit.org/junit4/) (**Test dependency**) - Allows writing and running unit tests on the local machine
- [Mockito](https://github.com/mockito/mockito) (**Test dependency**) - Mocking and stubbing dependencies in isolated unit tests
- [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin) (**Test dependency**) - Mockito wrapper optimized for Kotlin
- [Robolectric](https://github.com/robolectric/robolectric) (**Test dependency**) - Android-related framework components in unit tests
- [Mockk](https://mockk.io/) (**Test dependency**) - Mocks static Kotlin Objects (and more)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) (**Test dependency**) - Testing network calls
- [Google Truth](https://github.com/google/truth) (**Test dependency**) - Improved assertions
- [Espresso](https://developer.android.com/training/testing/espresso) (**Test dependency**) - UI tests

## License
Apache License 2.0, see the [LICENSE](LICENSE) file for details.
