package com.raion.models;

// level 1 - compact sedan (inspired by tesla model 3)
// available in: white, black, silver, blue
// trims: standard ($45k), premium ($50k), performance ($55k)
public class Level1 extends Vehicle {

    private static final String MODEL_NAME = "Raion Level 1";
    private static final int BATTERY_CAPACITY = 80; // kwh
    private static final int STANDARD_RANGE = 400; // miles

    public Level1(TrimLevel trimLevel, VehicleColor color) {
        super(MODEL_NAME, trimLevel, color);

        // validate this trim is allowed for level 1
        if (!trimLevel.isLevel1Trim()) {
            throw new IllegalArgumentException(
                    "invalid trim for level 1. must be standard, premium, or performance"
            );
        }

        // validate this color is allowed for level 1
        if (!color.isLevel1Color()) {
            throw new IllegalArgumentException(
                    "invalid color for level 1. must be white, black, silver, or blue"
            );
        }

        // set common specs for all level 1 vehicles
        setBatteryCapacity(BATTERY_CAPACITY);
        setRange(STANDARD_RANGE);

        // configure trim-specific specs
        configureTrimSpecs();
    }

    // set up specs based on which trim was selected
    private void configureTrimSpecs() {
        switch (trimLevel) {
            case STANDARD:
                setBasePrice(45000);
                setPower(290);
                setAcceleration(5.0);
                setTopSpeed(140);
                setDrivetrain("RWD");
                break;

            case PREMIUM:
                setBasePrice(50000);
                setPower(290);
                setAcceleration(5.0);
                setTopSpeed(140);
                setDrivetrain("RWD");
                break;

            case PERFORMANCE:
                setBasePrice(55000);
                setPower(360);
                setAcceleration(4.0);
                setTopSpeed(155);
                setDrivetrain("RWD");
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
        specs.append("Body Style: Compact Sedan\n");
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
        return 1;
    }

    // get trim-specific features description
    public String getTrimFeatures() {
        switch (trimLevel) {
            case STANDARD:
                return "standard features include: basic leather seats, 15-inch touchscreen, " +
                        "basic autopilot, glass panoramic roof, heated/ventilated front seats, " +
                        "heated rear seats, 360-degree cameras, wireless charging";

            case PREMIUM:
                return "premium adds: premium audio (18 speakers), upgraded nappa leather, " +
                        "20-inch alloy wheels, enhanced ambient lighting, ventilated rear seats, " +
                        "premium interior materials";

            case PERFORMANCE:
                return "performance adds: sport seats with bolsters, adaptive sport suspension, " +
                        "performance brakes, 20-inch performance wheels, track mode, launch control, " +
                        "sport steering wheel, carbon fiber accents";

            default:
                return "";
        }
    }
}