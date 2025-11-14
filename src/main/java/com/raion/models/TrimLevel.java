package com.raion.models;

// all the trim levels we offer across our vehicle lineup
// level 1 (compact sedan): standard, premium, performance
// level 2 (full-size suv): standard, premium, off-road
// level 3 (performance sedan): pro, max, ultra
// level 4 (ultra-luxury suv): flagship only

public enum TrimLevel {

    // level 1 & 2 trims
    STANDARD("Standard", "essential features with impressive performance"),
    PREMIUM("Premium", "enhanced luxury and premium materials"),

    // level 1 only
    PERFORMANCE("Performance", "sport-tuned with enhanced power and handling"),

    // level 2 only
    OFFROAD("Off-Road", "rugged capability for adventure enthusiasts"),

    // level 3 only
    PRO("Pro", "professional-grade performance for driving purists"),
    MAX("Max", "maximum luxury meets blistering performance"),
    ULTRA("Ultra", "ultimate expression of speed and technology"),

    // level 4 only
    FLAGSHIP("Flagship", "the pinnacle of luxury and innovation");

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

    // check which vehicle level can use this trim
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