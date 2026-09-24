import java.util.ArrayList;

public class Stream {

    String name;
    String city;
    String state;

    ArrayList<FishingReport> reports;

    public Stream(String name, String city, String state) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.reports = new ArrayList<>();
    }

    public void addReport(FishingReport report) {
        reports.add(report);
    }

    public void displayStream() {
        System.out.println(name);
        System.out.println(city + ", " + state);

        for (FishingReport report : reports) {
            report.displayFishingReport();
        }
    }
}