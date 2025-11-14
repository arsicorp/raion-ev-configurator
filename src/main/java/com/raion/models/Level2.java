package com.raion.models;

// level 2 - full-size suv (inspired by tesla model x + kia ev9)
// available in: white, black, silver, blue
// trims: standard ($85k), premium ($90k), off-road ($95k)
// 7-seater configuration
public class Level2 extends Vehicle {

    private static final String MODEL_NAME = "Raion Level 2";
    private static final int BATTERY_CAPACITY = 100; // kwh
    private static final int STANDARD_RANGE = 450; // miles
    private static final int SEATING_CAPACITY = 7;

    public Level2(TrimLevel trimLevel, VehicleColor color) {
        super(MODEL_NAME, trimLevel, color);

        // validate this trim is allowed for level 2
        if (!trimLevel.isLevel2Trim()) {
            throw new IllegalArgumentException(
                    "invalid trim for level 2. must be standard, premium, or offroad"
            );
        }

        // validate this color is allowed for level 2
        if (!color.isLevel2Color()) {
            throw new IllegalArgumentException(
                    "invalid color for level 2. must be white, black, silver, or blue"
            );
        }

        // set common specs for all level 2 vehicles
        setBatteryCapacity(BATTERY_CAPACITY);
        setRange(STANDARD_RANGE);
        setPower(670); // all trims have same power (dual motor)
        setDrivetrain("AWD");
        setAcceleration(6.0); // all trims same acceleration

        // configure trim-specific pricing and features
        configureTrimSpecs();
    }

    // set up specs based on which trim was selected
    private void configureTrimSpecs() {
        switch (trimLevel) {
            case STANDARD:
                setBasePrice(85000);
                setTopSpeed(130);
                break;

            case PREMIUM:
                setBasePrice(90000);
                setTopSpeed(130);
                break;

            case OFFROAD:
                setBasePrice(95000);
                setTopSpeed(130);
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
        specs.append("Body Style: Full-Size SUV\n");
        specs.append("Seating: ").append(SEATING_CAPACITY).append(" passengers\n");
        specs.append("Color: ").append(color.getDisplayName()).append("\n");
        specs.append("Drivetrain: ").append(drivetrain).append(" (Dual Motor)\n");
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
        return 2;
    }

    public int getSeatingCapacity() {
        return SEATING_CAPACITY;
    }

    // get trim-specific features description
    public String getTrimFeatures() {
        switch (trimLevel) {
            case STANDARD:
                return "standard features include: same leather as level 1, 15-inch touchscreen, " +
                        "basic autopilot, glass panoramic roof, heated/ventilated front seats, " +
                        "heated rear seats (all 3 rows), 360-degree cameras, wireless charging, " +
                        "power folding third row, third row heated seats";

            case PREMIUM:
                return "premium adds: premium audio (22 speakers), upgraded nappa leather throughout, " +
                        "22-inch alloy wheels, enhanced ambient lighting, executive second-row captain's chairs, " +
                        "ventilated second-row seats, power-adjustable second row, premium wood trim";

            case OFFROAD:
                return "off-road adds: lifted air suspension (+2 inches clearance), off-road drive modes " +
                        "(rock, sand, mud), underbody protection (skid plates), all-terrain tires, " +
                        "reinforced 20-inch wheels, front tow hooks, enhanced approach/departure angles, " +
                        "off-road camera system, hill descent control";

            default:
                return "";
        }
    }
}