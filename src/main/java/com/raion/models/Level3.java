package com.raion.models;

// Level 3 - Performance Sedan (inspired by Xiaomi SU7 Max Ultra + Porsche Taycan)
// Available in: Purple, Burgundy, Green (exclusive performance colors)
// Trims: Pro ($125k), Max ($130k), Ultra ($135k)
// Focus: Blistering performance with luxury touches
public class Level3 extends Vehicle {

    private static final String MODEL_NAME = "Raion Level 3";
    private static final int BATTERY_CAPACITY = 94; // kWh (93.7 rounded)
    private static final int STANDARD_RANGE = 350; // miles (reduced due to performance)

    public Level3(TrimLevel trimLevel, VehicleColor color) {
        super(MODEL_NAME, trimLevel, color);

        // Validate this trim is allowed for Level 3
        if (!trimLevel.isLevel3Trim()) {
            throw new IllegalArgumentException(
                    "Invalid trim for Level 3. Must be PRO, MAX, or ULTRA"
            );
        }

        // Validate this color is allowed for Level 3
        if (!color.isLevel3Color()) {
            throw new IllegalArgumentException(
                    "Invalid color for Level 3. Must be PURPLE, BURGUNDY, or GREEN"
            );
        }

        // Set common specs for all Level 3 vehicles
        setBatteryCapacity(BATTERY_CAPACITY);
        setRange(STANDARD_RANGE);
        setDrivetrain("AWD (Tri-Motor)");

        // Configure trim-specific specs
        configureTrimSpecs();
    }

    // Set up specs based on which trim was selected
    private void configureTrimSpecs() {
        switch (trimLevel) {
            case PRO:
                setBasePrice(125000);
                setPower(1527);
                setAcceleration(2.0);
                setTopSpeed(217);
                break;

            case MAX:
                setBasePrice(130000);
                setPower(1527);
                setAcceleration(2.0);
                setTopSpeed(217);
                break;

            case ULTRA:
                setBasePrice(135000);
                setPower(1600);
                setAcceleration(1.8);
                setTopSpeed(224);
                break;

            default:
                throw new IllegalStateException("Unexpected trim level: " + trimLevel);
        }
    }

    @Override
    public double calculatePrice() {
        return basePrice;
    }

    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== ").append(modelName).append(" ").append(trimLevel.getDisplayName()).append(" ===\n");
        specs.append("Body Style: Performance Sedan\n");
        specs.append("Color: ").append(color.getDisplayName()).append("\n");
        specs.append("Drivetrain: ").append(drivetrain).append("\n");
        specs.append("Power: ").append(power).append(" hp\n");
        specs.append("0-60 mph: ").append(acceleration).append(" seconds\n");
        specs.append("Top Speed: ").append(topSpeed).append(" mph\n");
        specs.append("Range: ").append(range).append(" miles\n");
        specs.append("Battery: ").append(batteryCapacity).append(" kWh\n");
        specs.append("Base Price: $").append(String.format("%,.2f", basePrice)).append("\n");

        // Add charging estimates
        specs.append("\nCharging Times:\n");
        specs.append("Home (Level 2): ").append(String.format("%.1f", getHomeChargingTime())).append(" hours\n");
        specs.append("DC Fast Charge (to 80%%): ").append(String.format("%.0f", getFastChargingTime())).append(" minutes\n");

        return specs.toString();
    }

    @Override
    public int getLevel() {
        return 3;
    }

    // Get trim-specific features description
    public String getTrimFeatures() {
        switch (trimLevel) {
            case PRO:
                return "Pro features include: Sport seats with aggressive bolsters, carbon fiber interior trim, " +
                        "Alcantara steering wheel, 15-inch touchscreen, glass panoramic roof, Track Mode, " +
                        "Launch Control, carbon ceramic brakes, adaptive sport suspension, " +
                        "21-inch forged wheels, enhanced cooling system, all Level 1 Standard features";

            case MAX:
                return "Max adds: Premium Nappa leather sport seats, premium audio (24 speakers), " +
                        "full Alcantara headliner, extended carbon fiber package (interior), " +
                        "21-inch lightweight forged wheels, enhanced ambient lighting, " +
                        "upgraded instrument cluster display, more luxury refinement";

            case ULTRA:
                return "Ultra adds: Full carbon fiber exterior package (hood, roof, trunk, spoiler), " +
                        "track suspension (adjustable, race-tuned), upgraded carbon ceramic brakes, " +
                        "ultra-lightweight 21-inch carbon wheels, race-spec tires, track telemetry system, " +
                        "lap timer with GPS, performance data recorder, front splitter and rear diffuser, active rear wing";

            default:
                return "";
        }
    }

    // Check if this is the ultimate performance trim
    public boolean isUltraTrim() {
        return trimLevel == TrimLevel.ULTRA;
    }
}
