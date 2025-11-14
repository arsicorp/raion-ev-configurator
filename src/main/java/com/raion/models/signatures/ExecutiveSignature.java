package com.raion.models.signatures;

import com.raion.models.Level4;
import com.raion.models.Option;
import com.raion.models.ServicePackage;

import java.util.ArrayList;
import java.util.List;

// executive signature - pre-configured level 4 flagship
// the ultimate luxury statement for business executives and vips
// saves $2,000 compared to building the same configuration
public class ExecutiveSignature extends Level4 {

    private static final double SIGNATURE_PRICE = 193000.00;
    private static final double SAVINGS = 2000.00;
    private final List<Option> includedOptions;
    private final List<ServicePackage> includedPackages;

    public ExecutiveSignature() {
        // pre-configured as level 4 flagship in black (only option)
        super(); // level4 constructor automatically uses black

        // build list of included options and packages
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createMassageSeatsLevel4());

        this.includedPackages = new ArrayList<>();
        this.includedPackages.add(ServicePackage.createExtendedWarranty8Year());
    }

    @Override
    public double calculatePrice() {
        // fixed signature price instead of base + features
        return SIGNATURE_PRICE;
    }

    // get the signature name
    public String getSignatureName() {
        return "Executive";
    }

    // get full signature description
    public String getSignatureDescription() {
        return "the pinnacle of automotive luxury. " +
                "every conceivable comfort and technology, with comprehensive coverage " +
                "for the most discerning executives.";
    }

    // get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("included in executive signature:\n");
        features.append("- level 4 flagship (1,180 hp, 620 miles range)\n");
        features.append("- obsidian black exclusive paint\n");
        features.append("- massage seats front & rear ($5,000 value)\n");
        features.append("  * 18-point massage functionality\n");
        features.append("- extended warranty 8-year ($5,000 value)\n");
        features.append("  * comprehensive coverage to 8 years/100,000 miles\n");
        features.append("\neverything else already included:\n");
        features.append("- premium nappa leather everywhere\n");
        features.append("- real wood trim (sapele)\n");
        features.append("- 24+ speaker ultra-premium audio\n");
        features.append("- adaptive air suspension (self-leveling)\n");
        features.append("- full self-driving capability (included)\n");
        features.append("- executive rear seats (zero-gravity mode)\n");
        features.append("- refrigerator/cooler between rear seats\n");
        features.append("- starlight headliner (led constellation)\n");
        features.append("- 360-degree cameras with night vision\n");
        features.append("- ar head-up display\n");
        features.append("- rear-wheel steering\n");
        features.append("- hand-stitched everything\n");

        return features.toString();
    }

    // get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // return defensive copy
    }

    // get the list of included service packages
    public List<ServicePackage> getIncludedPackages() {
        return new ArrayList<>(includedPackages); // return defensive copy
    }

    // calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double flagshipPrice = 185000; // level 4 flagship base
        double massageSeats = 5000; // level 4 massage seats price
        double extendedWarranty = 5000;
        // add $2k for the bundled savings to show value
        return flagshipPrice + massageSeats + extendedWarranty + SAVINGS;
    }

    // how much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // get target customer description
    public String getTargetCustomer() {
        return "perfect for: business executives, vips, luxury seekers, " +
                "those who demand the absolute best in comfort, technology, and prestige";
    }

    @Override
    public String toString() {
        return "Raion Executive Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Executive Signature ===\n");
        specs.append("Based on: Level 4 Flagship\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // add standard vehicle specs
        specs.append("Body Style: Ultra-Luxury SUV\n");
        specs.append("Seating: 4 passengers (Executive Configuration)\n");
        specs.append("Color: ").append(getColor().getDisplayName()).append("\n");
        specs.append("Drivetrain: ").append(getDrivetrain()).append("\n");
        specs.append("Power: ").append(getPower()).append(" hp\n");
        specs.append("0-60 mph: ").append(getAcceleration()).append(" seconds\n");
        specs.append("Top Speed: ").append(getTopSpeed()).append(" mph\n");
        specs.append("Range: ").append(getRange()).append(" miles (Industry Leading)\n");
        specs.append("Battery: ").append(getBatteryCapacity()).append(" kWh\n");

        return specs.toString();
    }
}