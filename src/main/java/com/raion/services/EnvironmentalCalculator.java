package com.raion.services;

import com.raion.models.Vehicle;

// Calculates environmental impact and benefits of electric vehicles
// Shows CO2 savings compared to gas vehicles
public class EnvironmentalCalculator {

    // Average gas car emits about 4.6 metric tons of CO2 per year
    private static final double GAS_CAR_CO2_PER_YEAR = 4.6; // metric tons

    // One mature tree absorbs about 48 pounds (0.022 metric tons) of CO2 per year
    // So about 50 trees needed to offset 1 ton of CO2
    private static final int TREES_PER_TON = 50;

    // Average annual mileage for calculations
    private static final int ANNUAL_MILES = 12000;

    // Time period for impact calculations (5 years)
    private static final int YEARS = 5;

    // Private constructor - utility class with only static methods
    private EnvironmentalCalculator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Calculate CO2 saved over 5 years compared to a gas vehicle
    public static double calculateCO2Saved(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        // Electric vehicles produce zero direct emissions
        // Savings = what a gas car would have emitted
        return GAS_CAR_CO2_PER_YEAR * YEARS;
    }

    // Calculate how many trees would need to be planted to equal the CO2 savings
    public static int calculateTreesEquivalent(double co2Tons) {
        if (co2Tons < 0) {
            return 0;
        }
        return (int) Math.round(co2Tons * TREES_PER_TON);
    }

    // Get a formatted environmental impact summary
    public static String getImpactSummary(Vehicle vehicle) {
        double co2Saved = calculateCO2Saved(vehicle);
        int trees = calculateTreesEquivalent(co2Saved);

        return String.format(
                "Over 5 years, you'll save approximately %.0f tons of CO2 " +
                        "(equivalent to planting %,d trees) compared to a gas vehicle.",
                co2Saved, trees
        );
    }

    // Calculate estimated home charging time in hours
    // Assumes 11 kW Level 2 home charger (typical)
    public static double calculateHomeChargingTime(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        double chargerPower = 11.0; // kW
        return vehicle.getBatteryCapacity() / chargerPower;
    }

    // Calculate DC fast charging time to 80% in minutes
    // Assumes 350 kW fast charger
    public static double calculateFastChargingTime(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        double chargerPower = 350.0; // kW
        double chargePercent = 0.8; // charge to 80%

        return (vehicle.getBatteryCapacity() * chargePercent / chargerPower) * 60;
    }

    // Get charging summary as formatted string
    public static String getChargingSummary(Vehicle vehicle) {
        double homeTime = calculateHomeChargingTime(vehicle);
        double fastTime = calculateFastChargingTime(vehicle);

        return String.format(
                "Charging: %.1f hours (home) / %.0f minutes (DC fast charge to 80%%)",
                homeTime, fastTime
        );
    }

    // Calculate annual electricity cost (rough estimate)
    // Assumes $0.13 per kWh average US electricity rate
    public static double calculateAnnualElectricityCost(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        double electricityRate = 0.13; // $ per kWh
        double milesPerKwh = (double) vehicle.getRange() / vehicle.getBatteryCapacity();
        double kwhPerYear = ANNUAL_MILES / milesPerKwh;

        return kwhPerYear * electricityRate;
    }

    // Calculate annual gas cost for comparison
    // Assumes $3.50 per gallon and 25 MPG for average gas car
    public static double calculateAnnualGasCost() {
        double gasPrice = 3.50; // $ per gallon
        double mpg = 25.0; // miles per gallon
        double gallonsPerYear = ANNUAL_MILES / mpg;

        return gallonsPerYear * gasPrice;
    }

    // Calculate 5-year fuel savings (gas vs electric)
    public static double calculateFuelSavings(Vehicle vehicle) {
        double electricCost = calculateAnnualElectricityCost(vehicle) * YEARS;
        double gasCost = calculateAnnualGasCost() * YEARS;

        return gasCost - electricCost;
    }
}
