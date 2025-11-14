package com.raion.services;

import com.raion.models.Feature;
import com.raion.models.Order;
import com.raion.models.Vehicle;

import java.util.List;

// handles all pricing calculations for vehicles and orders
// strategy pattern - different calculation methods for different scenarios
public class PriceCalculator {

    private static final double TAX_RATE = 0.085; // 8.5% sales tax

    // private constructor - this is a utility class with only static methods
    private PriceCalculator() {
        throw new UnsupportedOperationException("utility class cannot be instantiated");
    }

    // calculate the base vehicle price without any features
    public static double calculateBasePrice(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }
        return vehicle.calculatePrice();
    }

    // calculate total price of features
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

    // calculate subtotal (vehicle + features)
    public static double calculateSubtotal(Vehicle vehicle, List<Feature> features) {
        double vehiclePrice = calculateBasePrice(vehicle);
        double featuresPrice = calculateFeaturesTotal(features);
        return vehiclePrice + featuresPrice;
    }

    // calculate tax on a given amount
    public static double calculateTax(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        return amount * TAX_RATE;
    }

    // calculate final total with tax
    public static double calculateTotal(Vehicle vehicle, List<Feature> features) {
        double subtotal = calculateSubtotal(vehicle, features);
        double tax = calculateTax(subtotal);
        return subtotal + tax;
    }

    // calculate total from an order object
    public static double calculateTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }
        return order.calculateTotal();
    }

    // calculate monthly payment with standard terms (60 months, $10k down, 5.9% apr)
    public static double calculateMonthlyPayment(double totalPrice) {
        return calculateMonthlyPayment(totalPrice, 60, 10000, 5.9);
    }

    // calculate monthly payment with custom terms
    public static double calculateMonthlyPayment(double totalPrice, int months, double downPayment, double aprPercent) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("total price cannot be negative");
        }
        if (months <= 0) {
            throw new IllegalArgumentException("months must be positive");
        }
        if (downPayment < 0) {
            throw new IllegalArgumentException("down payment cannot be negative");
        }
        if (aprPercent < 0) {
            throw new IllegalArgumentException("apr cannot be negative");
        }

        double loanAmount = totalPrice - downPayment;

        // if fully paid or overpaid, no monthly payment needed
        if (loanAmount <= 0) {
            return 0;
        }

        // convert apr to monthly rate
        double monthlyRate = (aprPercent / 100) / 12;

        // handle 0% interest separately
        if (monthlyRate == 0) {
            return loanAmount / months;
        }

        // standard amortization formula
        double monthlyPayment = loanAmount *
                (monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);

        return monthlyPayment;
    }

    // calculate total interest paid over the loan term
    public static double calculateTotalInterest(double totalPrice, int months, double downPayment, double aprPercent) {
        double monthlyPayment = calculateMonthlyPayment(totalPrice, months, downPayment, aprPercent);
        double loanAmount = totalPrice - downPayment;

        if (loanAmount <= 0) {
            return 0;
        }

        double totalPaid = monthlyPayment * months;
        return totalPaid - loanAmount;
    }

    // format price for display with currency symbol and commas
    public static String formatPrice(double price) {
        return "$" + String.format("%,.2f", price);
    }

    // calculate what percentage down payment is of total price
    public static double calculateDownPaymentPercentage(double totalPrice, double downPayment) {
        if (totalPrice <= 0) {
            return 0;
        }
        return (downPayment / totalPrice) * 100;
    }

    // suggest recommended down payment (20% of total)
    public static double suggestDownPayment(double totalPrice) {
        return totalPrice * 0.20;
    }

    // get tax rate as percentage
    public static double getTaxRatePercentage() {
        return TAX_RATE * 100;
    }
}