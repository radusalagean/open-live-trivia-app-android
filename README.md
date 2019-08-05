[![Build Status](https://travis-ci.org/radusalagean/open-live-trivia-app-android.svg?branch=master)](https://travis-ci.org/radusalagean/open-live-trivia-app-android)
[![Latest Release](https://img.shields.io/github/release/radusalagean/open-live-trivia-app-android.svg)](https://github.com/radusalagean/open-live-trivia-app-android/releases)

# Open Live Trivia (Android)
![app_icon](app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png)

**Client-side** part of the **Open Live Trivia** project. For the **server** repository, please check [this link](https://github.com/radusalagean/open-live-trivia-api).

## Download
[![Get it on Google Play](https://i.imgur.com/2LewwTZ.png)](https://play.google.com/store/apps/details?id=com.busytrack.openlivetrivia&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1)

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
- [Stetho](http://facebook.github.io/stetho/) - Debugging
- [Timber](https://github.com/JakeWharton/timber) - Logging (Android Logcat wrapper)

## License
Apache License 2.0, see the [LICENSE](LICENSE) file for details.
