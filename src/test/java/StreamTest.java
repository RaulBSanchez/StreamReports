import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StreamTest {
    private Stream wiss(){
        return new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA",
            "01474000"
        );
    }

    private GaugeReading reading(LocalDateTime measuredAt, double flowCfs){
        return new GaugeReading(flowCfs, 2.5, 55.0, measuredAt, "USGS", null, null);
    }

    @Test
    void duplicateTimeIsRejected(){
        Stream stream = wiss();
        LocalDateTime time = LocalDateTime.of(2026, 9, 5, 9, 30);

        assertTrue(stream.addReading(reading(time, 80)));
        assertFalse(stream.addReading(reading(time, 90)));
        assertEquals(1, stream.getReadings().size());
        assertEquals(80, stream.getReadings().get(0).getFlowCfs(), 0.001);
    }

    @Test
    void zeroReadings(){
        Stream stream = wiss();

        assertTrue(stream.getLatestReading().isEmpty());
        assertTrue(stream.getReadingsOn(LocalDate.of(2026, 9, 5)).isEmpty());
    }

    @Test
    void latestReadingIsMostRecentTimeNotLastAdded(){
        Stream stream = wiss();

        stream.addReading(reading(LocalDateTime.of(2026, 9, 5, 9, 0), 80));
        stream.addReading(reading(LocalDateTime.of(2026, 9, 5, 12, 0), 75));
        stream.addReading(reading(LocalDateTime.of(2026, 9, 4, 18, 0), 90));

        GaugeReading latest = stream.getLatestReading().orElseThrow();

        assertEquals(LocalDateTime.of(2026, 9, 5, 12, 0), latest.getMeasuredAt());
    }

    @Test
    void readingsOnReturnsThatDayEarliestFirst(){
        Stream stream = wiss();

        stream.addReading(reading(LocalDateTime.of(2026, 9, 5, 15, 0), 70));
        stream.addReading(reading(LocalDateTime.of(2026, 9, 4, 23, 45), 99));
        stream.addReading(reading(LocalDateTime.of(2026, 9, 5, 0, 0), 80));
        stream.addReading(reading(LocalDateTime.of(2026, 9, 6, 0, 0), 60));

        List<GaugeReading> day = stream.getReadingsOn(LocalDate.of(2026, 9, 5));

        assertEquals(2, day.size());
        assertEquals(LocalDateTime.of(2026, 9, 5, 0, 0), day.get(0).getMeasuredAt());
        assertEquals(LocalDateTime.of(2026, 9, 5, 15, 0), day.get(1).getMeasuredAt());
    }

    @Test
    void readingsListCannotBeModified(){
        Stream stream = wiss();
        stream.addReading(reading(LocalDateTime.of(2026, 9, 5, 9, 30), 80));

        assertThrows(UnsupportedOperationException.class, () -> stream.getReadings().clear());
        assertThrows(UnsupportedOperationException.class,
            () -> stream.getReadingsOn(LocalDate.of(2026, 9, 5)).clear());
    }

    @Test
    void nullReadingIsRejected(){
        Stream stream = wiss();

        assertThrows(IllegalArgumentException.class, () -> stream.addReading(null));
        assertTrue(stream.getReadings().isEmpty());
    }

    @Test
    void missingNameIsRejected(){
        assertThrows(IllegalArgumentException.class, () -> new Stream(null, "Philadelphia", "PA", "01474000"));
        assertThrows(IllegalArgumentException.class, () -> new Stream("  ", "Philadelphia", "PA", "01474000"));

        Stream stream = wiss();

        assertThrows(IllegalArgumentException.class, () -> stream.setName(null));
        assertEquals("Wissahickon", stream.getName());
    }

    @Test
    void gaugeIdCanBeChanged(){
        Stream stream = wiss();

        stream.setUsgsGaugeId("01548030");

        assertEquals("01548030", stream.getUsgsGaugeId());
    }
}
