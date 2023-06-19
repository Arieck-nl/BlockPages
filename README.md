# BlockPages Demo app - Android

## DEMO project with:
- Kotlin
- Coroutines
- Ktor
- Koin
- Clean Architecture (lightweight)
- MVVM
- Jetpack Compose
- Compose Navigation
- Compose Paging
- Mockito

## Installation
Clone this repository and import into **Android Studio**
```bash
https://github.com/Arieck-nl/BlockPages
```

## Configuration
### .properties files:

Create a configuration file named `app.properties` and place it in the folder `buildConfig/dev` or `buildConfig/prod`. This file should contain at least the following properties:
```bash
BASE_URL="test.com"
API_URL_PREFIX="https://"
```

The app is currently configured to work plug and play with the [Rick & Morty API](https://rickandmortyapi.com). Just add the link as `BASE_URL`.

With these files, the project should be able to build and run.

## Build variants
Use the Android Studio *Build Variants* button to choose between `dev` and `prod` environment flavors combined with `debug` and `release` build types.

Prod build type requires signing.