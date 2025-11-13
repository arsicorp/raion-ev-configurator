package com.raion.models.signatures;

import com.raion.models.Level3;
import com.raion.models.Option;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// Track Beast Signature - Pre-configured Level 3 Ultra
// Ultimate performance machine for track day enthusiasts
// Saves $2,000 compared to building the same configuration
public class TrackBeastSignature extends Level3 {

    private static final double SIGNATURE_PRICE = 145000.00;
    private static final double SAVINGS = 2000.00;
    private final List<Option> includedOptions;

    public TrackBeastSignature() {
        // Pre-configured as Level 3 Ultra in Racing Green
        super(TrimLevel.ULTRA, VehicleColor.GREEN);

        // Build list of included options
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createTrackPackage());
    }

    @Override
    public double calculatePrice() {
        // Fixed signature price instead of base + options
        return SIGNATURE_PRICE;
    }

    // Get the signature name
    public String getSignatureName() {
        return "Track Beast";
    }

    // Get full signature description
    public String getSignatureDescription() {
        return "Unleash pure performance on the track. " +
                "1,600 hp of fury with professional-grade track equipment " +
                "for the ultimate driving experience.";
    }

    // Get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("INCLUDED IN TRACK BEAST SIGNATURE:\n");
        features.append("- Level 3 Ultra trim (1,600 hp, 350 miles range)\n");
        features.append("- Racing Green exclusive paint\n");
        features.append("- Track Package ($10,000 value)\n");
        features.append("  * Carbon ceramic brakes (upgraded)\n");
        features.append("  * Track telemetry system\n");
        features.append("  * Lap timer with GPS\n");
        features.append("  * Performance data recorder\n");
        features.append("- Full carbon fiber exterior package\n");
        features.append("- Track suspension (adjustable, race-tuned)\n");
        features.append("- Ultra-lightweight 21-inch carbon wheels\n");
        features.append("- Race-spec tires\n");
        features.append("- Front splitter and rear diffuser\n");
        features.append("- Active rear wing\n");
        features.append("- 0-60 mph in 1.8 seconds\n");
        features.append("- 224 mph top speed\n");

        return features.toString();
    }

    // Get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // Return defensive copy
    }

    // Calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double ultraPrice = 135000; // Level 3 Ultra base
        double trackPackage = 10000;
        return ultraPrice + trackPackage;
    }

    // How much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // Get target customer description
    public String getTargetCustomer() {
        return "Perfect for: Track day enthusiasts, speed lovers, performance junkies, " +
                "drivers who demand the absolute pinnacle of acceleration and handling";
    }

    @Override
    public String toString() {
        return "Raion Track Beast Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // Override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Track Beast Signature ===\n");
        specs.append("Based on: Level 3 Ultra\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // Add standard vehicle specs
        specs.append("Body Style: Performance Sedan\n");
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
