import java.time.LocalDateTime;

public class FlowReading {
    private double flowCfs;
    private double gaugeHeightFeet;
    private LocalDateTime measuredAt;
    private String source;
    private String sourceUrl;
    private FlowRating rating;

    public FlowReading(
        double flowCfs,
        double gaugeHeightFeet,
        LocalDateTime measuredAt,
        String source,
        String sourceUrl,
        FlowRating rating
    ) {
        this.flowCfs = flowCfs;
        this.gaugeHeightFeet = gaugeHeightFeet;
        this.measuredAt = measuredAt;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.rating = rating;
    }

    public double getFlowCfs() {
        return flowCfs;
    }

    public double getGaugeHeightFeet() {
        return gaugeHeightFeet;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public String getSource() {
        return source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public FlowRating getRating() {
        return rating;
    }

    public void displayFlowReading() {
        System.out.println("Flow: " + flowCfs + " CFS");
        System.out.println("Gauge height: " + gaugeHeightFeet + " ft");
        System.out.println("Measured at: " + measuredAt);
        System.out.println("Flow rating: " + rating);
        System.out.println("Source: " + source);
    }
}