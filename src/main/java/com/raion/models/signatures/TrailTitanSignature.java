package com.raion.models.signatures;

import com.raion.models.Level2;
import com.raion.models.ServicePackage;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// Trail Titan Signature - Pre-configured Level 2 Off-Road
// Built for adventure seekers and weekend warriors
// Saves $1,000 compared to building the same configuration
public class TrailTitanSignature extends Level2 {

    private static final double SIGNATURE_PRICE = 98500.00;
    private static final double SAVINGS = 1000.00;
    private final List<ServicePackage> includedPackages;

    public TrailTitanSignature() {
        // Pre-configured as Level 2 Off-Road in Black
        super(TrimLevel.OFFROAD, VehicleColor.BLACK);

        // Build list of included service packages
        this.includedPackages = new ArrayList<>();
        this.includedPackages.add(ServicePackage.createPremiumMaintenance5Year());
    }

    @Override
    public double calculatePrice() {
        // Fixed signature price instead of base + packages
        return SIGNATURE_PRICE;
    }

    // Get the signature name
    public String getSignatureName() {
        return "Trail Titan";
    }

    // Get full signature description
    public String getSignatureDescription() {
        return "Dominate any terrain with confidence. " +
                "Combines rugged off-road capability with worry-free maintenance coverage " +
                "for epic adventures.";
    }

    // Get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("INCLUDED IN TRAIL TITAN SIGNATURE:\n");
        features.append("- Level 2 Off-Road trim (670 hp, 450 miles range)\n");
        features.append("- Obsidian Black paint\n");
        features.append("- Premium Maintenance Package 5-Year ($3,500 value)\n");
        features.append("- Lifted air suspension (+2 inches ground clearance)\n");
        features.append("- Off-road drive modes (Rock, Sand, Mud)\n");
        features.append("- Underbody protection (skid plates)\n");
        features.append("- All-terrain tires on reinforced 20-inch wheels\n");
        features.append("- Front tow hooks and hill descent control\n");
        features.append("- Off-road camera system\n");
        features.append("- All Level 2 Standard features\n");

        return features.toString();
    }

    // Get the list of included service packages
    public List<ServicePackage> getIncludedPackages() {
        return new ArrayList<>(includedPackages); // Return defensive copy
    }

    // Calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double offroadPrice = 95000; // Level 2 Off-Road base
        double maintenancePackage = 3500;
        return offroadPrice + maintenancePackage;
    }

    // How much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // Get target customer description
    public String getTargetCustomer() {
        return "Perfect for: Adventure seekers, weekend warriors, off-road enthusiasts, " +
                "families who love exploring rugged terrain";
    }

    @Override
    public String toString() {
        return "Raion Trail Titan Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // Override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Trail Titan Signature ===\n");
        specs.append("Based on: Level 2 Off-Road\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // Add standard vehicle specs
        specs.append("Body Style: Full-Size SUV\n");
        specs.append("Seating: 7 passengers\n");
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
