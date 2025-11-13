package com.raion.controllers;

import com.raion.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// REST API controller for vehicle information
// Handles all endpoints related to vehicles, trims, colors, and specs
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    // GET /api/vehicles - Get information about all 4 vehicle models
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicles() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> vehicles = new ArrayList<>();

        // Level 1 info
        Map<String, Object> level1 = new HashMap<>();
        level1.put("level", 1);
        level1.put("name", "Raion Level 1");
        level1.put("bodyStyle", "Compact Sedan");
        level1.put("trims", Arrays.asList("STANDARD", "PREMIUM", "PERFORMANCE"));
        level1.put("colors", Arrays.asList("WHITE", "BLACK", "SILVER", "BLUE"));
        level1.put("priceRange", Map.of("min", 45000, "max", 55000));
        level1.put("range", 400);
        vehicles.add(level1);

        // Level 2 info
        Map<String, Object> level2 = new HashMap<>();
        level2.put("level", 2);
        level2.put("name", "Raion Level 2");
        level2.put("bodyStyle", "Full-Size SUV");
        level2.put("trims", Arrays.asList("STANDARD", "PREMIUM", "OFFROAD"));
        level2.put("colors", Arrays.asList("WHITE", "BLACK", "SILVER", "BLUE"));
        level2.put("priceRange", Map.of("min", 85000, "max", 95000));
        level2.put("range", 450);
        level2.put("seating", 7);
        vehicles.add(level2);

        // Level 3 info
        Map<String, Object> level3 = new HashMap<>();
        level3.put("level", 3);
        level3.put("name", "Raion Level 3");
        level3.put("bodyStyle", "Performance Sedan");
        level3.put("trims", Arrays.asList("PRO", "MAX", "ULTRA"));
        level3.put("colors", Arrays.asList("PURPLE", "BURGUNDY", "GREEN"));
        level3.put("priceRange", Map.of("min", 125000, "max", 135000));
        level3.put("range", 350);
        vehicles.add(level3);

        // Level 4 info
        Map<String, Object> level4 = new HashMap<>();
        level4.put("level", 4);
        level4.put("name", "Raion Level 4");
        level4.put("bodyStyle", "Ultra-Luxury SUV");
        level4.put("trims", Arrays.asList("FLAGSHIP"));
        level4.put("colors", Arrays.asList("BLACK"));
        level4.put("priceRange", Map.of("min", 185000, "max", 185000));
        level4.put("range", 620);
        level4.put("seating", 4);
        vehicles.add(level4);

        response.put("vehicles", vehicles);
        return ResponseEntity.ok(response);
    }

    // GET /api/vehicles/{level} - Get details about a specific vehicle level
    @GetMapping("/{level}")
    public ResponseEntity<Map<String, Object>> getVehicleByLevel(@PathVariable int level) {
        if (level < 1 || level > 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid level. Must be 1, 2, 3, or 4"));
        }

        Map<String, Object> vehicleInfo = new HashMap<>();
        vehicleInfo.put("level", level);

        switch (level) {
            case 1:
                vehicleInfo.put("name", "Raion Level 1");
                vehicleInfo.put("bodyStyle", "Compact Sedan");
                vehicleInfo.put("description", "Compact sedan inspired by Tesla Model 3");
                vehicleInfo.put("drivetrain", "RWD");
                vehicleInfo.put("battery", 80);
                vehicleInfo.put("range", 400);
                break;
            case 2:
                vehicleInfo.put("name", "Raion Level 2");
                vehicleInfo.put("bodyStyle", "Full-Size SUV");
                vehicleInfo.put("description", "Full-size SUV inspired by Tesla Model X and Kia EV9");
                vehicleInfo.put("drivetrain", "AWD");
                vehicleInfo.put("battery", 100);
                vehicleInfo.put("range", 450);
                vehicleInfo.put("seating", 7);
                break;
            case 3:
                vehicleInfo.put("name", "Raion Level 3");
                vehicleInfo.put("bodyStyle", "Performance Sedan");
                vehicleInfo.put("description", "Performance sedan inspired by Xiaomi SU7 Max Ultra and Porsche Taycan");
                vehicleInfo.put("drivetrain", "AWD (Tri-Motor)");
                vehicleInfo.put("battery", 94);
                vehicleInfo.put("range", 350);
                break;
            case 4:
                vehicleInfo.put("name", "Raion Level 4");
                vehicleInfo.put("bodyStyle", "Ultra-Luxury SUV");
                vehicleInfo.put("description", "Ultra-luxury SUV inspired by Yangwang U8 and Rolls Royce Cullinan");
                vehicleInfo.put("drivetrain", "AWD (Quad Motor)");
                vehicleInfo.put("battery", 120);
                vehicleInfo.put("range", 620);
                vehicleInfo.put("seating", 4);
                break;
        }

        return ResponseEntity.ok(vehicleInfo);
    }

    // GET /api/vehicles/{level}/trims - Get available trims for a vehicle level
    @GetMapping("/{level}/trims")
    public ResponseEntity<?> getAvailableTrims(@PathVariable int level) {
        if (level < 1 || level > 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid level. Must be 1, 2, 3, or 4"));
        }

        List<Map<String, Object>> trims = new ArrayList<>();

        switch (level) {
            case 1:
                trims.add(createTrimInfo("STANDARD", "Standard", 45000, 290, 5.0));
                trims.add(createTrimInfo("PREMIUM", "Premium", 50000, 290, 5.0));
                trims.add(createTrimInfo("PERFORMANCE", "Performance", 55000, 360, 4.0));
                break;
            case 2:
                trims.add(createTrimInfo("STANDARD", "Standard", 85000, 670, 6.0));
                trims.add(createTrimInfo("PREMIUM", "Premium", 90000, 670, 6.0));
                trims.add(createTrimInfo("OFFROAD", "Off-Road", 95000, 670, 6.0));
                break;
            case 3:
                trims.add(createTrimInfo("PRO", "Pro", 125000, 1527, 2.0));
                trims.add(createTrimInfo("MAX", "Max", 130000, 1527, 2.0));
                trims.add(createTrimInfo("ULTRA", "Ultra", 135000, 1600, 1.8));
                break;
            case 4:
                trims.add(createTrimInfo("FLAGSHIP", "Flagship", 185000, 1180, 3.2));
                break;
        }

        return ResponseEntity.ok(Map.of("level", level, "trims", trims));
    }

    // GET /api/vehicles/{level}/colors - Get available colors for a vehicle level
    @GetMapping("/{level}/colors")
    public ResponseEntity<?> getAvailableColors(@PathVariable int level) {
        if (level < 1 || level > 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid level. Must be 1, 2, 3, or 4"));
        }

        List<Map<String, Object>> colors = new ArrayList<>();

        switch (level) {
            case 1:
            case 2:
                colors.add(createColorInfo("WHITE", "Pearl White", "#FFFFFF"));
                colors.add(createColorInfo("BLACK", "Obsidian Black", "#000000"));
                colors.add(createColorInfo("SILVER", "Liquid Silver", "#C0C0C0"));
                colors.add(createColorInfo("BLUE", "Electric Blue", "#0066CC"));
                break;
            case 3:
                colors.add(createColorInfo("PURPLE", "Ultraviolet Purple", "#6A0DAD"));
                colors.add(createColorInfo("BURGUNDY", "Deep Burgundy", "#800020"));
                colors.add(createColorInfo("GREEN", "Racing Green", "#00563B"));
                break;
            case 4:
                colors.add(createColorInfo("BLACK", "Obsidian Black", "#000000"));
                break;
        }

        return ResponseEntity.ok(Map.of("level", level, "colors", colors));
    }

    // GET /api/vehicles/{level}/options - Get available options for a vehicle level
    @GetMapping("/{level}/options")
    public ResponseEntity<?> getAvailableOptions(@PathVariable int level) {
        if (level < 1 || level > 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid level. Must be 1, 2, 3, or 4"));
        }

        List<Map<String, Object>> options = new ArrayList<>();

        // Options available on all levels
        options.add(createOptionInfo("Enhanced Autopilot", 6000, "Autopilot"));
        options.add(createOptionInfo("Full Self-Driving Capability", 8000, "Autopilot"));
        options.add(createOptionInfo("Custom Paint Color", 2000, "Exterior"));

        // Level-specific options
        if (level == 2 || level == 3) {
            options.add(createOptionInfo("Massage Seats (Front & Rear)", 3000, "Comfort"));
        }

        if (level == 3) {
            options.add(createOptionInfo("Track Package", 10000, "Performance"));
        }

        if (level == 4) {
            options.add(createOptionInfo("Massage Seats (Front & Rear)", 5000, "Comfort"));
        }

        return ResponseEntity.ok(Map.of("level", level, "options", options));
    }

    // Helper method to create trim info map
    private Map<String, Object> createTrimInfo(String code, String name, double price, int power, double acceleration) {
        Map<String, Object> trim = new HashMap<>();
        trim.put("code", code);
        trim.put("name", name);
        trim.put("price", price);
        trim.put("power", power);
        trim.put("acceleration", acceleration);
        return trim;
    }

    // Helper method to create color info map
    private Map<String, Object> createColorInfo(String code, String name, String hex) {
        Map<String, Object> color = new HashMap<>();
        color.put("code", code);
        color.put("name", name);
        color.put("hex", hex);
        return color;
    }

    // Helper method to create option info map
    private Map<String, Object> createOptionInfo(String name, double price, String category) {
        Map<String, Object> option = new HashMap<>();
        option.put("name", name);
        option.put("price", price);
        option.put("category", category);
        return option;
    }
}
