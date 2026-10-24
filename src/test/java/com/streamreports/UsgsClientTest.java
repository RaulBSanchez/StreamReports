package com.streamreports;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UsgsClientTest {
    private final UsgsClient utcClient = new UsgsClient(null, ZoneOffset.UTC);

    private String sampleResponse() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/usgs-latest-continuous.json")) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // Builds a response with one feature per {parameterCode, value, time}
    private String response(String[]... features) {
        StringBuilder json = new StringBuilder("{\"type\":\"FeatureCollection\",\"features\":[");
        for (int i = 0; i < features.length; i++) {
            String[] f = features[i];
            if (i > 0) {
                json.append(",");
            }
            String value = f[1] == null ? "null" : "\"" + f[1] + "\"";
            json.append("{\"type\":\"Feature\",\"properties\":{")
                .append("\"parameter_code\":\"").append(f[0]).append("\",")
                .append("\"value\":").append(value).append(",")
                .append("\"time\":\"").append(f[2]).append("\",")
                .append("\"unit_of_measure\":\"\"}}");
        }
        return json.append("]}").toString();
    }

    @Test
    void parsesSampleResponse() throws IOException {
        GaugeReading reading = utcClient.parse(sampleResponse(), "01548030");

        assertEquals(76.8, reading.getFlowCfs(), 0.001);
        assertEquals(2.64, reading.getGaugeHeightFeet().orElseThrow(), 0.001);
        assertEquals(LocalDateTime.of(2026, 10, 13, 19, 30), reading.getMeasuredAt());
        assertEquals("USGS", reading.getSource());
        assertEquals("https://waterdata.usgs.gov/monitoring-location/USGS-01548030/", reading.getSourceUrl());
        assertEquals(56.84, reading.getWaterTemperatureF().orElseThrow(), 0.001);
        assertEquals(TemperatureRating.PRIME, reading.getTemperatureRating().orElseThrow());
    }

    @Test
    void measuredAtIsConvertedToTheClientZone() throws IOException {
        UsgsClient eastern = new UsgsClient(null, ZoneId.of("America/New_York"));

        GaugeReading reading = eastern.parse(sampleResponse(), "01548030");

        assertEquals(LocalDateTime.of(2026, 10, 13, 15, 30), reading.getMeasuredAt());
    }

    @Test
    void missingDischargeIsRejected() {
        String json = response(new String[] {"00065", "2.64", "2026-10-13T19:30:00+00:00"});

        assertThrows(IOException.class, () -> utcClient.parse(json, "01548030"));
    }

    @Test
    void nullDischargeValueIsTreatedAsMissing() {
        String json = response(new String[] {"00060", null, "2026-10-13T19:30:00+00:00"});

        assertThrows(IOException.class, () -> utcClient.parse(json, "01548030"));
    }

    @Test
    void gaugeHeightAndTemperatureAreOptional() throws IOException {
        String json = response(new String[] {"00060", "76.8", "2026-10-13T19:30:00+00:00"});

        GaugeReading reading = utcClient.parse(json, "01548030");

        assertTrue(reading.getGaugeHeightFeet().isEmpty());
        assertTrue(reading.getWaterTemperatureF().isEmpty());
        assertTrue(reading.getTemperatureRating().isEmpty());
    }

    @Test
    void mostRecentValueWinsWhenAParameterRepeats() throws IOException {
        String json = response(
            new String[] {"00060", "90.0", "2026-10-13T19:30:00+00:00"},
            new String[] {"00060", "70.0", "2026-10-13T18:00:00+00:00"}
        );

        GaugeReading reading = utcClient.parse(json, "01548030");

        assertEquals(90.0, reading.getFlowCfs(), 0.001);
    }

    @Test
    void blankGaugeIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new UsgsClient().fetchLatest(" "));
    }
}
