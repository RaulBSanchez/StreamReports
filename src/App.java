import java.time.LocalDate;

public class App {
    public static void main(String[] args) {

        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA"
        );

        FishingReport wissFishingReport = new FishingReport(
            LocalDate.of(2026, 9, 25),
            57,
            200,
            "clear",
            "No notes"
        );

        FishingReport wissFishingReport1 = new FishingReport(
            LocalDate.of(2026, 9, 24),
            57,
            200,
            "clear",
            "No notes"
        );


        FishingReport wissFishingReport3 = new FishingReport(
            LocalDate.of(2026, 9, 2),
            57,
            200,
            "clear",
            "No notes"
        );

        wiss.addReport(wissFishingReport);
        wiss.addReport(wissFishingReport1);
        wiss.addReport(wissFishingReport3);

        
        if (!wiss.hasReportForDate(wissFishingReport3.getDate())){
            wiss.addReport(wissFishingReport3);
            System.out.print("it worked");

        }
        else{
            System.out.println("didnt work, its already in there");
        }
    


        //wiss.displayStream();

        //System.out.println(wiss.getName() + " name");

        FishingReport latest = wiss.getLatesReport();
        System.out.println("--------------------");
        latest.displayFishingReport();

        FishingReport report = wiss.getReportByDate(
            LocalDate.of(2026, 9, 2));

        report.displayFishingReport();
    }
}