import java.time.LocalDate;

public class FishingReport {
    private LocalDate date;
    int waterTemperature;
    int waterLevel;
    String waterClarity;
    String notes;

    public FishingReport(
        LocalDate date,
        int waterTemperature,
        int waterLevel,
        String waterClarity,
        String notes
    ) {
        this.date = date;
        this.waterTemperature = waterTemperature;
        this.waterLevel = waterLevel;
        this.waterClarity = waterClarity;
        this.notes = notes;
    }

    public LocalDate getDate(){
        return date;
    }

    public int getWaterTemperature(){
        return waterTemperature;
    }

    public void setWaterTemperature(int waterTemperature){
        this.waterTemperature = waterTemperature;
    }

    public int getWaterLevel(){
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel){
        this.waterLevel = waterLevel;
    }

    public String getWaterClarity(){
        return waterClarity;
    }

    public void setWaterClarity(String waterClarity){
        this.waterClarity = waterClarity;
    }

    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public LocalDate getdate(){
        return date;
    }

    public void displayFishingReport() {
        System.out.println("Fishing Report");
        System.out.println("Date: " + date);
        System.out.println("Water Temperature: " + waterTemperature);
        System.out.println("Water Level: " + waterLevel);
        System.out.println("Water Clarity: " + waterClarity);
        System.out.println("Notes: " + notes);
    }
}