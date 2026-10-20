package com.streamreports;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public UsgsClient usgsClient() {
        return new UsgsClient();
    }

    // On startup, fetches each stream's latest USGS reading, stores it on the stream and prints it.
    @Bean
    public CommandLineRunner printLatestReadings(UsgsClient client) {
        return args -> {
            List<Stream> streams = List.of(
                new Stream("Big Fishing Creek", "Lamar", "PA", "01548030"),
                new Stream("Tulpehocken Creek", "Reading", "PA", "01470960"),
                new Stream("Pohopoco Creek", "Parryville", "PA", "01449800"),
                new Stream("Wissahickon Creek", "Philadelphia", "PA", "01474000"),
                new Stream("Valley Creek", "Valley Forge", "PA", "01473169")
            );

            for (Stream stream : streams) {
                try {
                    GaugeReading reading =
                        client.fetchLatest(stream.getUsgsGaugeId());

                    stream.addReading(reading);

                } catch (IOException e) {
                    System.out.println(
                        "Couldn't fetch " + stream.getName()
                        + ": " + e.getMessage()
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("USGS requests interrupted.");
                    break;
                }
            }

            for (Stream stream : streams) {
                System.out.println("------------------------------");
                stream.displayStream();
            }
        };
    }
}
