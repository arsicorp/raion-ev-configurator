package com.raion.models.signatures;

import com.raion.models.Level1;
import com.raion.models.Option;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// urban commuter signature - pre-configured level 1 premium
// perfect for daily city commuters and tech-savvy professionals
// saves $500 compared to building the same configuration
public class UrbanCommuterSignature extends Level1 {

    private static final double SIGNATURE_PRICE = 55500.00;
    private static final double SAVINGS = 500.00;
    private final List<Option> includedOptions;

    public UrbanCommuterSignature() {
        // pre-configured as level 1 premium in silver
        super(TrimLevel.PREMIUM, VehicleColor.SILVER);

        // build list of included options
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createEnhancedAutopilot());
    }

    @Override
    public double calculatePrice() {
        // fixed signature price instead of base + options
        return SIGNATURE_PRICE;
    }

    // get the signature name
    public String getSignatureName() {
        return "Urban Commuter";
    }

    // get full signature description
    public String getSignatureDescription() {
        return "the perfect daily driver for city professionals. " +
                "combines premium luxury with advanced autonomous driving features " +
                "for stress-free commuting.";
    }

    // get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("included in urban commuter signature:\n");
        features.append("- level 1 premium trim (290 hp, 400 miles range)\n");
        features.append("- liquid silver metallic paint\n");
        features.append("- enhanced autopilot ($6,000 value)\n");
        features.append("- premium audio system (18 speakers)\n");
        features.append("- 20-inch alloy wheels\n");
        features.append("- upgraded nappa leather seats\n");
        features.append("- enhanced ambient lighting\n");
        features.append("- all level 1 premium features\n");

        return features.toString();
    }

    // get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // return defensive copy
    }

    // calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double premiumPrice = 50000; // level 1 premium base
        double enhancedAutopilot = 6000;
        return premiumPrice + enhancedAutopilot;
    }

    // how much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // get target customer description
    public String getTargetCustomer() {
        return "perfect for: daily city commuters, tech-savvy professionals, " +
                "eco-conscious drivers who value convenience and automation";
    }

    @Override
    public String toString() {
        return "Raion Urban Commuter Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Urban Commuter Signature ===\n");
        specs.append("Based on: Level 1 Premium\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // add standard vehicle specs
        specs.append("Body Style: Compact Sedan\n");
        specs.append("Color: ").append(getColor().getDisplayName()).append("\n");
        specs.append("Drivetrain: ").append(getDrivetrain()).append("\n");
        specs.append("Power: ").append(getPower()).append(" hp\n");
        specs.append("0-60 mph: ").append(getAcceleration()).append(" seconds\n");
        specs.append("Top Speed: ").append(getTopSpeed()).append(" mph\n");
        specs.append("Range: ").append(getRange()).append(" miles\n");
        specs.append("Battery: ").append(getBatteryCapacity()).append(" kWh\n");

        return specs.toString();
    }
}