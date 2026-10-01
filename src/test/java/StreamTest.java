import java.time.LocalDate;
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
            "PA"
        );

        FishingReport report1 = new FishingReport(LocalDate.of(2026, 9, 5),57 , 200, "clear", "nothing of note");
        wiss.addReport(report1);
        boolean addedAgain = wiss.addReport(report1);
        assertFalse(addedAgain);
    }

    @Test 
    void missingDate(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA"
        );
        FishingReport missedReport  = wiss.getReportByDate(LocalDate.of(2026, 8,1));
        
        assertNull(missedReport);


    }

    @Test 
    void zeroReorts(){
        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA"
        );

        FishingReport latestEmpty = wiss.getLatestReport();

        assertNull(latestEmpty);
    }

    @Test
    void nonexistentReport(){

        Stream stream = new Stream(
        "Wissahickon",
        "Philadelphia",
        "PA"
        );

        FishingReport report = new FishingReport(
            LocalDate.of(2026, 8, 1),
            50,
            100,
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
        "PA"
        );

        FishingReport nonexistentReport = new FishingReport(
        LocalDate.of(2026, 8, 5),
        55,
        150,
        "clear",
        "Doesn't exist"
        );

        stream.deleteReport(nonexistentReport);
        assertFalse(stream.hasReportForDate(LocalDate.of(2026, 8, 5)));

    }
}




