import java.util.ArrayList;

public class Stream {

    private String name;
    private String city;
    private String state;

    private ArrayList<FishingReport> reports;

    public Stream(String name, String city, String state) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.reports = new ArrayList<>();
    }

    public void addReport(FishingReport report) {
        reports.add(report);
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public void displayStream() {
        System.out.println(name);
        System.out.println(city + ", " + state);

        for (FishingReport reporty : reports) {
            reporty.displayFishingReport();
        }
    }
}