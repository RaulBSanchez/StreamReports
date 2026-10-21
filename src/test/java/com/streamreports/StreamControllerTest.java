package com.streamreports;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StreamController.class)
public class StreamControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StreamService streamService;

    @Test
    void returnsStreamsAsJson() throws Exception {
        GaugeReading reading = new GaugeReading(
            77.9, 2.65, 54.0, LocalDateTime.of(2026, 10, 20, 15, 30), "USGS", null, null
        );
        when(streamService.fetchLatestReports()).thenReturn(List.of(
            new StreamReport("Big Fishing Creek", "Lamar", "PA", "01548030", reading, null),
            new StreamReport("Valley Creek", "Valley Forge", "PA", "01473169", null, "Couldn't fetch the USGS reading: timed out")
        ));

        mvc.perform(get("/api/streams"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Big Fishing Creek"))
            .andExpect(jsonPath("$[0].usgsGaugeId").value("01548030"))
            .andExpect(jsonPath("$[0].latestReading.flowCfs").value(77.9))
            .andExpect(jsonPath("$[0].latestReading.waterTemperatureF").value(54.0))
            .andExpect(jsonPath("$[0].latestReading.temperatureRating").value("COOL"))
            .andExpect(jsonPath("$[0].latestReading.measuredAt").value("2026-10-20T15:30:00"))
            .andExpect(jsonPath("$[0].latestReading.flowRating").doesNotExist())
            .andExpect(jsonPath("$[1].latestReading").isEmpty())
            .andExpect(jsonPath("$[1].error").value("Couldn't fetch the USGS reading: timed out"));
    }
}
