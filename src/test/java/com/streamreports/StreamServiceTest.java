package com.streamreports;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class StreamServiceTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 10, 20, 15, 30);

    private final UsgsClient client = mock(UsgsClient.class);

    private StreamService service(){
        return new StreamService(client, new StreamProperties(List.of(
            new StreamProperties.StreamConfig("Valley Creek", "Valley Forge", "PA", "01473169"),
            new StreamProperties.StreamConfig("Wissahickon Creek", "Philadelphia", "PA", "01474000")
        )));
    }

    private GaugeReading reading(double flowCfs, LocalDateTime measuredAt){
        return new GaugeReading(flowCfs, 2.5, 55.0, measuredAt, "USGS", null, null);
    }

    @Test
    void reportsEveryConfiguredStreamInOrder() throws Exception {
        when(client.fetchLatest("01473169")).thenReturn(reading(40.0, TIME));
        when(client.fetchLatest("01474000")).thenReturn(reading(90.0, TIME));

        List<StreamReport> reports = service().fetchLatestReports();

        assertEquals(2, reports.size());
        assertEquals("Valley Creek", reports.get(0).name());
        assertEquals(40.0, reports.get(0).latestReading().getFlowCfs());
        assertNull(reports.get(0).error());
        assertEquals("Wissahickon Creek", reports.get(1).name());
        assertEquals(90.0, reports.get(1).latestReading().getFlowCfs());
    }

    @Test
    void failedFetchIsReportedWithoutDroppingOtherStreams() throws Exception {
        when(client.fetchLatest("01473169")).thenThrow(new IOException("USGS returned HTTP 503"));
        when(client.fetchLatest("01474000")).thenReturn(reading(90.0, TIME));

        List<StreamReport> reports = service().fetchLatestReports();

        assertEquals(2, reports.size());
        assertNull(reports.get(0).latestReading());
        assertEquals("Couldn't fetch the USGS reading: USGS returned HTTP 503", reports.get(0).error());
        assertEquals(90.0, reports.get(1).latestReading().getFlowCfs());
    }

    @Test
    void failedFetchKeepsLastStoredReading() throws Exception {
        StreamService service = service();
        when(client.fetchLatest("01473169")).thenReturn(reading(40.0, TIME));
        when(client.fetchLatest("01474000")).thenReturn(reading(90.0, TIME));
        service.fetchLatestReports();

        when(client.fetchLatest("01473169")).thenThrow(new IOException("timed out"));
        StreamReport valley = service.fetchLatestReports().get(0);

        assertEquals(40.0, valley.latestReading().getFlowCfs());
        assertEquals("Couldn't fetch the USGS reading: timed out", valley.error());
    }
}
