package com.streamreports;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Everything a stream gauge measured at one moment. It can't be changed
 * after it is created.
 */
public class GaugeReading {
    private final double flowCfs;
    // Null if the gauge doesn't measure it
    private final Double gaugeHeightFeet;
    private final Double waterTemperatureF;

    private final LocalDateTime measuredAt;
    private final String source;
    private final String sourceUrl;

    public GaugeReading(
        double flowCfs,
        Double gaugeHeightFeet,
        Double waterTemperatureF,
        LocalDateTime measuredAt,
        String source,
        String sourceUrl
    ) {
        // Gauge height isn't checked: USGS heights are measured from a local
        // reference point, so a negative value can be a real reading.
        if (flowCfs < 0) {
            throw new IllegalArgumentException(
                "Flow cannot be negative: " + flowCfs
            );
        }
        if (measuredAt == null) {
            throw new IllegalArgumentException("Measurement time is required");
        }

        this.flowCfs = flowCfs;
        this.gaugeHeightFeet = gaugeHeightFeet;
        this.waterTemperatureF = waterTemperatureF;
        this.measuredAt = measuredAt;
        this.source = source;
        this.sourceUrl = sourceUrl;
    }

    public double getFlowCfs() {
        return flowCfs;
    }

    public Optional<Double> getGaugeHeightFeet() {
        return Optional.ofNullable(gaugeHeightFeet);
    }

    public Optional<Double> getWaterTemperatureF() {
        return Optional.ofNullable(waterTemperatureF);
    }

    /** Worked out from the water temperature; empty if the gauge doesn't measure it. */
    public Optional<TemperatureRating> getTemperatureRating() {
        return getWaterTemperatureF().map(TemperatureRating::fromFahrenheit);
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

    public void displayGaugeReading() {
        System.out.println("Measured at: " + measuredAt);
        System.out.println("Flow: " + flowCfs + " CFS");

        if (gaugeHeightFeet != null) {
            System.out.println("Gauge height: " + gaugeHeightFeet + " ft");
        }
        if (waterTemperatureF != null) {
            System.out.printf(
                "Water temperature: %.1f F (%s)%n",
                waterTemperatureF,
                TemperatureRating.fromFahrenheit(waterTemperatureF).getLabel()
            );
        }

        System.out.println("Source: " + source);
    }
}
