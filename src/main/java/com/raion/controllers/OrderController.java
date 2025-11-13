package com.raion.controllers;

import com.raion.models.*;
import com.raion.models.signatures.*;
import com.raion.services.EnvironmentalCalculator;
import com.raion.services.PriceCalculator;
import com.raion.services.ReceiptGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// REST API controller for order management
// Handles placing custom orders and signature vehicle orders
@RestController
@RequestMapping("/api/order")
public class OrderController {

    // POST /api/order - Place a custom vehicle order
    @PostMapping
    public ResponseEntity<?> placeCustomOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            // Validate request
            if (!orderRequest.containsKey("level") || !orderRequest.containsKey("trim") || !orderRequest.containsKey("color")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Missing required fields: level, trim, color")
                );
            }

            // Extract vehicle configuration
            int level = ((Number) orderRequest.get("level")).intValue();
            String trimStr = (String) orderRequest.get("trim");
            String colorStr = (String) orderRequest.get("color");

            // Validate level
            if (level < 1 || level > 4) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid level. Must be 1, 2, 3, or 4")
                );
            }

            // Parse trim and color enums
            TrimLevel trim;
            VehicleColor color;

            try {
                trim = TrimLevel.valueOf(trimStr.toUpperCase());
                color = VehicleColor.valueOf(colorStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid trim or color value")
                );
            }

            // Create the vehicle based on level
            Vehicle vehicle = createVehicle(level, trim, color);

            if (vehicle == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid trim/color combination for this vehicle level")
                );
            }

            // Create order
            Order order = new Order(vehicle);

            // Add options if provided
            if (orderRequest.containsKey("options")) {
                List<String> options = (List<String>) orderRequest.get("options");
                for (String optionName : options) {
                    Option option = createOptionByName(optionName, level);
                    if (option != null) {
                        order.addFeature(option);
                    }
                }
            }

            // Add service packages if provided
            if (orderRequest.containsKey("servicePackages")) {
                List<String> packages = (List<String>) orderRequest.get("servicePackages");
                for (String packageName : packages) {
                    ServicePackage servicePackage = createServicePackageByName(packageName);
                    if (servicePackage != null) {
                        order.addFeature(servicePackage);
                    }
                }
            }

            // Add accessories if provided
            if (orderRequest.containsKey("accessories")) {
                List<String> accessories = (List<String>) orderRequest.get("accessories");
                for (String accessoryName : accessories) {
                    Accessory accessory = createAccessoryByName(accessoryName);
                    if (accessory != null) {
                        order.addFeature(accessory);
                    }
                }
            }

            // Generate receipt
            String receiptPath = ReceiptGenerator.generateAndSaveReceipt(order);

            // Build response
            Map<String, Object> response = buildOrderResponse(order, receiptPath, false, null);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Failed to process order: " + e.getMessage())
            );
        }
    }

    // POST /api/order/signature - Place a signature vehicle order
    @PostMapping("/signature")
    public ResponseEntity<?> placeSignatureOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            // Validate request
            if (!orderRequest.containsKey("signatureName")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Missing required field: signatureName")
                );
            }

            String signatureName = ((String) orderRequest.get("signatureName")).toLowerCase().trim();

            // Create the signature vehicle
            Vehicle vehicle = createSignatureVehicle(signatureName);

            if (vehicle == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid signature name. Must be: urban-commuter, trail-titan, track-beast, or executive")
                );
            }

            // Create order
            Order order = new Order(vehicle);

            // Add additional options if provided (customer can still customize signature)
            if (orderRequest.containsKey("additionalOptions")) {
                List<String> options = (List<String>) orderRequest.get("additionalOptions");
                for (String optionName : options) {
                    Option option = createOptionByName(optionName, vehicle.getLevel());
                    if (option != null) {
                        order.addFeature(option);
                    }
                }
            }

            // Add accessories if provided
            if (orderRequest.containsKey("accessories")) {
                List<String> accessories = (List<String>) orderRequest.get("accessories");
                for (String accessoryName : accessories) {
                    Accessory accessory = createAccessoryByName(accessoryName);
                    if (accessory != null) {
                        order.addFeature(accessory);
                    }
                }
            }

            // Generate receipt
            String receiptPath = ReceiptGenerator.generateAndSaveReceipt(order);

            // Build response with signature info
            Map<String, Object> response = buildOrderResponse(order, receiptPath, true, signatureName);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Failed to process signature order: " + e.getMessage())
            );
        }
    }

    // Helper: Create vehicle based on level, trim, and color
    private Vehicle createVehicle(int level, TrimLevel trim, VehicleColor color) {
        try {
            switch (level) {
                case 1:
                    return new Level1(trim, color);
                case 2:
                    return new Level2(trim, color);
                case 3:
                    return new Level3(trim, color);
                case 4:
                    return new Level4(color);
                default:
                    return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Helper: Create signature vehicle by name
    private Vehicle createSignatureVehicle(String name) {
        switch (name) {
            case "urban-commuter":
                return new UrbanCommuterSignature();
            case "trail-titan":
                return new TrailTitanSignature();
            case "track-beast":
                return new TrackBeastSignature();
            case "executive":
                return new ExecutiveSignature();
            default:
                return null;
        }
    }

    // Helper: Create option by name
    private Option createOptionByName(String name, int level) {
        name = name.toLowerCase().trim();

        switch (name) {
            case "enhanced-autopilot":
            case "enhanced autopilot":
                return Option.createEnhancedAutopilot();
            case "full-self-driving":
            case "full self-driving":
            case "fsd":
                return Option.createFullSelfDriving();
            case "massage-seats":
            case "massage seats":
                return (level == 4) ? Option.createMassageSeatsLevel4() : Option.createMassageSeats();
            case "custom-paint":
            case "custom paint":
                return Option.createCustomPaint();
            case "track-package":
            case "track package":
                return Option.createTrackPackage();
            default:
                return null;
        }
    }

    // Helper: Create service package by name
    private ServicePackage createServicePackageByName(String name) {
        name = name.toLowerCase().trim();

        switch (name) {
            case "warranty-8yr":
            case "warranty 8yr":
            case "extended-warranty":
            case "extended warranty":
                return ServicePackage.createExtendedWarranty8Year();
            case "maintenance-5yr":
            case "maintenance 5yr":
            case "premium-maintenance":
            case "premium maintenance":
                return ServicePackage.createPremiumMaintenance5Year();
            case "roadside-assistance":
            case "roadside assistance":
                return ServicePackage.createPremiumRoadsideAssistance();
            default:
                return null;
        }
    }

    // Helper: Create accessory by name
    private Accessory createAccessoryByName(String name) {
        name = name.toLowerCase().trim();

        switch (name) {
            case "floor-mats":
            case "floor mats":
            case "mats":
                return Accessory.createPremiumFloorMats();
            case "home-charger":
            case "home charger":
            case "charger":
                return Accessory.createHomeCharger();
            case "paint-protection":
            case "paint protection":
            case "ppf":
                return Accessory.createPaintProtectionFilm();
            case "ceramic-coating":
            case "ceramic coating":
            case "ceramic":
                return Accessory.createCeramicCoating();
            default:
                return null;
        }
    }

    // Helper: Build comprehensive order response
    private Map<String, Object> buildOrderResponse(Order order, String receiptPath, boolean isSignature, String signatureName) {
        Map<String, Object> response = new HashMap<>();

        Vehicle vehicle = order.getVehicle();

        // Order details
        response.put("orderId", order.getOrderId());
        response.put("orderDate", order.getFormattedOrderDate());
        response.put("receiptFile", receiptPath);
        response.put("isSignature", isSignature);

        if (isSignature && signatureName != null) {
            response.put("signatureName", formatSignatureName(signatureName));
        }

        // Vehicle info
        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("model", vehicle.getModelName());
        vehicleInfo.put("trim", vehicle.getTrimLevel().getDisplayName());
        vehicleInfo.put("color", vehicle.getColor().getDisplayName());
        vehicleInfo.put("level", vehicle.getLevel());
        response.put("vehicle", vehicleInfo);

        // Pricing breakdown
        Map<String, Object> pricing = new HashMap<>();
        pricing.put("basePrice", vehicle.calculatePrice());
        pricing.put("featuresTotal", order.calculateFeaturesTotal());
        pricing.put("subtotal", order.calculateSubtotal());
        pricing.put("tax", order.calculateTax());
        pricing.put("taxRate", PriceCalculator.getTaxRatePercentage());
        pricing.put("total", order.calculateTotal());
        response.put("pricing", pricing);

        // Payment estimate
        Map<String, Object> payment = new HashMap<>();
        payment.put("monthlyPayment", Math.round(order.calculateMonthlyPayment() * 100.0) / 100.0);
        payment.put("loanTerm", 60);
        payment.put("downPayment", 10000);
        payment.put("apr", 5.9);
        response.put("paymentEstimate", payment);

        // Vehicle specifications
        Map<String, Object> specs = new HashMap<>();
        specs.put("power", vehicle.getPower());
        specs.put("acceleration", vehicle.getAcceleration());
        specs.put("topSpeed", vehicle.getTopSpeed());
        specs.put("range", vehicle.getRange());
        specs.put("battery", vehicle.getBatteryCapacity());
        specs.put("drivetrain", vehicle.getDrivetrain());
        response.put("specifications", specs);

        // Charging info
        Map<String, Object> charging = new HashMap<>();
        charging.put("homeChargingTime", Math.round(vehicle.getHomeChargingTime() * 10.0) / 10.0);
        charging.put("fastChargingTime", Math.round(vehicle.getFastChargingTime()));
        response.put("charging", charging);

        // Environmental impact
        Map<String, Object> environmental = new HashMap<>();
        double co2Saved = EnvironmentalCalculator.calculateCO2Saved(vehicle);
        int treesEquivalent = EnvironmentalCalculator.calculateTreesEquivalent(co2Saved);
        environmental.put("co2Saved", Math.round(co2Saved));
        environmental.put("treesEquivalent", treesEquivalent);
        response.put("environmental", environmental);

        // Added features list
        if (!order.getFeatures().isEmpty()) {
            List<Map<String, Object>> features = new ArrayList<>();
            for (Feature feature : order.getFeatures()) {
                Map<String, Object> featureInfo = new HashMap<>();
                featureInfo.put("name", feature.getName());
                featureInfo.put("price", feature.getPrice());
                featureInfo.put("category", feature.getCategory());
                features.add(featureInfo);
            }
            response.put("features", features);
        }

        return response;
    }

    // Helper: Format signature name for display
    private String formatSignatureName(String name) {
        switch (name) {
            case "urban-commuter":
                return "Urban Commuter";
            case "trail-titan":
                return "Trail Titan";
            case "track-beast":
                return "Track Beast";
            case "executive":
                return "Executive";
            default:
                return name;
        }
    }
}
