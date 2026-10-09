import java.time.LocalDate;
import java.time.LocalDateTime;
public class App {
    public static void main(String[] args) {


        Stream creek = new Stream(
            "Big Fishing Creek",
            "Lamar",
            "PA",
            "01548030");

        FlowReading flow = new FlowReading(
            66.4,
            2.55,
            LocalDateTime.of(2026, 10, 9, 9, 30),
            "USGS",
            "https://waterdata.usgs.gov/monitoring-location/USGS-01548030/",
            FlowRating.SKINNY);

        FishingReport report = new FishingReport(
            LocalDate.of(2026, 10, 9),
            "Low and clear, getting tougher",
            FishingRating.GOOD,
            "Clear",
            52,
            58,
            "Low and clear, with falling leaves potentially making fishing tougher.",
            "Use long casts and an upstream presentation. Nymphs have been productive; try streamers in low light or on cloudy days.",
            "More water than usual for this time of year, but conditions are low and clear again.",
            flow
        );

        creek.addReport(report);
        creek.displayStream();
       
    }
}