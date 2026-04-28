# account-analyser

A Ktor REST service that checks whether the numbers in a free-form text follow
[Benford's law](https://en.wikipedia.org/wiki/Benford%27s_law), using a chi-square
goodness-of-fit test.

## API

`POST /api/v1/analyse`

Request:
```json
{
  "text": "invoice 123 for 45.67 ...",
  "significanceLevel": 0.05
}
```

Response:
```json
{
  "sampleSize": 312,
  "significanceLevel": 0.05,
  "pValue": 0.42,
  "followsBenfordsLaw": true,
  "distribution": [
    { "digit": 1, "expectedFrequency": 0.301, "actualFrequency": 0.298 },
    ...
  ]
}
```

## Project layout

```
src/main/kotlin/com/epidemicsound/accountanalyser
├── Application.kt              # Ktor entry point + plugins
├── api/
│   ├── AnalysisRoute.kt        # POST /api/v1/analyse
│   └── Dto.kt                  # Request/response models
└── domain/
    ├── NumberExtractor.kt      # text -> leading digits           [TODO]
    ├── BenfordDistribution.kt  # expected probabilities (done)
    └── BenfordAnalyser.kt      # orchestrates the check           [TODO]
```

## Running

You'll need Gradle. Either:

```bash
brew install gradle
gradle wrapper          # generates ./gradlew
./gradlew run
```

…or just open the project in IntelliJ — it'll generate the wrapper for you on
the first import.

## Tests

```bash
./gradlew test
```

## What's left to implement

The TODOs are concentrated in two files:
1. `domain/NumberExtractor.kt` — parse numbers out of the input text.
2. `domain/BenfordAnalyser.kt` — wire the extractor + distribution + chi-square test.

Tests are stubbed out and ready to flesh out alongside the implementations.
