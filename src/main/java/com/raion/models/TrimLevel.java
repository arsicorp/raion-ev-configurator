package com.raion.models;

// All the trim levels we offer across our vehicle lineup
// Level 1 (Compact Sedan): Standard, Premium, Performance
// Level 2 (Full-Size SUV): Standard, Premium, Off-Road
// Level 3 (Performance Sedan): Pro, Max, Ultra
// Level 4 (Ultra-Luxury SUV): Flagship only

public enum TrimLevel {

    // Level 1 & 2 trims
    STANDARD("Standard", "Essential features with impressive performance"),
    PREMIUM("Premium", "Enhanced luxury and premium materials"),

    // Level 1 only
    PERFORMANCE("Performance", "Sport-tuned with enhanced power and handling"),

    // Level 2 only
    OFFROAD("Off-Road", "Rugged capability for adventure enthusiasts"),

    // Level 3 only
    PRO("Pro", "Professional-grade performance for driving purists"),
    MAX("Max", "Maximum luxury meets blistering performance"),
    ULTRA("Ultra", "Ultimate expression of speed and technology"),

    // Level 4 only
    FLAGSHIP("Flagship", "The pinnacle of luxury and innovation");

    private final String displayName;
    private final String description;

    TrimLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    // Check which vehicle level can use this trim
    public boolean isLevel1Trim() {
        return this == STANDARD || this == PREMIUM || this == PERFORMANCE;
    }

    public boolean isLevel2Trim() {
        return this == STANDARD || this == PREMIUM || this == OFFROAD;
    }

    public boolean isLevel3Trim() {
        return this == PRO || this == MAX || this == ULTRA;
    }

    public boolean isLevel4Trim() {
        return this == FLAGSHIP;
    }

    @Override
    public String toString() {
        return displayName + " (" + name() + ")";
    }
}
