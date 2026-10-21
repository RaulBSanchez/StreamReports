package com.streamreports;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/streams")
public class StreamController {
    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    /** Fetches fresh USGS readings for every configured stream. */
    @GetMapping
    public List<StreamReport> getStreams() throws InterruptedException {
        return streamService.fetchLatestReports();
    }
}
