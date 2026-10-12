import java.time.LocalDate;

public class FishingReport {
    private final LocalDate date;

    private String headline;
    private FishingRating rating;

    private String waterClarity;
    private Integer minWaterTemperature;
    private Integer maxWaterTemperature;

    private String conditionsSummary;
    private String fishingAdvice;
    private String notes;

    private FlowReading flowReading;

    public FishingReport(
        LocalDate date,
        int waterTemperature,
        String waterClarity,
        String notes
    ) {
        this.date = date;
        this.minWaterTemperature = waterTemperature;
        this.maxWaterTemperature = waterTemperature;
        this.waterClarity = waterClarity;
        this.notes = notes;
    }

    public FishingReport(
        LocalDate date,
        String headline,
        FishingRating rating,
        String waterClarity,
        Integer minWaterTemperature,
        Integer maxWaterTemperature,
        String conditionsSummary,
        String fishingAdvice,
        String notes,
        FlowReading flowReading
    ) {
        this.date = date;
        this.headline = headline;
        this.rating = rating;
        this.waterClarity = waterClarity;
        this.minWaterTemperature = minWaterTemperature;
        this.maxWaterTemperature = maxWaterTemperature;
        this.conditionsSummary = conditionsSummary;
        this.fishingAdvice = fishingAdvice;
        this.notes = notes;
        this.flowReading = flowReading;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public FishingRating getRating() {
        return rating;
    }

    public void setRating(FishingRating rating) {
        this.rating = rating;
    }

    public String getWaterClarity() {
        return waterClarity;
    }

    public void setWaterClarity(String waterClarity) {
        this.waterClarity = waterClarity;
    }

    public Integer getMinWaterTemperature() {
        return minWaterTemperature;
    }

    public void setMinWaterTemperature(Integer temperature) {
        this.minWaterTemperature = temperature;
    }

    public Integer getMaxWaterTemperature() {
        return maxWaterTemperature;
    }

    public void setMaxWaterTemperature(Integer temperature) {
        this.maxWaterTemperature = temperature;
    }

    public String getConditionsSummary() {
        return conditionsSummary;
    }

    public void setConditionsSummary(String conditionsSummary) {
        this.conditionsSummary = conditionsSummary;
    }

    public String getFishingAdvice() {
        return fishingAdvice;
    }

    public void setFishingAdvice(String fishingAdvice) {
        this.fishingAdvice = fishingAdvice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public FlowReading getFlowReading() {
        return flowReading;
    }

    public void setFlowReading(FlowReading flowReading) {
        this.flowReading = flowReading;
    }

    public void displayFishingReport() {
        System.out.println("Date: " + date);
        System.out.println("Headline: " + headline);
        System.out.println("Rating: " + rating);
        System.out.println("Water clarity: " + waterClarity);

        if (minWaterTemperature != null
                && maxWaterTemperature != null) {
            System.out.println(
                "Water temperature: "
                + minWaterTemperature + "-"
                + maxWaterTemperature + " F"
            );
        }

        System.out.println("Conditions: " + conditionsSummary);
        System.out.println("Fishing advice: " + fishingAdvice);
        System.out.println("Notes: " + notes);

        if (flowReading != null) {
            flowReading.displayFlowReading();
        }
    }
}