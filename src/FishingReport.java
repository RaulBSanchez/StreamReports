public class FishingReport {
    String date;
    int waterTemperature;
    int waterLevel;
    String waterClarity;
    String notes;

    public FishingReport(String date, int waterTemperature, int waterLevel, String waterClarity, String notes){
        this.date = date;
        this.waterTemperature = waterTemperature;
        this.waterLevel = waterLevel;
        this.waterClarity = waterClarity;
        this.notes = notes;

    }

    public void displayFishingReport(){
        System.out.println(date + " " + waterTemperature + " " + waterLevel + " " + waterClarity + " " + notes);
    }

}
