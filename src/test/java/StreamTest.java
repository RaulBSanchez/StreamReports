import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

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
}