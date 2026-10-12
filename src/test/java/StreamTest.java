import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StreamTest {
    @Test
    void duplicateIsRejected(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "10000"
        );

        FishingReport report1 = new FishingReport(LocalDate.of(2026, 9, 5),57, "clear", "nothing of note");
        wiss.addReport(report1);
        boolean addedAgain = wiss.addReport(report1);
        assertFalse(addedAgain);
    }

    @Test
    void missingDate(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "10000"
        );

        assertTrue(wiss.getReportByDate(LocalDate.of(2026, 8,1)).isEmpty());
    }

    @Test
    void zeroReports(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "10000"
        );

        assertTrue(wiss.getLatestReport().isEmpty());
    }

    @Test
    void nonexistentReport(){

        Stream stream = new Stream(
        "Wissahickon",
        "Philadelphia",
        "PA",
        "10000"
        );

        FishingReport report = new FishingReport(
            LocalDate.of(2026, 8, 1),
            50,
            "muddy",
            "This report doesn't exist"
        );

        boolean updated = stream.updateReport(report);

        assertFalse(updated);
        assertFalse(stream.hasReportForDate(LocalDate.of(2026,8,1)));
    }

    @Test
    void deleteNonexistentReport(){
        Stream stream = new Stream(
        "Wissahickon",
        "Philadelphia",
        "PA",
        "10000"
        );

        boolean deleted = stream.deleteReport(LocalDate.of(2026, 8, 5));

        assertFalse(deleted);
        assertFalse(stream.hasReportForDate(LocalDate.of(2026, 8, 5)));
    }

    @Test
    void updateExistingReport(){
        Stream stream = new Stream(
        "Wissahickon",
        "Philadelphia",
        "PA",
        "10000"
    );

    FishingReport originalReport = new FishingReport(
        LocalDate.of(2026, 9, 5),
        57,
        "clear",
        "nothing of note"
    );

    stream.addReport(originalReport);

    FishingReport newReport = new FishingReport(
        LocalDate.of(2026, 9, 5),
        60,
        "muddy",
        "water level increased");

    newReport.setFlowReading(new FlowReading(
        250,
        3.1,
        LocalDateTime.of(2026, 9, 5, 8, 0),
        "USGS",
        "https://waterdata.usgs.gov/monitoring-location/USGS-01474000/",
        FlowRating.PUSHY));

    assertTrue(stream.updateReport(newReport));

    FishingReport result = stream.getReportByDate(LocalDate.of(2026, 9, 5)).orElseThrow();

    assertEquals(60, result.getMinWaterTemperature());
    assertEquals(60, result.getMaxWaterTemperature());
    assertEquals(250, result.getFlowReading().getFlowCfs(), 0.001);
    assertEquals("muddy", result.getWaterClarity());
    assertEquals("water level increased", result.getNotes());

    }


    @Test
    void deleteExistingReport() {
    Stream stream = new Stream(
        "Wissahickon",
        "Philadelphia",
        "PA",
        "01474000"
    );

    FishingReport report = new FishingReport(
        LocalDate.of(2026, 9, 5),
        57,
        "clear",
        "nothing of note"
    );

    stream.addReport(report);

    assertTrue(stream.deleteReport(LocalDate.of(2026, 9, 5)));

    assertFalse(
        stream.hasReportForDate(LocalDate.of(2026, 9, 5))
    );
}

    @Test
    void latestReportIsMostRecentDateNotLastAdded(){
        Stream stream = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "01474000"
        );

        stream.addReport(new FishingReport(LocalDate.of(2026, 9, 5), 57, "clear", "middle"));
        stream.addReport(new FishingReport(LocalDate.of(2026, 9, 20), 54, "clear", "newest"));
        stream.addReport(new FishingReport(LocalDate.of(2026, 8, 30), 62, "stained", "oldest"));

        FishingReport latest = stream.getLatestReport().orElseThrow();

        assertEquals(LocalDate.of(2026, 9, 20), latest.getDate());
        assertEquals("newest", latest.getNotes());
    }

    @Test
    void reportsListCannotBeModified(){
        Stream stream = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "01474000"
        );

        stream.addReport(new FishingReport(LocalDate.of(2026, 9, 5), 57, "clear", "nothing of note"));

        assertEquals(1, stream.getReports().size());
        assertThrows(UnsupportedOperationException.class, () -> stream.getReports().clear());
    }

    @Test
    void deleteOnlyRemovesMatchingDate(){
        Stream stream = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "01474000"
        );

        stream.addReport(new FishingReport(LocalDate.of(2026, 9, 5), 57, "clear", "keep"));
        stream.addReport(new FishingReport(LocalDate.of(2026, 9, 6), 58, "clear", "remove"));

        stream.deleteReport(LocalDate.of(2026, 9, 6));

        assertEquals(1, stream.getReports().size());
        assertTrue(stream.hasReportForDate(LocalDate.of(2026, 9, 5)));
    }
}
