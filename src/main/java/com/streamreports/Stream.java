package com.streamreports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A stream, its USGS gauge, and the gauge readings collected for it, with at
 * most one reading per measurement time.
 */
public class Stream {

    private String name;
    private String city;
    private String state;
    // USGS monitoring location ID, used to look up the stream's flow gauge
    private String usgsGaugeId;

    private ArrayList<GaugeReading> readings;

    public Stream(String name, String city, String state, String usgsGaugeId ) {
        requireName(name);
        this.name = name;
        this.city = city;
        this.state = state;
        this.usgsGaugeId = usgsGaugeId;
        this.readings = new ArrayList<>();
    }

    private static void requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Stream name is required");
        }
    }

    /**
     * Returns false, and adds nothing, if a reading with the same measurement
     * time is already stored. USGS only updates every 15 minutes, so fetching
     * twice in a row gives the same reading.
     */
    public boolean addReading(GaugeReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading is required");
        }
        for (GaugeReading existing : readings) {
            if (existing.getMeasuredAt().equals(reading.getMeasuredAt())) {
                return false;
            }
        }
        readings.add(reading);
        return true;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        requireName(name);
        this.name = name;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getUsgsGaugeId(){
        return usgsGaugeId;
    }

    public void setUsgsGaugeId(String usgsGaugeId){
        this.usgsGaugeId = usgsGaugeId;
    }

    public void displayStream() {
        System.out.println(name);
        System.out.println(city + ", " + state);

        getLatestReading().ifPresentOrElse(
            GaugeReading::displayGaugeReading,
            () -> System.out.println("No gauge readings yet")
        );
    }

    /** The reading with the most recent measurement time, not the one added last. */
    public Optional<GaugeReading> getLatestReading(){
        GaugeReading latest = null;

        for (GaugeReading reading : readings){
            if (latest == null || reading.getMeasuredAt().isAfter(latest.getMeasuredAt())) {
                latest = reading;
            }
        }
        return Optional.ofNullable(latest);
    }

    /** All readings measured on that date, earliest first. */
    public List<GaugeReading> getReadingsOn(LocalDate date){
        List<GaugeReading> matches = new ArrayList<>();

        for (GaugeReading reading : readings){
            if (reading.getMeasuredAt().toLocalDate().equals(date)){
                matches.add(reading);
            }
        }
        matches.sort((a, b) -> a.getMeasuredAt().compareTo(b.getMeasuredAt()));
        return Collections.unmodifiableList(matches);
    }

    /** A read-only list, in the order the readings were added. */
    public List<GaugeReading> getReadings(){
        return Collections.unmodifiableList(readings);
    }
}
