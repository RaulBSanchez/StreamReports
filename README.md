# Stream Reports

A Spring Boot app for tracking trout stream conditions from USGS gauges: flow, gauge height and water temperature, with a rating of how safe the water temperature is for trout.

The app runs a web server on port 8080. `GET /api/streams` fetches the latest USGS reading for each stream configured in `application.yml` (five Pennsylvania streams) and returns them as JSON. Readings are kept in memory while the app runs; nothing is saved between runs yet.

A React + Vite frontend lives in `frontend/`. It is only the Vite starter page so far and doesn't call the API yet.

## Requirements

- Java 17+
- Maven 3.x
- Node.js 20.19+ or 22.12+, and npm (for the frontend)

## Running

```sh
mvn test              # run the test suite
mvn spring-boot:run   # start the app on http://localhost:8080 (Ctrl+C to stop)
```

Then open http://localhost:8080/api/streams. Each entry has the stream's name, city, state, `usgsGaugeId`, `latestReading` and `error`. If a USGS request fails, that stream is still included: `error` says what went wrong and `latestReading` is the last reading stored, or `null` if there isn't one.

### Frontend

In a second terminal, with the Spring Boot app running:

```sh
cd frontend
npm install           # first time only
npm run dev           # start the React app on http://localhost:5173
```

During development, Vite forwards requests for `/api/...` to the Spring Boot app on port 8080 (see `frontend/vite.config.js`), so React code can call `fetch('/api/streams')` directly.

To add or change streams, edit `stream-reports.streams` in `src/main/resources/application.yml`.

## Model

| Class | What it holds |
| --- | --- |
| `Stream` | A named stream (city, state, USGS gauge ID) and the gauge readings collected for it. Allows one reading per measurement time. |
| `GaugeReading` | What a gauge measured at one moment: flow (cfs), gauge height (ft, optional), water temperature (°F, optional), time measured, source and source URL. Can't be changed after it is created. |
| `TemperatureRating` | Worked out from water temperature (see below). |
| `UsgsClient` | Fetches the latest `GaugeReading` for a gauge from USGS. |
| `StreamService` | Builds a `Stream` for each configured stream and fetches their latest readings. |
| `StreamController` | Serves `GET /api/streams`. |

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
src/main/java/com/streamreports/   App, StreamController, StreamService, StreamReport, StreamProperties,
                                   Stream, GaugeReading, TemperatureRating, UsgsClient
src/main/resources/application.yml The streams to report on and their USGS gauge IDs
src/test/java/com/streamreports/   StreamTest, GaugeReadingTest, UsgsClientTest, StreamServiceTest,
                                   StreamControllerTest (JUnit 5)
src/test/resources/                Sample USGS response used by UsgsClientTest
frontend/                          React + Vite app (npm; its own package.json and .gitignore)
pom.xml                            Maven build, Java 17, Spring Boot 3.5.6 (starter-web, starter-test)
```

All classes are in the `com.streamreports` package. `App` is the `@SpringBootApplication` class, so Spring finds components in that package and the packages under it.

Build output in `target/` and `bin/` is git-ignored.

## Planned

- Rate flow (e.g. skinny / prime / pushy / blown) from cfs with per-stream thresholds
- Save and load streams and readings as JSON
- More endpoints, e.g. one stream by gauge ID, or a stream's stored readings
