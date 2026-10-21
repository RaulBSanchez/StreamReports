package com.streamreports;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** The streams to report on, from stream-reports.streams in application.yml. */
@ConfigurationProperties("stream-reports")
public record StreamProperties(List<StreamConfig> streams) {

    public StreamProperties {
        streams = streams == null ? List.of() : List.copyOf(streams);
    }

    public record StreamConfig(String name, String city, String state, String usgsGaugeId) {}
}
