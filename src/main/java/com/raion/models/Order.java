package com.raion.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// represents a customer order with vehicle and optional features
// this class uses composition - it has-a vehicle and has-many features
public class Order {

    private final String orderId;
    private final Vehicle vehicle;
    private final List<Feature> features;
    private final LocalDateTime orderDate;

    private static final double TAX_RATE = 0.085; // 8.5% sales tax

    // create a new order with a vehicle
    public Order(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }

        this.vehicle = vehicle;
        this.features = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.orderId = generateOrderId();
    }

    // generate order id from timestamp: yyyymmdd-hhmmss
    private String generateOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return orderDate.format(formatter);
    }

    // add a feature to the order (option, service package, or accessory)
    public void addFeature(Feature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("feature cannot be null");
        }

        // check if feature is eligible for this vehicle level
        if (!feature.isEligibleFor(vehicle.getLevel())) {
            throw new IllegalArgumentException(
                    feature.getName() + " is not available for " + vehicle.getModelName()
            );
        }

        features.add(feature);
    }

    // remove a feature from the order
    public void removeFeature(Feature feature) {
        features.remove(feature);
    }

    // get all features in the order
    public List<Feature> getFeatures() {
        return new ArrayList<>(features); // return copy to protect internal list
    }

    // calculate total price of all features
    public double calculateFeaturesTotal() {
        double total = 0;
        for (Feature feature : features) {
            total += feature.getPrice();
        }
        return total;
    }

    // calculate subtotal (vehicle + features, before tax)
    public double calculateSubtotal() {
        return vehicle.calculatePrice() + calculateFeaturesTotal();
    }

    // calculate tax on subtotal
    public double calculateTax() {
        return calculateSubtotal() * TAX_RATE;
    }

    // calculate final total (subtotal + tax)
    public double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }

    // calculate estimated monthly payment
    // default: 60 months, $10,000 down, 5.9% apr
    public double calculateMonthlyPayment() {
        return calculateMonthlyPayment(60, 10000, 5.9);
    }

    // calculate monthly payment with custom terms
    public double calculateMonthlyPayment(int months, double downPayment, double aprPercent) {
        if (months <= 0) {
            throw new IllegalArgumentException("months must be positive");
        }
        if (downPayment < 0) {
            throw new IllegalArgumentException("down payment cannot be negative");
        }
        if (aprPercent < 0) {
            throw new IllegalArgumentException("apr cannot be negative");
        }

        double total = calculateTotal();
        double loanAmount = total - downPayment;

        // if loan amount is zero or negative, no monthly payment
        if (loanAmount <= 0) {
            return 0;
        }

        // convert apr to monthly rate
        double monthlyRate = (aprPercent / 100) / 12;

        // standard loan payment formula: p * [r(1+r)^n] / [(1+r)^n - 1]
        if (monthlyRate == 0) {
            // if 0% interest, just divide by months
            return loanAmount / months;
        }

        double monthlyPayment = loanAmount *
                (monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);

        return monthlyPayment;
    }

    // getters
    public String getOrderId() {
        return orderId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    // format order date for display
    public String getFormattedOrderDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss");
        return orderDate.format(formatter);
    }

    // generate a summary of the order
    public String getOrderSummary() {
        StringBuilder summary = new StringBuilder();

        summary.append("Order ID: ").append(orderId).append("\n");
        summary.append("Date: ").append(getFormattedOrderDate()).append("\n\n");

        summary.append("Vehicle: ").append(vehicle.toString()).append("\n");
        summary.append("Base Price: $").append(String.format("%,.2f", vehicle.calculatePrice())).append("\n\n");

        if (!features.isEmpty()) {
            summary.append("Added Features:\n");
            for (Feature feature : features) {
                summary.append("  - ").append(feature.getName());
                summary.append(": $").append(String.format("%,.2f", feature.getPrice())).append("\n");
            }
            summary.append("\n");
        }

        summary.append("Subtotal: $").append(String.format("%,.2f", calculateSubtotal())).append("\n");
        summary.append("Tax (8.5%): $").append(String.format("%,.2f", calculateTax())).append("\n");
        summary.append("Total: $").append(String.format("%,.2f", calculateTotal())).append("\n");

        return summary.toString();
    }

    @Override
    public String toString() {
        return "Order " + orderId + " - " + vehicle.getModelName() + " - $" +
                String.format("%,.2f", calculateTotal());
    }
}