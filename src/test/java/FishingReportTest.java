import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FishingReportTest {
    private FlowReading sampleFlow(){
        return new FlowReading(
            66.4,
            2.55,
            LocalDateTime.of(2026, 10, 9, 9, 30),
            "USGS",
            "https://waterdata.usgs.gov/monitoring-location/USGS-01548030/",
            FlowRating.SKINNY);
    }

    @Test
    void fullConstructorSetsEveryField(){
        FlowReading flow = sampleFlow();

        FishingReport report = new FishingReport(
            LocalDate.of(2026, 10, 9),
            "Low and clear, getting tougher",
            FishingRating.GOOD,
            "Clear",
            52,
            58,
            "Low and clear, with falling leaves.",
            "Use long casts and an upstream presentation.",
            "More water than usual for this time of year.",
            flow
        );

        assertEquals(LocalDate.of(2026, 10, 9), report.getDate());
        assertEquals("Low and clear, getting tougher", report.getHeadline());
        assertEquals(FishingRating.GOOD, report.getRating());
        assertEquals("Clear", report.getWaterClarity());
        assertEquals(52, report.getMinWaterTemperature());
        assertEquals(58, report.getMaxWaterTemperature());
        assertEquals("Low and clear, with falling leaves.", report.getConditionsSummary());
        assertEquals("Use long casts and an upstream presentation.", report.getFishingAdvice());
        assertEquals("More water than usual for this time of year.", report.getNotes());
        assertSame(flow, report.getFlowReading());
    }

    @Test
    void fullConstructorAllowsMissingOptionalFields(){
        FishingReport report = new FishingReport(
            LocalDate.of(2026, 10, 9),
            null,
            null,
            "Clear",
            null,
            null,
            null,
            null,
            null,
            null
        );

        assertNull(report.getRating());
        assertNull(report.getMinWaterTemperature());
        assertNull(report.getMaxWaterTemperature());
        assertNull(report.getFlowReading());
    }

    @Test
    void shortConstructorUsesSameMinAndMaxTemperature(){
        FishingReport report = new FishingReport(
            LocalDate.of(2026, 9, 5),
            57,
            "clear",
            "nothing of note"
        );

        assertEquals(57, report.getMinWaterTemperature());
        assertEquals(57, report.getMaxWaterTemperature());
        assertNull(report.getHeadline());
        assertNull(report.getRating());
        assertNull(report.getFlowReading());
    }

    @Test
    void temperatureRangeCanBeChanged(){
        FishingReport report = new FishingReport(
            LocalDate.of(2026, 9, 5),
            57,
            "clear",
            "nothing of note"
        );

        report.setMinWaterTemperature(50);
        report.setMaxWaterTemperature(61);

        assertEquals(50, report.getMinWaterTemperature());
        assertEquals(61, report.getMaxWaterTemperature());
    }

    @Test
    void flowReadingKeepsGaugeDetails(){
        FlowReading flow = sampleFlow();

        assertEquals(66.4, flow.getFlowCfs(), 0.001);
        assertEquals(2.55, flow.getGaugeHeightFeet(), 0.001);
        assertEquals(LocalDateTime.of(2026, 10, 9, 9, 30), flow.getMeasuredAt());
        assertEquals("USGS", flow.getSource());
        assertEquals("https://waterdata.usgs.gov/monitoring-location/USGS-01548030/", flow.getSourceUrl());
        assertEquals(FlowRating.SKINNY, flow.getRating());
    }

    @Test
    void missingDateIsRejected(){
        assertThrows(IllegalArgumentException.class, () ->
            new FishingReport(null, 57, "clear", "nothing of note"));
    }

    @Test
    void minAboveMaxIsRejectedByConstructor(){
        assertThrows(IllegalArgumentException.class, () ->
            new FishingReport(
                LocalDate.of(2026, 10, 9),
                null,
                null,
                "Clear",
                60,
                52,
                null,
                null,
                null,
                null
            ));
    }

    @Test
    void minAboveMaxIsRejectedBySetters(){
        FishingReport report = new FishingReport(
            LocalDate.of(2026, 9, 5),
            57,
            "clear",
            "nothing of note"
        );

        assertThrows(IllegalArgumentException.class, () -> report.setMinWaterTemperature(60));
        assertThrows(IllegalArgumentException.class, () -> report.setMaxWaterTemperature(50));
        assertEquals(57, report.getMinWaterTemperature());
        assertEquals(57, report.getMaxWaterTemperature());
    }

    @Test
    void negativeFlowIsRejected(){
        assertThrows(IllegalArgumentException.class, () ->
            new FlowReading(
                -1,
                2.55,
                LocalDateTime.of(2026, 10, 9, 9, 30),
                "USGS",
                "https://waterdata.usgs.gov/monitoring-location/USGS-01548030/",
                FlowRating.SKINNY));
    }
}
