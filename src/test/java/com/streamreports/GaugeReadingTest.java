package com.streamreports;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GaugeReadingTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 10, 9, 9, 30);

    private GaugeReading withTemperature(Double temperatureF){
        return new GaugeReading(66.4, 2.55, temperatureF, TIME, "USGS", null, null);
    }

    @Test
    void keepsEveryField(){
        GaugeReading reading = new GaugeReading(
            66.4,
            2.55,
            54.5,
            TIME,
            "USGS",
            "https://waterdata.usgs.gov/monitoring-location/USGS-01548030/",
            FlowRating.SKINNY
        );

        assertEquals(66.4, reading.getFlowCfs(), 0.001);
        assertEquals(2.55, reading.getGaugeHeightFeet().orElseThrow(), 0.001);
        assertEquals(54.5, reading.getWaterTemperatureF().orElseThrow(), 0.001);
        assertEquals(TIME, reading.getMeasuredAt());
        assertEquals("USGS", reading.getSource());
        assertEquals("https://waterdata.usgs.gov/monitoring-location/USGS-01548030/", reading.getSourceUrl());
        assertEquals(FlowRating.SKINNY, reading.getFlowRating().orElseThrow());
    }

    @Test
    void optionalFieldsCanBeMissing(){
        GaugeReading reading = new GaugeReading(66.4, null, null, TIME, "USGS", null, null);

        assertTrue(reading.getGaugeHeightFeet().isEmpty());
        assertTrue(reading.getWaterTemperatureF().isEmpty());
        assertTrue(reading.getTemperatureRating().isEmpty());
        assertTrue(reading.getFlowRating().isEmpty());
    }

    @Test
    void negativeGaugeHeightIsAllowed(){
        GaugeReading reading = new GaugeReading(66.4, -0.4, null, TIME, "USGS", null, null);

        assertEquals(-0.4, reading.getGaugeHeightFeet().orElseThrow(), 0.001);
    }

    @Test
    void negativeFlowIsRejected(){
        assertThrows(IllegalArgumentException.class,
            () -> new GaugeReading(-1, 2.55, 54.0, TIME, "USGS", null, null));
    }

    @Test
    void missingTimeIsRejected(){
        assertThrows(IllegalArgumentException.class,
            () -> new GaugeReading(66.4, 2.55, 54.0, null, "USGS", null, null));
    }

    @Test
    void temperatureRatingFollowsTheBands(){
        assertEquals(TemperatureRating.COLD, withTemperature(47.9).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.COOL, withTemperature(48.0).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.COOL, withTemperature(55.9).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.PRIME, withTemperature(56.0).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.PRIME, withTemperature(63.9).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.CAUTION, withTemperature(64.0).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.CAUTION, withTemperature(67.9).getTemperatureRating().orElseThrow());
        assertEquals(TemperatureRating.NOT_SAFE, withTemperature(68.0).getTemperatureRating().orElseThrow());
    }

    @Test
    void notSafeHasAReadableLabel(){
        assertEquals("Not safe for trout", TemperatureRating.NOT_SAFE.getLabel());
    }
}
