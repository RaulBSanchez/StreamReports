package com.streamreports;

/**
 * One stream's entry in the /api/streams response. latestReading is null if
 * no reading has been fetched yet; error is null unless the last fetch failed.
 */
public record StreamReport(
    String name,
    String city,
    String state,
    String usgsGaugeId,
    GaugeReading latestReading,
    String error
) {}
