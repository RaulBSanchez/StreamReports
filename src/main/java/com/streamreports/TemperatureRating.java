package com.streamreports;

/**
 * How suitable the water temperature is for trout. Each band includes its
 * lower bound, so 56°F is PRIME and 68°F is NOT_SAFE.
 */
public enum TemperatureRating {
    COLD("Cold"),
    COOL("Cool"),
    PRIME("Prime"),
    CAUTION("Caution"),
    NOT_SAFE("Not safe for trout");

    // Human-friendly name shown to users, e.g. "Prime"
    private final String label;

    TemperatureRating(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Picks the rating for a temperature:
     *   below 48°F -> COLD, 48–55 -> COOL, 56–63 -> PRIME,
     *   64–67 -> CAUTION, 68 and up -> NOT_SAFE
     */
    public static TemperatureRating fromFahrenheit(double temperatureF) {
        if (temperatureF < 48) {
            return COLD;
        }
        if (temperatureF < 56) {
            return COOL;
        }
        if (temperatureF < 64) {
            return PRIME;
        }
        if (temperatureF < 68) {
            return CAUTION;
        }
        return NOT_SAFE;
    }
}
