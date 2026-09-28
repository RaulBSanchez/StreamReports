import java.time.LocalDate;
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

    public boolean addReport(FishingReport report) {
        if (hasReportForDate(report.getDate())){
            return false;
        }
        reports.add(report);
        return true;
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

    public FishingReport getLatestReport(){
        if (reports.isEmpty()){
            return null;
        }

        FishingReport latest = reports.get(0);

        for (FishingReport report : reports){
            if (report.getDate().isAfter(latest.getDate())) {
                latest = report;
            }
        }
        return latest;
    }

    public FishingReport getReportByDate(LocalDate date){
        for (FishingReport report : reports){
            if (report.getDate().equals(date)){
                return report;
            }
        }

        return null;
    }



    public boolean hasReportForDate(LocalDate date){
        for (FishingReport report: reports){
            if (report.getDate().equals(date)){
                return true;
            }
        }
        return false;
    }


    public void updateReport(FishingReport updatedReport){
        for (int i = 0; i < reports.size(); i ++) {
            if (reports.get(i).getDate().equals(updatedReport.getDate())){
                reports.set(i, updatedReport);
                return;
            }
        }
    }

    public void deleteReport(FishingReport deleteReport){
        for (int i =0; i < reports.size(); i ++){
            if (reports.get(i).getDate().equals(deleteReport.getDate())){
                reports.remove(i);
                return;
            }
        }
    }

    public void allReports(){
        for (FishingReport report : reports){
            report.displayFishingReport();
        }
    }

    public ArrayList<FishingReport> getReports(){
        return reports;
    }

}