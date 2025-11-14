package com.raion.models;

// all available colors for our vehicles
// level 1 & 2: white, black, silver, blue
// level 3: purple, burgundy, green (exclusive performance colors)
// level 4: black only (ultra-exclusive)

public enum VehicleColor {

    // standard colors for level 1 & 2
    WHITE("Pearl White", "#FFFFFF"),
    BLACK("Obsidian Black", "#000000"),
    SILVER("Liquid Silver", "#C0C0C0"),
    BLUE("Electric Blue", "#0066CC"),

    // exclusive colors for level 3 performance vehicles
    PURPLE("Ultraviolet Purple", "#6A0DAD"),
    BURGUNDY("Deep Burgundy", "#800020"),
    GREEN("Racing Green", "#00563B");

    private final String displayName;
    private final String hexCode;

    VehicleColor(String displayName, String hexCode) {
        this.displayName = displayName;
        this.hexCode = hexCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHexCode() {
        return hexCode;
    }

    // check which vehicle level can use this color
    public boolean isLevel1Color() {
        return this == WHITE || this == BLACK || this == SILVER || this == BLUE;
    }

    public boolean isLevel2Color() {
        return this == WHITE || this == BLACK || this == SILVER || this == BLUE;
    }

    public boolean isLevel3Color() {
        return this == PURPLE || this == BURGUNDY || this == GREEN;
    }

    public boolean isLevel4Color() {
        return this == BLACK;
    }

    public boolean isPerformanceColor() {
        return isLevel3Color();
    }

    @Override
    public String toString() {
        return displayName + " (" + hexCode + ")";
    }
}