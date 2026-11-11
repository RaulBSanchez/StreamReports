package com.streamreports;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** The streams to report on, from stream-reports.streams in application.yml. */
@ConfigurationProperties("stream-reports")
public record StreamProperties(List<StreamConfig> streams) {

    // Compact constructor: runs before the record's fields are set.
    // If application.yml lists no streams, use an empty list instead of null.
    // Otherwise make a read-only copy so the list can't be changed later.
    public StreamProperties {
        if (streams == null) {
            streams = List.of();
        } else {
            streams = List.copyOf(streams);
        }
    }

    /**
     * One entry under stream-reports.streams in application.yml.
     * Spring matches yml keys to these fields (e.g. usgs-gauge-id -> usgsGaugeId).
     */
    public record StreamConfig(String name, String city, String state, String usgsGaugeId) {}
}
