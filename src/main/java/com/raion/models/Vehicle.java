package com.raion.models;

// base class for all raion vehicles
// level1, level2, level3, and level4 all extend this
public abstract class Vehicle {

    // shared properties across all vehicles
    protected String modelName;
    protected TrimLevel trimLevel;
    protected VehicleColor color;
    protected double basePrice;
    protected int power; // horsepower
    protected int range; // miles
    protected int batteryCapacity; // kwh
    protected double acceleration; // 0-60 mph time in seconds
    protected int topSpeed; // mph
    protected String drivetrain; // rwd, awd, etc.

    // constructor all subclasses will use
    protected Vehicle(String modelName, TrimLevel trimLevel, VehicleColor color) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("model name cannot be empty");
        }
        if (trimLevel == null) {
            throw new IllegalArgumentException("trim level cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }

        this.modelName = modelName;
        this.trimLevel = trimLevel;
        this.color = color;
    }

    // getters for all properties
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

    // setters for properties that subclasses will configure
    protected void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("base price cannot be negative");
        }
        this.basePrice = basePrice;
    }

    protected void setPower(int power) {
        if (power <= 0) {
            throw new IllegalArgumentException("power must be positive");
        }
        this.power = power;
    }

    protected void setRange(int range) {
        if (range <= 0) {
            throw new IllegalArgumentException("range must be positive");
        }
        this.range = range;
    }

    protected void setBatteryCapacity(int batteryCapacity) {
        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("battery capacity must be positive");
        }
        this.batteryCapacity = batteryCapacity;
    }

    protected void setAcceleration(double acceleration) {
        if (acceleration <= 0) {
            throw new IllegalArgumentException("acceleration time must be positive");
        }
        this.acceleration = acceleration;
    }

    protected void setTopSpeed(int topSpeed) {
        if (topSpeed <= 0) {
            throw new IllegalArgumentException("top speed must be positive");
        }
        this.topSpeed = topSpeed;
    }

    protected void setDrivetrain(String drivetrain) {
        if (drivetrain == null || drivetrain.trim().isEmpty()) {
            throw new IllegalArgumentException("drivetrain cannot be empty");
        }
        this.drivetrain = drivetrain;
    }

    // abstract methods that each vehicle type must implement
    // each level has different pricing logic based on trim
    public abstract double calculatePrice();

    // return formatted specifications for display
    public abstract String getSpecifications();

    // get the vehicle level (1, 2, 3, or 4)
    // used for validation and feature eligibility
    public abstract int getLevel();

    // calculate estimated charging time in hours for level 2 home charging
    // assumes 11 kw charger (typical home setup)
    public double getHomeChargingTime() {
        double chargerPower = 11.0; // kw
        return batteryCapacity / chargerPower;
    }

    // calculate estimated dc fast charging time to 80% in minutes
    // assumes 350 kw fast charger
    public double getFastChargingTime() {
        double chargerPower = 350.0; // kw
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