package com.streamreports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

/**
 * Entry point for the StreamReports backend.
 *
 * How a request flows through the app:
 *   StreamController (/api/streams)
 *     -> StreamService (keeps the list of streams from application.yml)
 *       -> UsgsClient (calls the USGS API for each stream's gauge)
 *         -> GaugeReading (one measurement, stored on its Stream)
 *     <- a list of StreamReport objects, sent back as JSON
 */
@SpringBootApplication
// Lets Spring find StreamProperties and fill it from application.yml
@ConfigurationPropertiesScan
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    // UsgsClient isn't a Spring component itself, so we create it here.
    // Spring then passes this one instance to anything that needs it
    // (e.g. StreamService's constructor).
    @Bean
    public UsgsClient usgsClient() {
        return new UsgsClient();
    }
}
