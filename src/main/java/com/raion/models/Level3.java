package com.raion.models;

// level 3 - performance sedan (inspired by xiaomi su7 max ultra + porsche taycan)
// available in: purple, burgundy, green (exclusive performance colors)
// trims: pro ($125k), max ($130k), ultra ($135k)
// focus: blistering performance with luxury touches
public class Level3 extends Vehicle {

    private static final String MODEL_NAME = "Raion Level 3";
    private static final int BATTERY_CAPACITY = 94; // kwh (93.7 rounded)
    private static final int STANDARD_RANGE = 350; // miles (reduced due to performance)

    public Level3(TrimLevel trimLevel, VehicleColor color) {
        super(MODEL_NAME, trimLevel, color);

        // validate this trim is allowed for level 3
        if (!trimLevel.isLevel3Trim()) {
            throw new IllegalArgumentException(
                    "invalid trim for level 3. must be pro, max, or ultra"
            );
        }

        // validate this color is allowed for level 3
        if (!color.isLevel3Color()) {
            throw new IllegalArgumentException(
                    "invalid color for level 3. must be purple, burgundy, or green"
            );
        }

        // set common specs for all level 3 vehicles
        setBatteryCapacity(BATTERY_CAPACITY);
        setRange(STANDARD_RANGE);
        setDrivetrain("AWD (Tri-Motor)");

        // configure trim-specific specs
        configureTrimSpecs();
    }

    // set up specs based on which trim was selected
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
                throw new IllegalStateException("unexpected trim level: " + trimLevel);
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

        // add charging estimates
        specs.append("\nCharging Times:\n");
        specs.append("Home (Level 2): ").append(String.format("%.1f", getHomeChargingTime())).append(" hours\n");
        specs.append("DC Fast Charge (to 80%): ").append(String.format("%.0f", getFastChargingTime())).append(" minutes\n");

        return specs.toString();
    }

    @Override
    public int getLevel() {
        return 3;
    }

    // get trim-specific features description
    public String getTrimFeatures() {
        switch (trimLevel) {
            case PRO:
                return "pro features include: sport seats with aggressive bolsters, carbon fiber interior trim, " +
                        "alcantara steering wheel, 15-inch touchscreen, glass panoramic roof, track mode, " +
                        "launch control, carbon ceramic brakes, adaptive sport suspension, " +
                        "21-inch forged wheels, enhanced cooling system, all level 1 standard features";

            case MAX:
                return "max adds: premium nappa leather sport seats, premium audio (24 speakers), " +
                        "full alcantara headliner, extended carbon fiber package (interior), " +
                        "21-inch lightweight forged wheels, enhanced ambient lighting, " +
                        "upgraded instrument cluster display, more luxury refinement";

            case ULTRA:
                return "ultra adds: full carbon fiber exterior package (hood, roof, trunk, spoiler), " +
                        "track suspension (adjustable, race-tuned), upgraded carbon ceramic brakes, " +
                        "ultra-lightweight 21-inch carbon wheels, race-spec tires, track telemetry system, " +
                        "lap timer with gps, performance data recorder, front splitter and rear diffuser, active rear wing";

            default:
                return "";
        }
    }

    // check if this is the ultimate performance trim
    public boolean isUltraTrim() {
        return trimLevel == TrimLevel.ULTRA;
    }
}