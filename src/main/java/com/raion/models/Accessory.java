package com.raion.models;

// Physical accessories and add-ons customers can purchase
// Things like floor mats, home chargers, paint protection, etc.
public class Accessory implements Feature {

    private final String name;
    private final double price;
    private final String description;
    private final boolean isInstalled; // true if installed by dealer, false if customer takes home

    // Constructor for accessories (defaults to not installed)
    public Accessory(String name, double price, String description) {
        this(name, price, description, false);
    }

    // Constructor with installation option
    public Accessory(String name, double price, String description, boolean isInstalled) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Accessory name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Accessory price cannot be negative");
        }

        this.name = name;
        this.price = price;
        this.description = description;
        this.isInstalled = isInstalled;
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
        return "Accessory";
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    // Factory methods for standard accessories

    public static Accessory createPremiumFloorMats() {
        return new Accessory(
                "Premium Floor Mats",
                400.00,
                "All-weather floor mats with Raion logo for all rows",
                false
        );
    }

    public static Accessory createHomeCharger() {
        return new Accessory(
                "Home EV Charger (Level 2, 240V)",
                800.00,
                "Wall-mounted Level 2 charger with 25-foot cable. Includes installation kit",
                false
        );
    }

    public static Accessory createPaintProtectionFilm() {
        return new Accessory(
                "Paint Protection Film (Full Front)",
                2000.00,
                "Clear protective film for front bumper, hood, fenders, and mirrors. Professional installation included",
                true // Installed by dealer
        );
    }

    public static Accessory createCeramicCoating() {
        return new Accessory(
                "Ceramic Coating (Full Vehicle)",
                1500.00,
                "Professional-grade ceramic coating for entire vehicle. Provides long-lasting protection and shine",
                true // Installed by dealer
        );
    }

    @Override
    public String toString() {
        String installNote = isInstalled ? " (Installed)" : "";
        return name + " - $" + String.format("%.2f", price) + installNote;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Accessory accessory = (Accessory) obj;
        return Double.compare(accessory.price, price) == 0 && name.equals(accessory.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
