# Stream Reports

A Java app for recording trout stream fishing reports: water temperature, clarity, a fishing rating and advice, plus USGS gauge flow readings for each day on the water.

Right now it is a domain model with tests. `App` builds a sample report for Big Fishing Creek (Lamar, PA) and prints it to the console. There is no live data or saved storage yet.

## Requirements

- Java 17+
- Maven 3.x

## Running

```sh
mvn test                                    # run the test suite
mvn compile exec:java -Dexec.mainClass=App  # print the sample report
```

## Model

| Class | What it holds |
| --- | --- |
| `Stream` | A named stream (city, state, USGS gauge ID) and its reports. Allows one report per date. |
| `FishingReport` | One day's conditions: date, headline, `FishingRating`, water clarity, min/max water temperature (°F), conditions summary, fishing advice, notes, and an optional `FlowReading`. |
| `FlowReading` | A gauge measurement: flow (cfs), gauge height (ft), time measured, source, source URL, and a `FlowRating`. |
| `FishingRating` | `POOR`, `FAIR`, `GOOD`, `EXCELLENT` |
| `FlowRating` | `SKINNY`, `PRIME`, `PUSHY`, `BLOWN` |

### Working with reports

```java
Stream creek = new Stream("Big Fishing Creek", "Lamar", "PA", "01548030");

creek.addReport(report);                       // false if a report already exists for that date
creek.getReportByDate(date);                   // Optional<FishingReport>
creek.getLatestReport();                       // Optional<FishingReport>, by most recent date
creek.updateReport(report);                    // replaces the report with the same date; false if none
creek.deleteReport(date);                      // false if no report for that date
creek.getReports();                            // read-only list
```

`FishingReport` has two constructors. One is a short form (date, a single water temperature, clarity, notes) that sets min and max to the same value. The other is a full form that takes every field.

### Validation

These throw `IllegalArgumentException`:

- A `FishingReport` with a `null` date
- A minimum water temperature above the maximum, from the constructor or either setter. When you widen a range upward with the setters, set the max first.
- A `FlowReading` with negative cfs

Gauge height is not checked, because USGS gauge heights are relative to a local reference point and can be negative.

## Project layout

```
src/main/java/   App, Stream, FishingReport, FlowReading, FishingRating, FlowRating
src/test/java/   StreamTest, FishingReportTest (JUnit 5)
pom.xml          Maven build, Java 17, JUnit Jupiter 5.12.2
```

Build output in `target/` and `bin/` is git-ignored.

## Planned

- Fetch live `FlowReading`s from the USGS Instantaneous Values API using each stream's gauge ID
- Work out `FlowRating` from cfs with per-stream thresholds
- Save and load streams and reports as JSON
- Manage multiple streams instead of one hardcoded stream in `App`
