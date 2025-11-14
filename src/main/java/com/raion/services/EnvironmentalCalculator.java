package com.raion.services;

import com.raion.models.Vehicle;

// calculates environmental impact and benefits of electric vehicles
// shows co2 savings compared to gas vehicles
public class EnvironmentalCalculator {

    // average gas car emits about 4.6 metric tons of co2 per year
    private static final double GAS_CAR_CO2_PER_YEAR = 4.6; // metric tons

    // one mature tree absorbs about 48 pounds (0.022 metric tons) of co2 per year
    // so about 50 trees needed to offset 1 ton of co2
    private static final int TREES_PER_TON = 50;

    // average annual mileage for calculations
    private static final int ANNUAL_MILES = 12000;

    // time period for impact calculations (5 years)
    private static final int YEARS = 5;

    // private constructor - utility class with only static methods
    private EnvironmentalCalculator() {
        throw new UnsupportedOperationException("utility class cannot be instantiated");
    }

    // calculate co2 saved over 5 years compared to a gas vehicle
    public static double calculateCO2Saved(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }

        // electric vehicles produce zero direct emissions
        // savings = what a gas car would have emitted
        return GAS_CAR_CO2_PER_YEAR * YEARS;
    }

    // calculate how many trees would need to be planted to equal the co2 savings
    public static int calculateTreesEquivalent(double co2Tons) {
        if (co2Tons < 0) {
            return 0;
        }
        return (int) Math.round(co2Tons * TREES_PER_TON);
    }

    // get a formatted environmental impact summary
    public static String getImpactSummary(Vehicle vehicle) {
        double co2Saved = calculateCO2Saved(vehicle);
        int trees = calculateTreesEquivalent(co2Saved);

        return String.format(
                "over 5 years, you'll save approximately %.0f tons of co2 " +
                        "(equivalent to planting %,d trees) compared to a gas vehicle.",
                co2Saved, trees
        );
    }

    // calculate estimated home charging time in hours
    // assumes 11 kw level 2 home charger (typical)
    public static double calculateHomeChargingTime(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }

        double chargerPower = 11.0; // kw
        return vehicle.getBatteryCapacity() / chargerPower;
    }

    // calculate dc fast charging time to 80% in minutes
    // assumes 350 kw fast charger
    public static double calculateFastChargingTime(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }

        double chargerPower = 350.0; // kw
        double chargePercent = 0.8; // charge to 80%

        return (vehicle.getBatteryCapacity() * chargePercent / chargerPower) * 60;
    }

    // get charging summary as formatted string
    public static String getChargingSummary(Vehicle vehicle) {
        double homeTime = calculateHomeChargingTime(vehicle);
        double fastTime = calculateFastChargingTime(vehicle);

        return String.format(
                "charging: %.1f hours (home) / %.0f minutes (dc fast charge to 80%%)",
                homeTime, fastTime
        );
    }

    // calculate annual electricity cost (rough estimate)
    // assumes $0.13 per kwh average us electricity rate
    public static double calculateAnnualElectricityCost(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }

        double electricityRate = 0.13; // $ per kwh
        double milesPerKwh = (double) vehicle.getRange() / vehicle.getBatteryCapacity();
        double kwhPerYear = ANNUAL_MILES / milesPerKwh;

        return kwhPerYear * electricityRate;
    }

    // calculate annual gas cost for comparison
    // assumes $3.50 per gallon and 25 mpg for average gas car
    public static double calculateAnnualGasCost() {
        double gasPrice = 3.50; // $ per gallon
        double mpg = 25.0; // miles per gallon
        double gallonsPerYear = ANNUAL_MILES / mpg;

        return gallonsPerYear * gasPrice;
    }

    // calculate 5-year fuel savings (gas vs electric)
    public static double calculateFuelSavings(Vehicle vehicle) {
        double electricCost = calculateAnnualElectricityCost(vehicle) * YEARS;
        double gasCost = calculateAnnualGasCost() * YEARS;

        return gasCost - electricCost;
    }
}