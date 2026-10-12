import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.DynamicTest.stream;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

public class StreamTest {
    @Test 
    void duplipcateIsRejected(){
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
        FishingReport missedReport  = wiss.getReportByDate(LocalDate.of(2026, 8,1));
        
        assertNull(missedReport);


    }

    @Test 
    void zeroReorts(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "10000"
        );

        FishingReport latestEmpty = wiss.getLatestReport();

        assertNull(latestEmpty);
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

        stream.updateReport(report);

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

        FishingReport nonexistentReport = new FishingReport(
        LocalDate.of(2026, 8, 5),
        55,
        "clear",
        "Doesn't exist"
        );

        stream.deleteReport(nonexistentReport);
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

    stream.updateReport(newReport);

    FishingReport result = stream.getReportByDate(LocalDate.of(2026, 9, 5));

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

    stream.deleteReport(report);

    assertFalse(
        stream.hasReportForDate(LocalDate.of(2026, 9, 5))
    );
}
}




