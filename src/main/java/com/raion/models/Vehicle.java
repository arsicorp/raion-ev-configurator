package com.raion.models;

// Base class for all Raion vehicles
// Level1, Level2, Level3, and Level4 all extend this
public abstract class Vehicle {

    // Shared properties across all vehicles
    protected String modelName;
    protected TrimLevel trimLevel;
    protected VehicleColor color;
    protected double basePrice;
    protected int power; // horsepower
    protected int range; // miles
    protected int batteryCapacity; // kWh
    protected double acceleration; // 0-60 mph time in seconds
    protected int topSpeed; // mph
    protected String drivetrain; // RWD, AWD, etc.

    // Constructor all subclasses will use
    protected Vehicle(String modelName, TrimLevel trimLevel, VehicleColor color) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }
        if (trimLevel == null) {
            throw new IllegalArgumentException("Trim level cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        this.modelName = modelName;
        this.trimLevel = trimLevel;
        this.color = color;
    }

    // Getters for all properties
    public String getModelName() {
        return modelName;
    }

    public TrimLevel getTrimLevel() {
        return trimLevel;
    }

    public VehicleColor getColor() {
        return color;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getPower() {
        return power;
    }

    public int getRange() {
        return range;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    // Setters for properties that subclasses will configure
    protected void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        this.basePrice = basePrice;
    }

    protected void setPower(int power) {
        if (power <= 0) {
            throw new IllegalArgumentException("Power must be positive");
        }
        this.power = power;
    }

    protected void setRange(int range) {
        if (range <= 0) {
            throw new IllegalArgumentException("Range must be positive");
        }
        this.range = range;
    }

    protected void setBatteryCapacity(int batteryCapacity) {
        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("Battery capacity must be positive");
        }
        this.batteryCapacity = batteryCapacity;
    }

    protected void setAcceleration(double acceleration) {
        if (acceleration <= 0) {
            throw new IllegalArgumentException("Acceleration time must be positive");
        }
        this.acceleration = acceleration;
    }

    protected void setTopSpeed(int topSpeed) {
        if (topSpeed <= 0) {
            throw new IllegalArgumentException("Top speed must be positive");
        }
        this.topSpeed = topSpeed;
    }

    protected void setDrivetrain(String drivetrain) {
        if (drivetrain == null || drivetrain.trim().isEmpty()) {
            throw new IllegalArgumentException("Drivetrain cannot be empty");
        }
        this.drivetrain = drivetrain;
    }

    // Abstract methods that each vehicle type must implement
    // Each level has different pricing logic based on trim
    public abstract double calculatePrice();

    // Return formatted specifications for display
    public abstract String getSpecifications();

    // Get the vehicle level (1, 2, 3, or 4)
    // Used for validation and feature eligibility
    public abstract int getLevel();

    // Calculate estimated charging time in hours for Level 2 home charging
    // Assumes 11 kW charger (typical home setup)
    public double getHomeChargingTime() {
        double chargerPower = 11.0; // kW
        return batteryCapacity / chargerPower;
    }

    // Calculate estimated DC fast charging time to 80% in minutes
    // Assumes 350 kW fast charger
    public double getFastChargingTime() {
        double chargerPower = 350.0; // kW
        double chargeToPercent = 0.8; // charge to 80%
        return (batteryCapacity * chargeToPercent / chargerPower) * 60; // convert hours to minutes
    }

    @Override
    public String toString() {
        return modelName + " " + trimLevel.getDisplayName() + " - " + color.getDisplayName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vehicle vehicle = (Vehicle) obj;
        return modelName.equals(vehicle.modelName)
                && trimLevel == vehicle.trimLevel
                && color == vehicle.color;
    }

    @Override
    public int hashCode() {
        int result = modelName.hashCode();
        result = 31 * result + trimLevel.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}
