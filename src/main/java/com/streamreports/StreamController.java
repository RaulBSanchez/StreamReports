package com.streamreports;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The HTTP API the frontend calls. Every endpoint here lives under /api/streams.
 * This class only handles web requests; the actual work happens in StreamService.
 */
@RestController
@RequestMapping("/api/streams")
public class StreamController {
    private final StreamService streamService;

    // Spring creates the StreamService and passes it in automatically
    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    /**
     * GET /api/streams
     * Fetches fresh USGS readings for every configured stream. Spring turns
     * the returned list into a JSON array.
     */
    @GetMapping
    public List<StreamReport> getStreams() throws InterruptedException {
        return streamService.fetchLatestReports();
    }
}
