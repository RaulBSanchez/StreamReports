import java.time.LocalDate;

public class StreamTest {
    public static void main(String[] args) {

        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA"
        );

        FishingReport report1 = new FishingReport(
            LocalDate.of(2026, 9, 25),
            57,
            200,
            "clear",
            "No notes"
        );

        FishingReport report2 = new FishingReport(
            LocalDate.of(2026, 9, 24),
            57,
            200,
            "clear",
            "No notes"
        );

        FishingReport report3 = new FishingReport(
            LocalDate.of(2026, 9, 2),
            57,
            200,
            "clear",
            "No notes"
        );

        wiss.addReport(report1);
        wiss.addReport(report2);
        wiss.addReport(report3);

        System.out.println("Printing all reports");
        wiss.allReports();

        FishingReport latest = wiss.getLatestReport();

        System.out.println("--------------------");
        latest.displayFishingReport();


        FishingReport report = wiss.getReportByDate(
            LocalDate.of(2026, 9, 2)
        );

        report.displayFishingReport();
        System.out.println("this is now after the display");
        wiss.getReportByDate(LocalDate.of(2026, 9, 25)).displayFishingReport();
        FishingReport report3update = new FishingReport(
            LocalDate.of(2026, 9, 25),
            57,
            200,
            "cloudy",
            "No notes"
        );

        
        wiss.updateReport(report3update);
        System.out.println("After update");
        wiss.getReportByDate(LocalDate.of(2026, 9, 25)).displayFishingReport();
        FishingReport ToDeletereport = wiss.getReportByDate(
            LocalDate.of(2026, 9, 2)
        );

        wiss.deleteReport(ToDeletereport);


        System.out.println("Printing all reports");
        wiss.allReports();
    }
}