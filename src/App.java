public class App {
    public static void main(String[] args) throws Exception {
       // System.out.println("Hello, World!");
       Stream wiss = new Stream("Wissahickon", "Philadelphia", "PA");
       wiss.displayStream();

       FishingReport wissFishingReport = new FishingReport("today", 157, 200, "beautiful", "no notes");
       wissFishingReport.displayFishingReport();
    }
}
