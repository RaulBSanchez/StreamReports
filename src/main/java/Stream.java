import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Stream {

    private String name;
    private String city;
    private String state;
    private String usgsGaugeId;

    private ArrayList<FishingReport> reports;

    public Stream(String name, String city, String state, String usgsGaugeId ) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.usgsGaugeId = usgsGaugeId;
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

    public Optional<FishingReport> getLatestReport(){
        if (reports.isEmpty()){
            return Optional.empty();
        }

        FishingReport latest = reports.get(0);

        for (FishingReport report : reports){
            if (report.getDate().isAfter(latest.getDate())) {
                latest = report;
            }
        }
        return Optional.of(latest);
    }

    public Optional<FishingReport> getReportByDate(LocalDate date){
        for (FishingReport report : reports){
            if (report.getDate().equals(date)){
                return Optional.of(report);
            }
        }

        return Optional.empty();
    }



    public boolean hasReportForDate(LocalDate date){
        for (FishingReport report: reports){
            if (report.getDate().equals(date)){
                return true;
            }
        }
        return false;
    }


    public boolean updateReport(FishingReport updatedReport){
        for (int i = 0; i < reports.size(); i ++) {
            if (reports.get(i).getDate().equals(updatedReport.getDate())){
                reports.set(i, updatedReport);
                return true;
            }
        }
        return false;
    }

    public boolean deleteReport(LocalDate date){
        for (int i =0; i < reports.size(); i ++){
            if (reports.get(i).getDate().equals(date)){
                reports.remove(i);
                return true;
            }
        }
        return false;
    }

    public void allReports(){
        for (FishingReport report : reports){
            report.displayFishingReport();
        }
    }

    public List<FishingReport> getReports(){
        return Collections.unmodifiableList(reports);
    }

    public String getUsgsGaugeId(){
        return usgsGaugeId;
    }

}