package com.raion.models;

// service plans and warranties that customers can add to their order
// these are things like extended warranties, maintenance packages, roadside assistance
public class ServicePackage implements Feature {

    private final String name;
    private final double price;
    private final String description;
    private final int durationYears;
    private final boolean isRecurring; // true for annual plans, false for one-time purchases

    // constructor for one-time service packages (like warranties)
    public ServicePackage(String name, double price, String description, int durationYears) {
        this(name, price, description, durationYears, false);
    }

    // constructor with recurring option (for annual plans)
    public ServicePackage(String name, double price, String description, int durationYears, boolean isRecurring) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("service package name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("service package price cannot be negative");
        }
        if (durationYears <= 0) {
            throw new IllegalArgumentException("duration must be at least 1 year");
        }

        this.name = name;
        this.price = price;
        this.description = description;
        this.durationYears = durationYears;
        this.isRecurring = isRecurring;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return "Service Package";
    }

    public int getDurationYears() {
        return durationYears;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    // factory methods for standard service packages

    public static ServicePackage createBasicWarranty() {
        return new ServicePackage(
                "Basic Warranty",
                0.00, // included free
                "4 years / 50,000 miles comprehensive warranty. 8 years / 100,000 miles battery warranty",
                4
        );
    }

    public static ServicePackage createExtendedWarranty8Year() {
        return new ServicePackage(
                "Extended Warranty - 8 Years",
                5000.00,
                "extends comprehensive warranty to 8 years / 100,000 miles. covers all vehicle components",
                8
        );
    }

    public static ServicePackage createPremiumMaintenance5Year() {
        return new ServicePackage(
                "Premium Maintenance Package - 5 Years",
                3500.00,
                "all scheduled maintenance included for 5 years. tire rotations, brake inspections, software updates",
                5
        );
    }

    public static ServicePackage createPremiumRoadsideAssistance() {
        return new ServicePackage(
                "Premium Roadside Assistance",
                500.00,
                "24/7 roadside support, towing, mobile service, loaner vehicle",
                1,
                true // annual recurring
        );
    }

    @Override
    public String toString() {
        String recurring = isRecurring ? "/year" : "";
        return name + " - $" + String.format("%.2f", price) + recurring + " (" + durationYears + " years)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ServicePackage that = (ServicePackage) obj;
        return Double.compare(that.price, price) == 0
                && durationYears == that.durationYears
                && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + durationYears;
        return result;
    }
}