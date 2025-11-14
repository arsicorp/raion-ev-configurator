package com.raion.models;

// level 4 - ultra-luxury suv (inspired by yangwang u8 + rolls royce cullinan)
// available in: black only (ultra-exclusive)
// only one trim: flagship ($185k)
// 4-seater ultra-luxury configuration with everything included
public class Level4 extends Vehicle {

    private static final String MODEL_NAME = "Raion Level 4";
    private static final int BATTERY_CAPACITY = 120; // kwh
    private static final int FLAGSHIP_RANGE = 620; // miles (longest range)
    private static final int SEATING_CAPACITY = 4; // ultra-luxury 4-seater

    public Level4(VehicleColor color) {
        // level 4 only has flagship trim, so we hardcode it
        super(MODEL_NAME, TrimLevel.FLAGSHIP, color);

        // validate this color is allowed for level 4 (black only)
        if (!color.isLevel4Color()) {
            throw new IllegalArgumentException(
                    "invalid color for level 4. must be black only"
            );
        }

        // set all specs for the flagship
        setBasePrice(185000);
        setBatteryCapacity(BATTERY_CAPACITY);
        setRange(FLAGSHIP_RANGE);
        setPower(1180); // quad motor
        setAcceleration(3.2);
        setTopSpeed(155); // luxury focused, not max speed
        setDrivetrain("AWD (Quad Motor)");
    }

    // convenience constructor that automatically uses black
    public Level4() {
        this(VehicleColor.BLACK);
    }

    @Override
    public double calculatePrice() {
        return basePrice;
    }

    @Override
    public String getSpecifications() {
        StringBuilder specs = new StringBuilder();
        specs.append("=== ").append(modelName).append(" Flagship ===\n");
        specs.append("Body Style: Ultra-Luxury SUV\n");
        specs.append("Seating: ").append(SEATING_CAPACITY).append(" passengers (Executive Configuration)\n");
        specs.append("Color: ").append(color.getDisplayName()).append("\n");
        specs.append("Drivetrain: ").append(drivetrain).append("\n");
        specs.append("Power: ").append(power).append(" hp\n");
        specs.append("0-60 mph: ").append(acceleration).append(" seconds\n");
        specs.append("Top Speed: ").append(topSpeed).append(" mph\n");
        specs.append("Range: ").append(range).append(" miles (Industry Leading)\n");
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
        return 4;
    }

    public int getSeatingCapacity() {
        return SEATING_CAPACITY;
    }

    // everything included description - no separate trims
    public String getIncludedFeatures() {
        return "everything included: premium nappa leather everywhere, real wood trim (sapele), " +
                "24+ speaker ultra-premium audio system, adaptive air suspension (self-leveling), " +
                "full self-driving capability (included), executive rear seats with zero-gravity mode, " +
                "power recline, power leg rests, heated/ventilated/cooled seats, 18-point adjustment, " +
                "executive tray tables, refrigerator/cooler between rear seats, dual rear entertainment screens, " +
                "starlight headliner (led constellation), panoramic glass roof, customizable ambient lighting (64 colors), " +
                "360-degree cameras with night vision, ar head-up display, 3 wireless phone charging pads (50w fast charge), " +
                "rear-wheel steering, all advanced safety features, hand-stitched everything, bespoke customization available";
    }

    // level 4 is the only model with full self-driving included
    public boolean hasFullSelfDrivingIncluded() {
        return true;
    }
}