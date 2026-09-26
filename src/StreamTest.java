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



        // Testing Edge Cases

        //1. Duplicate date - > rejected
        System.out.println("==================");
        System.out.println("Test 1: Duplicate Date");

        boolean duplicatedADded = wiss.addReport(report1);

        if (!duplicatedADded){
            System.out.println("Pass: Duplicate date rejected");

        } else{
            System.out.println("Fail: Duplicate date was added");
        }

        //2. Search for a nonexistent date -> return null
        System.out.println("==================");
        System.out.println("Test 2: Nonexistente date");

        FishingReport missingReport = wiss.getReportByDate(LocalDate.of(2026, 8, 1));

        if(missingReport == null){
            System.out.println("PASS: Nonexistent date returned null");

        }else{
            System.out.print("FAIL: Nonexistent date returned a report");
        }


        //3 Get latest with zero reports -> return null
        System.out.println("==================");
        System.out.println("3: Test empty Stream Report");

        Stream emptyStream = new Stream("Empty Stream", "Philadelphia", "PA");

        FishingReport latestEmpty = emptyStream.getLatestReport();

        if(latestEmpty == null){
            System.out.println("Test passeed");
        }
        else{
            System.out.println("Test failed");
        }

        //update a nonexistent report -> no updates
        System.out.println("==================");
        System.out.println("4: Update nonexistent report");

        FishingReport nonexistentReport = new FishingReport(
            LocalDate.of(2026, 8, 1),
            50,
            100,
            "muddy",
            "This report doesn't exist"
        );

        wiss.updateReport(nonexistentReport);

        if(!wiss.hasReportForDate(LocalDate.of(2026, 8, 1))){
            System.out.println("Test Passed, no fake update was updated ");

        }else{
            System.out.println("Test Faile: a nonexistent report was updated");
        }


        // 5. Delete nonexistent report → nothing changes
        System.out.println("--------------------");
        System.out.println("5: Delete nonexistent report");

        FishingReport anotherNonexistentReport = new FishingReport(
            LocalDate.of(2026, 8, 5),
            55,
            150,
            "clear",
            "Doesn't exist"
        );

        wiss.deleteReport(anotherNonexistentReport);

        if (!wiss.hasReportForDate(LocalDate.of(2026, 8, 5))) {
            System.out.println("PASS: Delete did not affect the reports");
        } else {
            System.out.println("FAIL: Something went wrong");
        }
    }
}