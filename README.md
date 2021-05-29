Feather
=======

A library for common data objects and parsing, used across ParchmentMC's projects.

There are five subprojects within the repository:

 - **`core`** - the main data interfaces and implementations (immutables and builders).
 - **`io-gson`** - JSON adapters for the [Gson](https://github.com/google/gson) library
 - **`io-moshi`** - JSON adapters for the [Moshi](https://github.com/square/moshi) library.
 - **`io-proguard`** - parsing for ProGuard mapping files
 - **`utils`** - miscellaneous utilities not fit for inclusion in the core library

Installing Feather
==================

Add this to your Gradle buildscript (replacing `${feather_version}` with the actual version of Feather):

```gradle
repositories {
    maven {
        name 'ParchmentMC'
        url 'https://ldtteam.jfrog.io/artifactory/parchmentmc/'
    }
}

dependencies {
    implementation "org.parchmentmc:feather:${feather_version}"
    implementation "org.parchmentmc.feather:io-gson:${feather_version}" // For the Gson adapters
    implementation "org.parchmentmc.feather:io-moshi:${feather_version}" // For the Moshi adapters
    implementation "org.parchmentmc.feather:io-proguard:${feather_version}" // For the ProGuard parser
    implementation "org.parchmentmc.feather:utils:${feather_version}" // For the misc. utilities
}
```

The IO Libraries
----------------

Feather offers JSON adapters for two JSON parsing libraries: Gson and Moshi.

```java
class UsingGson {
    final Gson gson = new GsonBuilder()
            // Required for `MappingDataContainer` and inner data classes
            .registerTypeAdapterFactory(new MDCGsonAdapterFactory())
            // Required for `MappingDataContainer`s and `SourceMetadata`
            .registerTypeAdapter(SimpleVersion.class, new SimpleVersionAdapter())
            // Required for the metadata classes (`SourceMetadata`, `MethodReference`, etc.) and `Named`
            .registerTypeAdapterFactory(new MetadataAdapterFactory())
            // Alternatively, if you only need to serialize `Named` objects
            .registerTypeAdapter(Named.class, new NamedAdapter())
            // Required for parsing manifests: `LauncherManifest`, `VersionManifest`, and their inner data classes
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .build();
} 

class UsingMoshi {
    final Moshi moshi = new Moshi.Builder()
            // Required for `MappingDataContainer` and inner data classes
            .add(new MDCMoshiAdapter())
            // Required for `MappingDataContainer`s and `SourceMetadata`
            .add(new SimpleVersionAdapter())
            // Required for the metadata classes (`SourceMetadata`, `MethodReference`, etc.) and `Named`
            .add(new MetadataMoshiAdapter())
            // Required for parsing manifests: `LauncherManifest`, `VersionManifest`, and their inner data classes
            .add(new OffsetDateTimeAdapter())
            .create();
}
```

License
=======

Copyright (c) 2021 ParchmentMC. This project is licensed under the MIT License (see `LICENSE.txt`). 