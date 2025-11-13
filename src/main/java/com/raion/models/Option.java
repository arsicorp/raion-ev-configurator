package com.raion.models;

// Vehicle-specific upgrades and enhancements
// These are the big-ticket options like autopilot systems, massage seats, etc.
public class Option implements Feature {

    private final String name;
    private final double price;
    private final String description;
    private final String category;
    private final Integer restrictedToLevel; // null means available on all levels

    // Constructor for options available on all vehicles
    public Option(String name, double price, String description, String category) {
        this(name, price, description, category, null);
    }

    // Constructor for options restricted to specific vehicle level
    public Option(String name, double price, String description, String category, Integer restrictedToLevel) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Option name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Option price cannot be negative");
        }

        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.restrictedToLevel = restrictedToLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public boolean isEligibleFor(int vehicleLevel) {
        // If no restriction, available on all levels
        if (restrictedToLevel == null) {
            return true;
        }
        // Otherwise, check if it matches the restricted level
        return restrictedToLevel == vehicleLevel;
    }

    // Factory methods for commonly used options
    // Makes it easy to create standard options across the app

    public static Option createEnhancedAutopilot() {
        return new Option(
                "Enhanced Autopilot",
                6000.00,
                "Navigate on Autopilot, Auto Lane Change, Autopark, Summon, Smart Summon",
                "Autopilot"
        );
    }

    public static Option createFullSelfDriving() {
        return new Option(
                "Full Self-Driving Capability",
                8000.00,
                "All Enhanced Autopilot features plus Traffic Light and Stop Sign Control, Autosteer on city streets",
                "Autopilot"
        );
    }

    public static Option createMassageSeats() {
        return new Option(
                "Massage Seats (Front & Rear)",
                3000.00,
                "Multi-point massage functionality for front and rear seats",
                "Comfort"
        );
    }

    public static Option createMassageSeatsLevel4() {
        return new Option(
                "Massage Seats (Front & Rear)",
                5000.00,
                "18-point massage functionality for front and rear executive seats",
                "Comfort",
                4 // Level 4 only
        );
    }

    public static Option createCustomPaint() {
        return new Option(
                "Custom Paint Color",
                2000.00,
                "Exclusive custom paint finish beyond standard color options",
                "Exterior"
        );
    }

    public static Option createTrackPackage() {
        return new Option(
                "Track Package",
                10000.00,
                "Carbon ceramic brakes, track telemetry system, lap timer with GPS, performance data recorder",
                "Performance",
                3 // Level 3 only
        );
    }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Option option = (Option) obj;
        return Double.compare(option.price, price) == 0 && name.equals(option.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
