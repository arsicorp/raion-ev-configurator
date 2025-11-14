package com.raion.services;

import com.raion.models.Feature;
import com.raion.models.Order;
import com.raion.models.Vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * generates professional receipts for vehicle orders
 * saves receipts to text files in the receipts folder
 *
 * receipt folder location: ./receipts (relative to application root)
 */
public class ReceiptGenerator {

    // receipts folder path (relative to where spring boot runs)
    private static final String RECEIPTS_FOLDER = "receipts";
    private static final String LINE_SEPARATOR = "=".repeat(60);

    /**
     * generate receipt text from an order
     *
     * @param order the order to generate a receipt for
     * @return formatted receipt text
     * @throws IllegalArgumentException if order is null
     */
    public static String generateReceipt(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        StringBuilder receipt = new StringBuilder();
        Vehicle vehicle = order.getVehicle();

        // header
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("RAION MOTORS\n");
        receipt.append("Electric Vehicle Order Receipt\n");
        receipt.append(LINE_SEPARATOR).append("\n");

        // order info
        receipt.append("Order ID: ").append(order.getOrderId()).append("\n");
        receipt.append("Date: ").append(order.getFormattedOrderDate()).append("\n");

        // vehicle configuration
        receipt.append("\nVEHICLE CONFIGURATION\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Model: ").append(vehicle.getModelName()).append("\n");
        receipt.append("Trim: ").append(vehicle.getTrimLevel().getDisplayName()).append("\n");
        receipt.append("Color: ").append(vehicle.getColor().getDisplayName()).append("\n");
        receipt.append("Drivetrain: ").append(vehicle.getDrivetrain()).append("\n");
        receipt.append("Range: ").append(vehicle.getRange()).append(" miles\n");
        receipt.append("Base Price: ").append(PriceCalculator.formatPrice(vehicle.calculatePrice())).append("\n");

        // features if any were added
        if (!order.getFeatures().isEmpty()) {
            receipt.append("\nADDED FEATURES\n");
            receipt.append(LINE_SEPARATOR).append("\n");

            for (Feature feature : order.getFeatures()) {
                receipt.append(feature.getName()).append(": ");
                receipt.append(PriceCalculator.formatPrice(feature.getPrice())).append("\n");
            }
        }

        // price summary
        receipt.append("\nSUMMARY\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Subtotal: ").append(PriceCalculator.formatPrice(order.calculateSubtotal())).append("\n");
        receipt.append("Tax (").append(PriceCalculator.getTaxRatePercentage()).append("%): ");
        receipt.append(PriceCalculator.formatPrice(order.calculateTax())).append("\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("TOTAL: ").append(PriceCalculator.formatPrice(order.calculateTotal())).append("\n");

        // payment estimate
        double monthlyPayment = order.calculateMonthlyPayment();
        receipt.append("\nESTIMATED MONTHLY PAYMENT: ").append(PriceCalculator.formatPrice(monthlyPayment)).append("\n");
        receipt.append("(Based on: 60 months, $10,000 down, 5.9% APR)\n");

        // vehicle specifications
        receipt.append("\nVEHICLE SPECIFICATIONS\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        receipt.append("Power: ").append(vehicle.getPower()).append(" hp\n");
        receipt.append("0-60 mph: ").append(vehicle.getAcceleration()).append(" seconds\n");
        receipt.append("Top Speed: ").append(vehicle.getTopSpeed()).append(" mph\n");
        receipt.append("Range: ").append(vehicle.getRange()).append(" miles\n");
        receipt.append("Battery: ").append(vehicle.getBatteryCapacity()).append(" kWh\n");

        // environmental impact
        receipt.append("\nENVIRONMENTAL IMPACT\n");
        receipt.append(LINE_SEPARATOR).append("\n");
        double co2Saved = EnvironmentalCalculator.calculateCO2Saved(vehicle);
        int treesEquivalent = EnvironmentalCalculator.calculateTreesEquivalent(co2Saved);
        receipt.append("Estimated CO2 Saved (5 years): ").append(String.format("%.0f", co2Saved)).append(" tons\n");
        receipt.append("Equivalent to planting: ").append(treesEquivalent).append(" trees\n");

        // footer
        receipt.append("\nThank you for choosing Raion Motors!\n");
        receipt.append("www.raionmotors.com | support@raionmotors.com\n");
        receipt.append(LINE_SEPARATOR).append("\n");

        return receipt.toString();
    }

    /**
     * create receipts directory if it doesn't exist
     *
     * @return path to receipts directory
     * @throws IOException if directory cannot be created
     */
    private static Path ensureReceiptsFolderExists() throws IOException {
        Path receiptsPath = Paths.get(RECEIPTS_FOLDER);

        if (!Files.exists(receiptsPath)) {
            try {
                Files.createDirectories(receiptsPath);
                System.out.println("created receipts folder: " + receiptsPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("failed to create receipts folder: " + e.getMessage());
                throw new IOException("could not create receipts directory: " + receiptsPath.toAbsolutePath(), e);
            }
        }

        // verify it's actually a directory
        if (!Files.isDirectory(receiptsPath)) {
            throw new IOException("receipts path exists but is not a directory: " + receiptsPath.toAbsolutePath());
        }

        // verify we can write to it
        if (!Files.isWritable(receiptsPath)) {
            throw new IOException("receipts directory is not writable: " + receiptsPath.toAbsolutePath());
        }

        return receiptsPath;
    }

    /**
     * save receipt to a file in the receipts folder
     *
     * @param order the order to save a receipt for
     * @return path to the saved receipt file
     * @throws IOException if file cannot be saved
     * @throws IllegalArgumentException if order is null
     */
    public static String saveReceiptToFile(Order order) throws IOException {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        // ensure receipts folder exists
        Path receiptsPath = ensureReceiptsFolderExists();

        // generate filename from order id
        String filename = order.getOrderId() + ".txt";
        Path filepath = receiptsPath.resolve(filename);

        // generate receipt content
        String receiptContent = generateReceipt(order);

        // write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath.toFile()))) {
            writer.write(receiptContent);
            writer.flush();
        } catch (IOException e) {
            System.err.println("failed to write receipt file: " + filepath.toAbsolutePath());
            throw new IOException("could not write receipt file: " + filepath.toAbsolutePath(), e);
        }

        // verify the file was created
        if (!Files.exists(filepath)) {
            throw new IOException("receipt file was not created: " + filepath.toAbsolutePath());
        }

        return filepath.toString();
    }

    /**
     * generate receipt and save it, returning the filepath
     * this is the main entry point for saving receipts
     *
     * @param order the order to save a receipt for
     * @return path to the saved receipt file
     * @throws RuntimeException if receipt cannot be saved
     */
    public static String generateAndSaveReceipt(Order order) {
        try {
            String filepath = saveReceiptToFile(order);
            System.out.println("receipt saved successfully: " + filepath);
            return filepath;
        } catch (IOException e) {
            String errorMsg = "failed to save receipt: " + e.getMessage();
            System.err.println(errorMsg);
            System.err.println("  working directory: " + System.getProperty("user.dir"));
            System.err.println("  receipts folder: " + getReceiptsFolderPath());
            throw new RuntimeException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "unexpected error while saving receipt: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * check if receipts folder exists and is writable
     *
     * @return true if receipts folder exists and is writable
     */
    public static boolean receiptsFolderExists() {
        try {
            Path receiptsPath = Paths.get(RECEIPTS_FOLDER);
            return Files.exists(receiptsPath) &&
                    Files.isDirectory(receiptsPath) &&
                    Files.isWritable(receiptsPath);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * get the absolute path to the receipts folder
     *
     * @return absolute path to receipts folder
     */
    public static String getReceiptsFolderPath() {
        try {
            return Paths.get(RECEIPTS_FOLDER).toAbsolutePath().toString();
        } catch (Exception e) {
            return RECEIPTS_FOLDER;
        }
    }

    /**
     * get the number of receipts currently saved
     *
     * @return number of receipt files in the receipts folder
     */
    public static int getReceiptCount() {
        try {
            Path receiptsPath = Paths.get(RECEIPTS_FOLDER);
            if (!Files.exists(receiptsPath)) {
                return 0;
            }

            return (int) Files.list(receiptsPath)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * print diagnostic information about the receipts folder
     * useful for debugging receipt saving issues
     */
    public static void printReceiptsFolderInfo() {
        System.out.println("\n========== RECEIPTS FOLDER INFO ==========");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Receipts Folder: " + getReceiptsFolderPath());
        System.out.println("Folder Exists: " + receiptsFolderExists());
        System.out.println("Receipt Count: " + getReceiptCount());
        System.out.println("==========================================\n");
    }
}