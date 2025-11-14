package com.raion.models.signatures;

import com.raion.models.Level2;
import com.raion.models.ServicePackage;
import com.raion.models.TrimLevel;
import com.raion.models.VehicleColor;

import java.util.ArrayList;
import java.util.List;

// trail titan signature - pre-configured level 2 off-road
// built for adventure seekers and weekend warriors
// saves $1,000 compared to building the same configuration
public class TrailTitanSignature extends Level2 {

    private static final double SIGNATURE_PRICE = 97500.00;
    private static final double SAVINGS = 1000.00;
    private final List<ServicePackage> includedPackages;

    public TrailTitanSignature() {
        // pre-configured as level 2 off-road in black
        super(TrimLevel.OFFROAD, VehicleColor.BLACK);

        // build list of included service packages
        this.includedPackages = new ArrayList<>();
        this.includedPackages.add(ServicePackage.createPremiumMaintenance5Year());
    }

    @Override
    public double calculatePrice() {
        // fixed signature price instead of base + packages
        return SIGNATURE_PRICE;
    }

    // get the signature name
    public String getSignatureName() {
        return "Trail Titan";
    }

    // get full signature description
    public String getSignatureDescription() {
        return "dominate any terrain with confidence. " +
                "combines rugged off-road capability with worry-free maintenance coverage " +
                "for epic adventures.";
    }

    // get what's included in this signature
    public String getIncludedFeatures() {
        StringBuilder features = new StringBuilder();
        features.append("included in trail titan signature:\n");
        features.append("- level 2 off-road trim (670 hp, 450 miles range)\n");
        features.append("- obsidian black paint\n");
        features.append("- premium maintenance package 5-year ($3,500 value)\n");
        features.append("- lifted air suspension (+2 inches ground clearance)\n");
        features.append("- off-road drive modes (rock, sand, mud)\n");
        features.append("- underbody protection (skid plates)\n");
        features.append("- all-terrain tires on reinforced 20-inch wheels\n");
        features.append("- front tow hooks and hill descent control\n");
        features.append("- off-road camera system\n");
        features.append("- all level 2 standard features\n");

        return features.toString();
    }

    // get the list of included service packages
    public List<ServicePackage> getIncludedPackages() {
        return new ArrayList<>(includedPackages); // return defensive copy
    }

    // calculate what the customer would pay if building this manually
    public double getRegularPrice() {
        double offroadPrice = 95000; // level 2 off-road base
        double maintenancePackage = 3500;
        return offroadPrice + maintenancePackage;
    }

    // how much customer saves with signature package
    public double getSavings() {
        return SAVINGS;
    }

    // get target customer description
    public String getTargetCustomer() {
        return "perfect for: adventure seekers, weekend warriors, off-road enthusiasts, " +
                "families who love exploring rugged terrain";
    }

    @Override
    public String toString() {
        return "Raion Trail Titan Signature - $" + String.format("%,.2f", SIGNATURE_PRICE) +
                " (Save $" + String.format("%.2f", SAVINGS) + ")";
    }

    // override specifications to show signature info
    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== Trail Titan Signature ===\n");
        specs.append("Based on: Level 2 Off-Road\n");
        specs.append("Signature Price: $").append(String.format("%,.2f", SIGNATURE_PRICE)).append("\n");
        specs.append("Regular Price: $").append(String.format("%,.2f", getRegularPrice())).append("\n");
        specs.append("You Save: $").append(String.format("%.2f", SAVINGS)).append("\n\n");

        // add standard vehicle specs
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