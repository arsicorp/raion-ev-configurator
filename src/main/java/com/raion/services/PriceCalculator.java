package com.raion.services;

import com.raion.models.Feature;
import com.raion.models.Order;
import com.raion.models.Vehicle;

import java.util.List;

// Handles all pricing calculations for vehicles and orders
// Strategy pattern - different calculation methods for different scenarios
public class PriceCalculator {

    private static final double TAX_RATE = 0.085; // 8.5% sales tax

    // Private constructor - this is a utility class with only static methods
    private PriceCalculator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Calculate the base vehicle price without any features
    public static double calculateBasePrice(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        return vehicle.calculatePrice();
    }

    // Calculate total price of features
    public static double calculateFeaturesTotal(List<Feature> features) {
        if (features == null) {
            return 0;
        }

        double total = 0;
        for (Feature feature : features) {
            total += feature.getPrice();
        }
        return total;
    }

    // Calculate subtotal (vehicle + features)
    public static double calculateSubtotal(Vehicle vehicle, List<Feature> features) {
        double vehiclePrice = calculateBasePrice(vehicle);
        double featuresPrice = calculateFeaturesTotal(features);
        return vehiclePrice + featuresPrice;
    }

    // Calculate tax on a given amount
    public static double calculateTax(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return amount * TAX_RATE;
    }

    // Calculate final total with tax
    public static double calculateTotal(Vehicle vehicle, List<Feature> features) {
        double subtotal = calculateSubtotal(vehicle, features);
        double tax = calculateTax(subtotal);
        return subtotal + tax;
    }

    // Calculate total from an Order object
    public static double calculateTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return order.calculateTotal();
    }

    // Calculate monthly payment with standard terms (60 months, $10k down, 5.9% APR)
    public static double calculateMonthlyPayment(double totalPrice) {
        return calculateMonthlyPayment(totalPrice, 60, 10000, 5.9);
    }

    // Calculate monthly payment with custom terms
    public static double calculateMonthlyPayment(double totalPrice, int months, double downPayment, double aprPercent) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        if (months <= 0) {
            throw new IllegalArgumentException("Months must be positive");
        }
        if (downPayment < 0) {
            throw new IllegalArgumentException("Down payment cannot be negative");
        }
        if (aprPercent < 0) {
            throw new IllegalArgumentException("APR cannot be negative");
        }

        double loanAmount = totalPrice - downPayment;

        // If fully paid or overpaid, no monthly payment needed
        if (loanAmount <= 0) {
            return 0;
        }

        // Convert APR to monthly rate
        double monthlyRate = (aprPercent / 100) / 12;

        // Handle 0% interest separately
        if (monthlyRate == 0) {
            return loanAmount / months;
        }

        // Standard amortization formula
        double monthlyPayment = loanAmount *
                (monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);

        return monthlyPayment;
    }

    // Calculate total interest paid over the loan term
    public static double calculateTotalInterest(double totalPrice, int months, double downPayment, double aprPercent) {
        double monthlyPayment = calculateMonthlyPayment(totalPrice, months, downPayment, aprPercent);
        double loanAmount = totalPrice - downPayment;

        if (loanAmount <= 0) {
            return 0;
        }

        double totalPaid = monthlyPayment * months;
        return totalPaid - loanAmount;
    }

    // Format price for display with currency symbol and commas
    public static String formatPrice(double price) {
        return "$" + String.format("%,.2f", price);
    }

    // Calculate what percentage down payment is of total price
    public static double calculateDownPaymentPercentage(double totalPrice, double downPayment) {
        if (totalPrice <= 0) {
            return 0;
        }
        return (downPayment / totalPrice) * 100;
    }

    // Suggest recommended down payment (20% of total)
    public static double suggestDownPayment(double totalPrice) {
        return totalPrice * 0.20;
    }

    // Get tax rate as percentage
    public static double getTaxRatePercentage() {
        return TAX_RATE * 100;
    }
}
