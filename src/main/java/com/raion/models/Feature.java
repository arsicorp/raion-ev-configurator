package com.raion.models;

// Interface for anything that can be added to a vehicle order
// This lets us treat Options, ServicePackages, and Accessories the same way
// Order class uses List<Feature> so it can hold any combination of these

public interface Feature {

    // Basic info every feature needs
    String getName();
    double getPrice();
    String getDescription();

    // Category helps organize features in the UI (like "Autopilot" or "Warranty")
    default String getCategory() {
        return "Feature";
    }

    // Check if this feature works with a specific vehicle level
    // Default is yes for all vehicles, but subclasses can override
    default boolean isEligibleFor(int vehicleLevel) {
        return true;
    }
}
