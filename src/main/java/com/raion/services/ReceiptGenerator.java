package com.raion.services;

import com.raion.models.Feature;
import com.raion.models.Order;
import com.raion.models.Vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

// Generates professional receipts for vehicle orders
// Saves receipts to text files in the receipts folder
public class ReceiptGenerator {

    private static final String RECEIPTS_FOLDER = "receipts";
    private static final String LINE_SEPARATOR = "=".repeat(60);

    // Generate receipt text from an order
    public static String generateReceipt(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        StringBuilder receipt = new StringBuilder();
        Vehicle vehicle = order.getVehicle();

        // Header
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("RAION MOTORS\n");
        receipt.append("Electric Vehicle Order Receipt\n");
        receipt.append(LINE_SEPARATOR).append("\n");

        // Order info
        receipt.append("Order ID: ").append(order.getOrderId()).append("\n");
        receipt.append("Date: ").append(order.getFormattedOrderDate()).append("\n");

        // Vehicle configuration
        receipt.append("\nVEHICLE CONFIGURATION\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Model: ").append(vehicle.getModelName()).append("\n");
        receipt.append("Trim: ").append(vehicle.getTrimLevel().getDisplayName()).append("\n");
        receipt.append("Color: ").append(vehicle.getColor().getDisplayName()).append("\n");
        receipt.append("Drivetrain: ").append(vehicle.getDrivetrain()).append("\n");
        receipt.append("Range: ").append(vehicle.getRange()).append(" miles\n");
        receipt.append("Base Price: ").append(PriceCalculator.formatPrice(vehicle.calculatePrice())).append("\n");

        // Features if any were added
        if (!order.getFeatures().isEmpty()) {
            receipt.append("\nADDED FEATURES\n");
            receipt.append(LINE_SEPARATOR).append("\n");

            for (Feature feature : order.getFeatures()) {
                receipt.append(feature.getName()).append(": ");
                receipt.append(PriceCalculator.formatPrice(feature.getPrice())).append("\n");
            }
        }

        // Price summary
        receipt.append("\nSUMMARY\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Subtotal: ").append(PriceCalculator.formatPrice(order.calculateSubtotal())).append("\n");
        receipt.append("Tax (8.5%): ").append(PriceCalculator.formatPrice(order.calculateTax())).append("\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("TOTAL: ").append(PriceCalculator.formatPrice(order.calculateTotal())).append("\n");

        // Payment estimate
        double monthlyPayment = order.calculateMonthlyPayment();
        receipt.append("\nESTIMATED MONTHLY PAYMENT: ").append(PriceCalculator.formatPrice(monthlyPayment)).append("\n");
        receipt.append("(Based on: 60 months, $10,000 down, 5.9% APR)\n");

        // Vehicle specifications
        receipt.append("\nVEHICLE SPECIFICATIONS\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Power: ").append(vehicle.getPower()).append(" hp\n");
        receipt.append("0-60 mph: ").append(vehicle.getAcceleration()).append(" seconds\n");
        receipt.append("Top Speed: ").append(vehicle.getTopSpeed()).append(" mph\n");
        receipt.append("Range: ").append(vehicle.getRange()).append(" miles\n");
        receipt.append("Battery: ").append(vehicle.getBatteryCapacity()).append(" kWh\n");

        // Environmental impact
        receipt.append("\nENVIRONMENTAL IMPACT\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        double co2Saved = EnvironmentalCalculator.calculateCO2Saved(vehicle);
        int treesEquivalent = EnvironmentalCalculator.calculateTreesEquivalent(co2Saved);
        receipt.append("Estimated CO2 Saved (5 years): ").append(String.format("%.0f", co2Saved)).append(" tons\n");
        receipt.append("Equivalent to planting: ").append(treesEquivalent).append(" trees\n");

        // Footer
        receipt.append("\nThank you for choosing Raion Motors!\n");
        receipt.append("www.raionmotors.com | support@raionmotors.com\n");
        receipt.append(LINE_SEPARATOR).append("\n");

        return receipt.toString();
    }

    // Save receipt to a file in the receipts folder
    public static String saveReceiptToFile(Order order) throws IOException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // Create receipts folder if it doesn't exist
        File receiptsDir = new File(RECEIPTS_FOLDER);
        if (!receiptsDir.exists()) {
            receiptsDir.mkdirs();
        }

        // Generate filename from order ID
        String filename = order.getOrderId() + ".txt";
        String filepath = RECEIPTS_FOLDER + File.separator + filename;

        // Generate receipt content
        String receiptContent = generateReceipt(order);

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(receiptContent);
        }

        return filepath;
    }

    // Generate receipt and save it, returning the filepath
    public static String generateAndSaveReceipt(Order order) {
        try {
            return saveReceiptToFile(order);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save receipt: " + e.getMessage(), e);
        }
    }

    // Check if receipts folder exists
    public static boolean receiptsFolderExists() {
        File receiptsDir = new File(RECEIPTS_FOLDER);
        return receiptsDir.exists() && receiptsDir.isDirectory();
    }

    // Get the receipts folder path
    public static String getReceiptsFolderPath() {
        return new File(RECEIPTS_FOLDER).getAbsolutePath();
    }
}
