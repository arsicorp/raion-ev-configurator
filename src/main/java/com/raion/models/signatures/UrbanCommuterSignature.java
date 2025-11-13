package com.raion.models.signatures;

import com.raion.models.Level1;
import com.raion.models.Option;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// Urban Commuter Signature - Pre-configured Level 1 Premium
// Perfect for daily city commuters and tech-savvy professionals
// Saves $500 compared to building the same configuration
public class UrbanCommuterSignature extends Level1 {

    private static final double SIGNATURE_PRICE = 58000.00;
    private static final double SAVINGS = 500.00;
    private final List<Option> includedOptions;

    public UrbanCommuterSignature() {
        // Pre-configured as Level 1 Premium in Silver
        super(TrimLevel.PREMIUM, VehicleColor.SILVER);

        // Build list of included options
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createEnhancedAutopilot());
    }

    @Override
    public double calculatePrice() {
        // Fixed signature price instead of base + options
        return SIGNATURE_PRICE;
    }

    // Get the signature name
    public String getSignatureName() {
        return "Urban Commuter";
    }

    // Get full signature description
    public String getSignatureDescription() {
        return "The perfect daily driver for city professionals. " +
                "Combines Premium luxury with advanced autonomous driving features " +
                "for stress-free commuting.";
    }

    // Get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("INCLUDED IN URBAN COMMUTER SIGNATURE:\n");
        features.append("- Level 1 Premium trim (290 hp, 400 miles range)\n");
        features.append("- Liquid Silver metallic paint\n");
        features.append("- Enhanced Autopilot ($6,000 value)\n");
        features.append("- Premium audio system (18 speakers)\n");
        features.append("- 20-inch alloy wheels\n");
        features.append("- Upgraded Nappa leather seats\n");
        features.append("- Enhanced ambient lighting\n");
        features.append("- All Level 1 Premium features\n");

        return features.toString();
    }

    // Get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // Return defensive copy
    }

    // Calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double premiumPrice = 50000; // Level 1 Premium base
        double enhancedAutopilot = 6000;
        return premiumPrice + enhancedAutopilot;
    }

    // How much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // Get target customer description
    public String getTargetCustomer() {
        return "Perfect for: Daily city commuters, tech-savvy professionals, " +
                "eco-conscious drivers who value convenience and automation";
    }

    @Override
    public String toString() {
        return "Raion Urban Commuter Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // Override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Urban Commuter Signature ===\n");
        specs.append("Based on: Level 1 Premium\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // Add standard vehicle specs
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
