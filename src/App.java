public class App {
    public static void main(String[] args) {

        Stream wiss = new Stream(
            "Wissahickon",
            "Philadelphia",
            "PA"
        );

        FishingReport wissFishingReport = new FishingReport(
            "today",
            57,
            200,
            "clear",
            "No notes"
        );

        FishingReport wissFishingReport1 = new FishingReport(
            "yesterday",
            57,
            200,
            "clear",
            "No notes"
        );

        wiss.addReport(wissFishingReport);
        wiss.addReport(wissFishingReport1);

        wiss.displayStream();

        System.out.println(wiss.getName() + " name");
    }
}