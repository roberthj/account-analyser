# account-analyser

A Ktor REST service that checks whether the amounts in a text document follow
[Benford's law](https://en.wikipedia.org/wiki/Benford%27s_law), using a chi-square

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
└── service/
    ├── NumberExtractor.kt      # text -> leading digits of amounts
    ├── BenfordDistribution.kt  # expected probabilities
    └── BenfordAnalyser.kt      # orchestrates extract + chi-square
```

## Assumption on input format

The input string is treated as a flattened financial document where the values
to analyse are marked by an `Amount` keyword or similar. Since it comes from another system we can assume some consistency in the data.
For example:

```
Invoice 123 Account 456 Amount 1000.00 Notes paid... Amount: $250.50
```

Amount labels can be provided in the api call.

Unlabelled numbers (invoice IDs, account IDs, freetext digits) are intentionally
ignored — they are typically sequential or arbitrary and break the analysis.

Future improvement: Add fallback for more unstructured input.
For example numbers with $100, €100, 100.00, sum, tot, etc. to filter out irrelevant numbers

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

## Decision rule

Given a chi-square p-value and a chosen significance level α, the API reports
`followsBenfordsLaw = (pValue >= α)` — i.e. we *fail to reject* the null
hypothesis that the leading digits follow Benford's law. Lower α ⇒ stricter
test ⇒ harder to reject.
