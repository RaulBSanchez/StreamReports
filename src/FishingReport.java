public class FishingReport {
    String date;
    int waterTemperature;
    int waterLevel;
    String waterClarity;
    String notes;

    public FishingReport(
        String date,
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

    public void displayFishingReport() {
        System.out.println("Fishing Report");
        System.out.println("Date: " + date);
        System.out.println("Water Temperature: " + waterTemperature);
        System.out.println("Water Level: " + waterLevel);
        System.out.println("Water Clarity: " + waterClarity);
        System.out.println("Notes: " + notes);
    }
}