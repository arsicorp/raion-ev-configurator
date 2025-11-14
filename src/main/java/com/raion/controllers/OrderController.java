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

/**
 * rest api controller for order management
 * handles placing custom orders and signature vehicle orders
 *
 * endpoints:
 * - post /api/order - place a custom vehicle order
 * - post /api/order/signature - place a signature vehicle order
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    /**
     * post /api/order - place a custom vehicle order
     *
     * request body format from frontend:
     * {
     *   "level": 1,  // frontend sends level as number, not "model" string
     *   "trim": "Standard",  // trim name as display string
     *   "color": "white",  // color as lowercase string
     *   "options": ["enhanced-autopilot", "fsd", ...],  // option ids
     *   "servicePackages": [...],
     *   "accessories": ["floor-mats", "home-charger", ...]  // accessory ids
     * }
     */
    @PostMapping("/order")
    public ResponseEntity<?> placeCustomOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            // validate required fields - frontend sends 'level' not 'model'
            if (!orderRequest.containsKey("level") || !orderRequest.containsKey("trim") || !orderRequest.containsKey("color")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "missing required fields: level, trim, color")
                );
            }

            // extract level as integer (frontend sends it as number)
            int level;
            Object levelObj = orderRequest.get("level");
            if (levelObj instanceof Integer) {
                level = (Integer) levelObj;
            } else if (levelObj instanceof String) {
                try {
                    level = Integer.parseInt((String) levelObj);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "invalid level format")
                    );
                }
            } else {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid level format")
                );
            }

            if (level < 1 || level > 4) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid level. must be 1, 2, 3, or 4")
                );
            }

            // extract trim and color strings
            String trimStr = ((String) orderRequest.get("trim")).trim();
            String colorStr = ((String) orderRequest.get("color")).trim();

            // convert trim name to enum (handle display names like "Off-Road" -> OFFROAD)
            TrimLevel trim = parseTrimLevel(trimStr);
            if (trim == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid trim: " + trimStr)
                );
            }

            // convert color name to enum (handle lowercase like "white" -> WHITE)
            VehicleColor color = parseColor(colorStr);
            if (color == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid color: " + colorStr)
                );
            }

            // create the vehicle based on level
            Vehicle vehicle = createVehicle(level, trim, color);

            if (vehicle == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid trim/color combination for this vehicle level")
                );
            }

            // create order
            Order order = new Order(vehicle);

            // add options if provided (frontend sends option IDs)
            if (orderRequest.containsKey("options")) {
                @SuppressWarnings("unchecked")
                List<String> optionIds = (List<String>) orderRequest.get("options");
                for (String optionId : optionIds) {
                    if (optionId != null && !optionId.trim().isEmpty()) {
                        Option option = createOptionById(optionId, level);
                        if (option != null) {
                            order.addFeature(option);
                        }
                    }
                }
            }

            // add service packages if provided
            if (orderRequest.containsKey("servicePackages")) {
                @SuppressWarnings("unchecked")
                List<String> packageIds = (List<String>) orderRequest.get("servicePackages");
                for (String packageId : packageIds) {
                    if (packageId != null && !packageId.trim().isEmpty()) {
                        ServicePackage servicePackage = createServicePackageById(packageId);
                        if (servicePackage != null) {
                            order.addFeature(servicePackage);
                        }
                    }
                }
            }

            // add accessories if provided (frontend sends accessory IDs)
            if (orderRequest.containsKey("accessories")) {
                @SuppressWarnings("unchecked")
                List<String> accessoryIds = (List<String>) orderRequest.get("accessories");
                for (String accessoryId : accessoryIds) {
                    if (accessoryId != null && !accessoryId.trim().isEmpty()) {
                        Accessory accessory = createAccessoryById(accessoryId);
                        if (accessory != null) {
                            order.addFeature(accessory);
                        }
                    }
                }
            }

            // generate and save receipt
            String receiptPath = ReceiptGenerator.generateAndSaveReceipt(order);

            // build response
            Map<String, Object> response = buildOrderResponse(order, receiptPath, false, null);

            System.out.println("✓ order placed successfully: " + order.getOrderId());
            System.out.println("  receipt saved to: " + receiptPath);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("✗ order validation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("error", "invalid order data: " + e.getMessage())
            );
        } catch (Exception e) {
            System.err.println("✗ order processing failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "error", "failed to process order",
                            "details", e.getMessage(),
                            "type", e.getClass().getSimpleName()
                    )
            );
        }
    }

    /**
     * post /api/order/signature - place a signature vehicle order
     *
     * request body format:
     * {
     *   "signatureName": "urban-commuter" | "trail-titan" | "track-beast" | "executive",
     *   "additionalOptions": [...],
     *   "accessories": [...]
     * }
     */
    @PostMapping("/order/signature")
    public ResponseEntity<?> placeSignatureOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            // validate request
            if (!orderRequest.containsKey("signatureName")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "missing required field: signatureName")
                );
            }

            String signatureName = ((String) orderRequest.get("signatureName")).toLowerCase().trim();

            // create the signature vehicle
            Vehicle vehicle = createSignatureVehicle(signatureName);

            if (vehicle == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid signature name. must be: urban-commuter, trail-titan, track-beast, or executive")
                );
            }

            // create order
            Order order = new Order(vehicle);

            // add additional options if provided (customer can still customize signature)
            if (orderRequest.containsKey("additionalOptions")) {
                @SuppressWarnings("unchecked")
                List<String> optionIds = (List<String>) orderRequest.get("additionalOptions");
                for (String optionId : optionIds) {
                    if (optionId != null && !optionId.trim().isEmpty()) {
                        Option option = createOptionById(optionId, vehicle.getLevel());
                        if (option != null) {
                            order.addFeature(option);
                        }
                    }
                }
            }

            // add accessories if provided
            if (orderRequest.containsKey("accessories")) {
                @SuppressWarnings("unchecked")
                List<String> accessoryIds = (List<String>) orderRequest.get("accessories");
                for (String accessoryId : accessoryIds) {
                    if (accessoryId != null && !accessoryId.trim().isEmpty()) {
                        Accessory accessory = createAccessoryById(accessoryId);
                        if (accessory != null) {
                            order.addFeature(accessory);
                        }
                    }
                }
            }

            // generate and save receipt
            String receiptPath = ReceiptGenerator.generateAndSaveReceipt(order);

            // build response with signature info
            Map<String, Object> response = buildOrderResponse(order, receiptPath, true, signatureName);

            System.out.println("✓ signature order placed successfully: " + order.getOrderId());
            System.out.println("  signature: " + formatSignatureName(signatureName));
            System.out.println("  receipt saved to: " + receiptPath);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("✗ signature order validation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("error", "invalid signature order data: " + e.getMessage())
            );
        } catch (Exception e) {
            System.err.println("✗ signature order processing failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "error", "failed to process signature order",
                            "details", e.getMessage(),
                            "type", e.getClass().getSimpleName()
                    )
            );
        }
    }

    /**
     * helper: parse trim level from display name
     * handles names like "Standard", "Off-Road", etc.
     */
    private TrimLevel parseTrimLevel(String trimStr) {
        if (trimStr == null || trimStr.isEmpty()) {
            return null;
        }

        // normalize: remove spaces, hyphens, convert to uppercase
        String normalized = trimStr.toUpperCase()
                .replace("-", "")
                .replace(" ", "")
                .trim();

        try {
            return TrimLevel.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            // if direct match fails, try some common variations
            switch (normalized) {
                case "OFFROAD":
                    return TrimLevel.OFFROAD;
                default:
                    return null;
            }
        }
    }

    /**
     * helper: parse color from name (handles lowercase like "white" -> WHITE)
     */
    private VehicleColor parseColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) {
            return null;
        }

        try {
            return VehicleColor.valueOf(colorStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * helper: create vehicle based on level, trim, and color
     */
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
            System.err.println("failed to create vehicle: " + e.getMessage());
            return null;
        }
    }

    /**
     * helper: create signature vehicle by name
     */
    private Vehicle createSignatureVehicle(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

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

    /**
     * helper: create option by id (matches frontend ids like "enhanced-autopilot")
     */
    private Option createOptionById(String id, int level) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        id = id.toLowerCase().trim();

        switch (id) {
            case "enhanced-autopilot":
                return Option.createEnhancedAutopilot();
            case "full-self-driving":
            case "fsd":
                return Option.createFullSelfDriving();
            case "massage-seats":
                return (level == 4) ? Option.createMassageSeatsLevel4() : Option.createMassageSeats();
            case "custom-paint":
                return Option.createCustomPaint();
            case "track-package":
                return Option.createTrackPackage();
            default:
                System.err.println("unknown option id: " + id);
                return null;
        }
    }

    /**
     * helper: create service package by id
     */
    private ServicePackage createServicePackageById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        id = id.toLowerCase().trim();

        switch (id) {
            case "warranty-8yr":
            case "extended-warranty":
                return ServicePackage.createExtendedWarranty8Year();
            case "maintenance-5yr":
            case "premium-maintenance":
                return ServicePackage.createPremiumMaintenance5Year();
            case "roadside-assistance":
                return ServicePackage.createPremiumRoadsideAssistance();
            default:
                System.err.println("unknown service package id: " + id);
                return null;
        }
    }

    /**
     * helper: create accessory by id (matches frontend ids like "floor-mats")
     */
    private Accessory createAccessoryById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        id = id.toLowerCase().trim();

        switch (id) {
            case "floor-mats":
                return Accessory.createPremiumFloorMats();
            case "home-charger":
                return Accessory.createHomeCharger();
            case "paint-protection":
                return Accessory.createPaintProtectionFilm();
            case "ceramic-coating":
                return Accessory.createCeramicCoating();
            default:
                System.err.println("unknown accessory id: " + id);
                return null;
        }
    }

    /**
     * helper: build comprehensive order response
     */
    private Map<String, Object> buildOrderResponse(Order order, String receiptPath, boolean isSignature, String signatureName) {
        Map<String, Object> response = new HashMap<>();

        Vehicle vehicle = order.getVehicle();

        // order details
        response.put("orderId", order.getOrderId());
        response.put("orderDate", order.getFormattedOrderDate());
        response.put("receiptFile", receiptPath);
        response.put("isSignature", isSignature);

        if (isSignature && signatureName != null) {
            response.put("signatureName", formatSignatureName(signatureName));
        }

        // vehicle info
        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("model", vehicle.getModelName());
        vehicleInfo.put("trim", vehicle.getTrimLevel().getDisplayName());
        vehicleInfo.put("color", vehicle.getColor().getDisplayName());
        vehicleInfo.put("level", vehicle.getLevel());
        response.put("vehicle", vehicleInfo);

        // pricing breakdown
        Map<String, Object> pricing = new HashMap<>();
        pricing.put("basePrice", vehicle.calculatePrice());
        pricing.put("featuresTotal", order.calculateFeaturesTotal());
        pricing.put("subtotal", order.calculateSubtotal());
        pricing.put("tax", order.calculateTax());
        pricing.put("taxRate", PriceCalculator.getTaxRatePercentage());
        pricing.put("total", order.calculateTotal());
        response.put("pricing", pricing);

        // payment estimate
        Map<String, Object> payment = new HashMap<>();
        payment.put("monthlyPayment", Math.round(order.calculateMonthlyPayment() * 100.0) / 100.0);
        payment.put("loanTerm", 60);
        payment.put("downPayment", 10000);
        payment.put("apr", 5.9);
        response.put("paymentEstimate", payment);

        // vehicle specifications
        Map<String, Object> specs = new HashMap<>();
        specs.put("power", vehicle.getPower());
        specs.put("acceleration", vehicle.getAcceleration());
        specs.put("topSpeed", vehicle.getTopSpeed());
        specs.put("range", vehicle.getRange());
        specs.put("battery", vehicle.getBatteryCapacity());
        specs.put("drivetrain", vehicle.getDrivetrain());
        response.put("specifications", specs);

        // charging info
        Map<String, Object> charging = new HashMap<>();
        charging.put("homeChargingTime", Math.round(vehicle.getHomeChargingTime() * 10.0) / 10.0);
        charging.put("fastChargingTime", Math.round(vehicle.getFastChargingTime()));
        response.put("charging", charging);

        // environmental impact
        Map<String, Object> environmental = new HashMap<>();
        double co2Saved = EnvironmentalCalculator.calculateCO2Saved(vehicle);
        int treesEquivalent = EnvironmentalCalculator.calculateTreesEquivalent(co2Saved);
        environmental.put("co2Saved", Math.round(co2Saved));
        environmental.put("treesEquivalent", treesEquivalent);
        response.put("environmental", environmental);

        // added features list
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

    /**
     * helper: format signature name for display
     */
    private String formatSignatureName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

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