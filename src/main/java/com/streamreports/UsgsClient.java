package com.streamreports;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Fetches the latest gauge values from the USGS Water Data OGC API
 * (the latest-continuous collection). No API key is needed.
 */
public class UsgsClient {
    private static final String LATEST_URL =
        "https://api.waterdata.usgs.gov/ogcapi/v1/collections/latest-continuous/items";
    private static final String LOCATION_PAGE_URL =
        "https://waterdata.usgs.gov/monitoring-location/";

    // USGS parameter codes: each kind of measurement has a numeric code.
    // Discharge is the flow rate (how much water passes per second).
    static final String DISCHARGE = "00060";          // ft^3/s
    static final String GAUGE_HEIGHT = "00065";       // ft
    static final String WATER_TEMPERATURE = "00010";  // degC

    private final HttpClient http;
    private final ZoneId zone;
    // Jackson's JSON parser
    private final ObjectMapper mapper = new ObjectMapper();

    // Normal constructor used by the app: real HTTP client, the server's time zone
    public UsgsClient() {
        this(
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
            ZoneId.systemDefault()
        );
    }

    // USGS times are UTC; zone is what measuredAt gets converted to.
    // Package-private so tests can pass in a fake HTTP client and a fixed zone.
    UsgsClient(HttpClient http, ZoneId zone) {
        this.http = http;
        this.zone = zone;
    }

    /**
     * Gets the latest reading for a gauge, e.g. "01548030" (without the "USGS-" prefix).
     *
     * @throws IOException if the request fails or the gauge has no current discharge value
     */
    public GaugeReading fetchLatest(String gaugeId) throws IOException, InterruptedException {
        if (gaugeId == null || gaugeId.isBlank()) {
            throw new IllegalArgumentException("Gauge ID is required");
        }

        // Build the request URL. We ask for JSON, skip the map coordinates
        // (we don't need them), and request only the three parameters we use.
        String locationId = URLEncoder.encode("USGS-" + gaugeId, StandardCharsets.UTF_8);
        URI uri = URI.create(
            LATEST_URL
            + "?f=json&skipGeometry=true"
            + "&monitoring_location_id=" + locationId
            + "&parameter_code=" + DISCHARGE + "," + GAUGE_HEIGHT + "," + WATER_TEMPERATURE
        );

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(20))
            .GET()
            .build();

        // Send the request and wait for the response. Anything other than
        // 200 OK means USGS couldn't give us data.
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException(
                "USGS request for gauge " + gaugeId + " failed with status " + response.statusCode()
            );
        }

        return parse(response.body(), gaugeId);
    }

    /**
     * Turns the USGS JSON response into a GaugeReading. Package-private so
     * tests can call it with saved JSON instead of making a real request.
     */
    GaugeReading parse(String json, String gaugeId) throws IOException {
        // Map of parameter code -> its most recent measurement
        Map<String, Measurement> latest = latestByParameter(mapper.readTree(json));

        // Flow is required; gauge height and temperature are optional
        // because not every gauge measures them.
        Measurement discharge = latest.get(DISCHARGE);
        if (discharge == null) {
            throw new IOException("Gauge " + gaugeId + " has no current discharge reading");
        }

        Measurement gaugeHeight = latest.get(GAUGE_HEIGHT);
        Measurement temperature = latest.get(WATER_TEMPERATURE);

        // The reading's time comes from the discharge measurement,
        // converted from UTC to local time.
        return new GaugeReading(
            discharge.value,
            gaugeHeight == null ? null : gaugeHeight.value,
            temperature == null ? null : toFahrenheit(temperature),
            discharge.time.atZoneSameInstant(zone).toLocalDateTime(),
            "USGS",
            LOCATION_PAGE_URL + "USGS-" + gaugeId + "/"
        );
    }

    // A gauge can have more than one time series for a parameter (e.g. two sensors),
    // so keep the most recent value for each. Features with no value are skipped:
    // USGS sends a null value when a gauge is down or iced over.
    private static Map<String, Measurement> latestByParameter(JsonNode root) throws IOException {
        Map<String, Measurement> latest = new HashMap<>();

        // Each "feature" in the response is one parameter's latest value
        for (JsonNode feature : root.path("features")) {
            JsonNode properties = feature.path("properties");
            String code = properties.path("parameter_code").asText();
            String value = properties.path("value").asText(null);
            String time = properties.path("time").asText(null);

            if (value == null || value.isBlank() || time == null) {
                continue;
            }

            // Convert the text values into numbers and dates. If USGS sends
            // something we can't read, report it as an IOException.
            Measurement measurement;
            try {
                measurement = new Measurement(
                    Double.parseDouble(value),
                    properties.path("unit_of_measure").asText(""),
                    OffsetDateTime.parse(time)
                );
            } catch (RuntimeException e) {
                throw new IOException("Unexpected USGS value for parameter " + code + ": " + value, e);
            }

            // Keep this one only if it's the first we've seen for this
            // parameter, or newer than the one we already have.
            Measurement current = latest.get(code);
            if (current == null || measurement.time.isAfter(current.time)) {
                latest.put(code, measurement);
            }
        }

        return latest;
    }

    // USGS usually reports water temperature in Celsius. Convert it unless
    // it's already in Fahrenheit.
    private static double toFahrenheit(Measurement temperature) {
        if (temperature.unit.equalsIgnoreCase("degF")) {
            return temperature.value;
        }
        return temperature.value * 9 / 5 + 32;
    }

    // One value from the USGS response, used only inside this class
    private record Measurement(double value, String unit, OffsetDateTime time) {}
}
