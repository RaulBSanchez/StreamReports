package com.streamreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Holds the configured streams in memory and fetches their latest USGS
 * readings. Readings are kept on each Stream until the app stops.
 */
@Service
public class StreamService {
    private final UsgsClient client;
    private final List<Stream> streams = new ArrayList<>();

    public StreamService(UsgsClient client, StreamProperties properties) {
        this.client = client;
        for (StreamProperties.StreamConfig config : properties.streams()) {
            streams.add(new Stream(config.name(), config.city(), config.state(), config.usgsGaugeId()));
        }
    }

    /**
     * Fetches each stream's latest reading and reports it. A stream whose fetch
     * fails is still included, with the error and its last stored reading, if any.
     * Synchronized because Stream isn't thread-safe and requests can overlap.
     */
    public synchronized List<StreamReport> fetchLatestReports() throws InterruptedException {
        List<StreamReport> reports = new ArrayList<>();

        for (Stream stream : streams) {
            String error = null;
            try {
                stream.addReading(client.fetchLatest(stream.getUsgsGaugeId()));
            } catch (IOException e) {
                error = "Couldn't fetch the USGS reading: " + e.getMessage();
            }

            reports.add(new StreamReport(
                stream.getName(),
                stream.getCity(),
                stream.getState(),
                stream.getUsgsGaugeId(),
                stream.getLatestReading().orElse(null),
                error
            ));
        }
        return reports;
    }
}
