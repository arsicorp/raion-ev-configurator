package com.raion.models.signatures;

import com.raion.models.Level3;
import com.raion.models.Option;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// track beast signature - pre-configured level 3 ultra
// ultimate performance machine for track day enthusiasts
// saves $2,000 compared to building the same configuration
public class TrackBeastSignature extends Level3 {

    private static final double SIGNATURE_PRICE = 143000.00;
    private static final double SAVINGS = 2000.00;
    private final List<Option> includedOptions;

    public TrackBeastSignature() {
        // pre-configured as level 3 ultra in racing green
        super(TrimLevel.ULTRA, VehicleColor.GREEN);

        // build list of included options
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createTrackPackage());
    }

    @Override
    public double calculatePrice() {
        // fixed signature price instead of base + options
        return SIGNATURE_PRICE;
    }

    // get the signature name
    public String getSignatureName() {
        return "Track Beast";
    }

    // get full signature description
    public String getSignatureDescription() {
        return "unleash pure performance on the track. " +
                "1,600 hp of fury with professional-grade track equipment " +
                "for the ultimate driving experience.";
    }

    // get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("included in track beast signature:\n");
        features.append("- level 3 ultra trim (1,600 hp, 350 miles range)\n");
        features.append("- racing green exclusive paint\n");
        features.append("- track package ($10,000 value)\n");
        features.append("  * carbon ceramic brakes (upgraded)\n");
        features.append("  * track telemetry system\n");
        features.append("  * lap timer with gps\n");
        features.append("  * performance data recorder\n");
        features.append("- full carbon fiber exterior package\n");
        features.append("- track suspension (adjustable, race-tuned)\n");
        features.append("- ultra-lightweight 21-inch carbon wheels\n");
        features.append("- race-spec tires\n");
        features.append("- front splitter and rear diffuser\n");
        features.append("- active rear wing\n");
        features.append("- 0-60 mph in 1.8 seconds\n");
        features.append("- 224 mph top speed\n");

        return features.toString();
    }

    // get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // return defensive copy
    }

    // calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double ultraPrice = 135000; // level 3 ultra base
        double trackPackage = 10000;
        return ultraPrice + trackPackage;
    }

    // how much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // get target customer description
    public String getTargetCustomer() {
        return "perfect for: track day enthusiasts, speed lovers, performance junkies, " +
                "drivers who demand the absolute pinnacle of acceleration and handling";
    }

    @Override
    public String toString() {
        return "Raion Track Beast Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Track Beast Signature ===\n");
        specs.append("Based on: Level 3 Ultra\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // add standard vehicle specs
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