package com.raion.models.signatures;

import com.raion.models.Level4;
import com.raion.models.Option;
import com.raion.models.ServicePackage;

import java.util.ArrayList;
import java.util.List;

// Executive Signature - Pre-configured Level 4 Flagship
// The ultimate luxury statement for business executives and VIPs
// Saves $2,000 compared to building the same configuration
public class ExecutiveSignature extends Level4 {

    private static final double SIGNATURE_PRICE = 195000.00;
    private static final double SAVINGS = 2000.00;
    private final List<Option> includedOptions;
    private final List<ServicePackage> includedPackages;

    public ExecutiveSignature() {
        // Pre-configured as Level 4 Flagship in Black (only option)
        super(); // Level4 constructor automatically uses BLACK

        // Build list of included options and packages
        this.includedOptions = new ArrayList<>();
        this.includedOptions.add(Option.createMassageSeatsLevel4());

        this.includedPackages = new ArrayList<>();
        this.includedPackages.add(ServicePackage.createExtendedWarranty8Year());
    }

    @Override
    public double calculatePrice() {
        // Fixed signature price instead of base + features
        return SIGNATURE_PRICE;
    }

    // Get the signature name
    public String getSignatureName() {
        return "Executive";
    }

    // Get full signature description
    public String getSignatureDescription() {
        return "The pinnacle of automotive luxury. " +
                "Every conceivable comfort and technology, with comprehensive coverage " +
                "for the most discerning executives.";
    }

    // Get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("INCLUDED IN EXECUTIVE SIGNATURE:\n");
        features.append("- Level 4 Flagship (1,180 hp, 620 miles range)\n");
        features.append("- Obsidian Black exclusive paint\n");
        features.append("- Massage Seats Front & Rear ($5,000 value)\n");
        features.append("  * 18-point massage functionality\n");
        features.append("- Extended Warranty 8-Year ($5,000 value)\n");
        features.append("  * Comprehensive coverage to 8 years/100,000 miles\n");
        features.append("\nEVERYTHING ELSE ALREADY INCLUDED:\n");
        features.append("- Premium Nappa leather everywhere\n");
        features.append("- Real wood trim (Sapele)\n");
        features.append("- 24+ speaker ultra-premium audio\n");
        features.append("- Adaptive air suspension (self-leveling)\n");
        features.append("- Full Self-Driving Capability (INCLUDED)\n");
        features.append("- Executive rear seats (zero-gravity mode)\n");
        features.append("- Refrigerator/cooler between rear seats\n");
        features.append("- Starlight headliner (LED constellation)\n");
        features.append("- 360-degree cameras with night vision\n");
        features.append("- AR head-up display\n");
        features.append("- Rear-wheel steering\n");
        features.append("- Hand-stitched everything\n");

        return features.toString();
    }

    // Get the list of included options
    public List<Option> getIncludedOptions() {
        return new ArrayList<>(includedOptions); // Return defensive copy
    }

    // Get the list of included service packages
    public List<ServicePackage> getIncludedPackages() {
        return new ArrayList<>(includedPackages); // Return defensive copy
    }

    // Calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double flagshipPrice = 185000; // Level 4 Flagship base
        double massageSeats = 5000; // Level 4 massage seats price
        double extendedWarranty = 5000;
        // Add $2k for the bundled savings to show value
        return flagshipPrice + massageSeats + extendedWarranty + SAVINGS;
    }

    // How much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // Get target customer description
    public String getTargetCustomer() {
        return "Perfect for: Business executives, VIPs, luxury seekers, " +
                "those who demand the absolute best in comfort, technology, and prestige";
    }

    @Override
    public String toString() {
        return "Raion Executive Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // Override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Executive Signature ===\n");
        specs.append("Based on: Level 4 Flagship\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // Add standard vehicle specs
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
