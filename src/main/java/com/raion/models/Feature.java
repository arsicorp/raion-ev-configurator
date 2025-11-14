package com.raion.models;

// interface for anything that can be added to a vehicle order
// this lets us treat options, servicepackages, and accessories the same way
// order class uses list<feature> so it can hold any combination of these

public interface Feature {

    // basic info every feature needs
    String getName();
    double getPrice();
    String getDescription();

    // category helps organize features in the ui (like "autopilot" or "warranty")
    default String getCategory() {
        return "Feature";
    }

    // check if this feature works with a specific vehicle level
    // default is yes for all vehicles, but subclasses can override
    default boolean isEligibleFor(int vehicleLevel) {
        return true;
    }
}