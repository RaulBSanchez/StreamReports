# Stream Reports

A Spring Boot app for tracking trout stream conditions from USGS gauges: flow, gauge height and water temperature, with a rating of how safe the water temperature is for trout.

Right now, when `App` starts, it fetches the latest USGS reading for five Pennsylvania streams, stores each one on its stream and prints them to the console. The app then keeps running as a web server on port 8080 (no endpoints yet). Nothing is saved between runs yet.

## Requirements

- Java 17+
- Maven 3.x

## Running

```sh
mvn test              # run the test suite
mvn spring-boot:run   # start the app; fetches and prints the live USGS readings (Ctrl+C to stop)
```

## Model

| Class | What it holds |
| --- | --- |
| `Stream` | A named stream (city, state, USGS gauge ID) and the gauge readings collected for it. Allows one reading per measurement time. |
| `GaugeReading` | What a gauge measured at one moment: flow (cfs), gauge height (ft, optional), water temperature (°F, optional), time measured, source, source URL, and a `FlowRating` (optional). Can't be changed after it is created. |
| `TemperatureRating` | Worked out from water temperature (see below). |
| `FlowRating` | `SKINNY`, `PRIME`, `PUSHY`, `BLOWN`. Not set on live readings yet. |
| `UsgsClient` | Fetches the latest `GaugeReading` for a gauge from USGS. |

### Water temperature ratings

| Rating | Water temperature |
| --- | --- |
| `COLD` | below 48°F |
| `COOL` | 48–56°F |
| `PRIME` | 56–64°F |
| `CAUTION` | 64–68°F |
| `NOT_SAFE` ("Not safe for trout") | 68°F and up |

A temperature exactly on a boundary gets the warmer rating: 56°F is `PRIME`, 68°F is `NOT_SAFE`.

### Fetching and storing readings

```java
Stream creek = new Stream("Big Fishing Creek", "Lamar", "PA", "01548030");

GaugeReading live = new UsgsClient().fetchLatest(creek.getUsgsGaugeId());
creek.addReading(live);                 // false if a reading for that time is already stored

creek.getLatestReading();               // Optional<GaugeReading>, by most recent measurement time
creek.getReadingsOn(date);              // that day's readings, earliest first
creek.getReadings();                    // read-only list

live.getWaterTemperatureF();            // Optional<Double>
live.getTemperatureRating();            // Optional<TemperatureRating>
```

`UsgsClient` calls the [USGS Water Data API](https://api.waterdata.usgs.gov/ogcapi/v1/collections/latest-continuous) for discharge (`00060`), gauge height (`00065`) and water temperature (`00010`, converted from °C). No API key is needed. Measurement times are converted from UTC to the computer's time zone. It throws `IOException` if the request fails or the gauge has no current discharge value.

### Validation

These throw `IllegalArgumentException`:

- A `GaugeReading` with negative cfs or no measurement time
- A `Stream` with a `null` or blank name, from the constructor or `setName`
- Passing `null` to `Stream.addReading`
- A blank gauge ID passed to `UsgsClient.fetchLatest`

Gauge height is not checked, because USGS gauge heights are relative to a local reference point and can be negative.

## Project layout

```
src/main/java/com/streamreports/   App, Stream, GaugeReading, TemperatureRating, FlowRating, UsgsClient
src/test/java/com/streamreports/   StreamTest, GaugeReadingTest, UsgsClientTest (JUnit 5)
src/test/resources/                Sample USGS response used by UsgsClientTest
pom.xml                            Maven build, Java 17, Spring Boot 3.5.6 (starter-web, starter-test)
```

All classes are in the `com.streamreports` package. `App` is the `@SpringBootApplication` class, so Spring finds components in that package and the packages under it.

Build output in `target/` and `bin/` is git-ignored.

## Planned

- Work out `FlowRating` from cfs with per-stream thresholds
- Save and load streams and readings as JSON
- Move the hardcoded stream list out of `App`
- REST endpoints for streams and their readings
