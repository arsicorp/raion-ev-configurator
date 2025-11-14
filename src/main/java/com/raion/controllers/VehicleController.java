package com.raion.controllers;

import com.raion.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// rest api controller for vehicle information
// handles all endpoints related to vehicles, trims, colors, and specs
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VehicleController {

    // get /api/vehicles - get information about all 4 vehicle models
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicles() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> vehicles = new ArrayList<>();

        // level 1 info
        Map<String, Object> level1 = new HashMap<>();
        level1.put("level", 1);
        level1.put("name", "Raion Level 1");
        level1.put("bodyStyle", "Compact Sedan");
        level1.put("trims", Arrays.asList("STANDARD", "PREMIUM", "PERFORMANCE"));
        level1.put("colors", Arrays.asList("WHITE", "BLACK", "SILVER", "BLUE"));
        level1.put("priceRange", Map.of("min", 45000, "max", 55000));
        level1.put("range", 400);
        vehicles.add(level1);

        // level 2 info
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

        // level 3 info
        Map<String, Object> level3 = new HashMap<>();
        level3.put("level", 3);
        level3.put("name", "Raion Level 3");
        level3.put("bodyStyle", "Performance Sedan");
        level3.put("trims", Arrays.asList("PRO", "MAX", "ULTRA"));
        level3.put("colors", Arrays.asList("PURPLE", "BURGUNDY", "GREEN"));
        level3.put("priceRange", Map.of("min", 125000, "max", 135000));
        level3.put("range", 350);
        vehicles.add(level3);

        // level 4 info
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

    // get /api/vehicles/{level} - get complete vehicle configuration data
    // this returns everything the frontend configurator needs: trims, colors, options, accessories, specs
    @GetMapping("/{level}")
    public ResponseEntity<?> getVehicleByLevel(@PathVariable int level) {
        if (level < 1 || level > 4) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid level. Must be 1, 2, 3, or 4")
            );
        }

        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("level", level);

        // add basic vehicle info
        switch (level) {
            case 1:
                vehicleData.put("name", "Raion Level 1");
                vehicleData.put("bodyStyle", "Compact Sedan");
                vehicleData.put("description", "compact sedan inspired by tesla model 3");
                vehicleData.put("drivetrain", "RWD");
                vehicleData.put("battery", 80);
                vehicleData.put("range", 400);
                break;
            case 2:
                vehicleData.put("name", "Raion Level 2");
                vehicleData.put("bodyStyle", "Full-Size SUV");
                vehicleData.put("description", "full-size suv inspired by tesla model x and kia ev9");
                vehicleData.put("drivetrain", "AWD");
                vehicleData.put("battery", 100);
                vehicleData.put("range", 450);
                vehicleData.put("seating", 7);
                break;
            case 3:
                vehicleData.put("name", "Raion Level 3");
                vehicleData.put("bodyStyle", "Performance Sedan");
                vehicleData.put("description", "performance sedan inspired by xiaomi su7 max ultra and porsche taycan");
                vehicleData.put("drivetrain", "AWD (Tri-Motor)");
                vehicleData.put("battery", 94);
                vehicleData.put("range", 350);
                break;
            case 4:
                vehicleData.put("name", "Raion Level 4");
                vehicleData.put("bodyStyle", "Ultra-Luxury SUV");
                vehicleData.put("description", "ultra-luxury suv inspired by yangwang u8 and rolls royce cullinan");
                vehicleData.put("drivetrain", "AWD (Quad Motor)");
                vehicleData.put("battery", 120);
                vehicleData.put("range", 620);
                vehicleData.put("seating", 4);
                break;
        }

        // add trims with full details (frontend needs: name, price, power, acceleration, range, battery)
        vehicleData.put("trims", getTrimsForLevel(level));

        // add colors with name and hex (frontend needs this format)
        vehicleData.put("colors", getColorsForLevel(level));

        // add available options with id, name, price (frontend needs id to track selections)
        vehicleData.put("options", getOptionsForLevel(level));

        // add available accessories with id, name, price (frontend needs id to track selections)
        vehicleData.put("accessories", getAccessoriesForLevel(level));

        return ResponseEntity.ok(vehicleData);
    }

    // get available trims for a vehicle level with complete specs
    private List<Map<String, Object>> getTrimsForLevel(int level) {
        List<Map<String, Object>> trims = new ArrayList<>();

        switch (level) {
            case 1:
                trims.add(createTrimInfo("Standard", 45000, 290, 5.0, 140, 400, 80));
                trims.add(createTrimInfo("Premium", 50000, 290, 5.0, 140, 400, 80));
                trims.add(createTrimInfo("Performance", 55000, 360, 4.0, 155, 400, 80));
                break;
            case 2:
                trims.add(createTrimInfo("Standard", 85000, 670, 6.0, 130, 450, 100));
                trims.add(createTrimInfo("Premium", 90000, 670, 6.0, 130, 450, 100));
                trims.add(createTrimInfo("Off-Road", 95000, 670, 6.0, 130, 450, 100));
                break;
            case 3:
                trims.add(createTrimInfo("Pro", 125000, 1527, 2.0, 217, 350, 94));
                trims.add(createTrimInfo("Max", 130000, 1527, 2.0, 217, 350, 94));
                trims.add(createTrimInfo("Ultra", 135000, 1600, 1.8, 224, 350, 94));
                break;
            case 4:
                trims.add(createTrimInfo("Flagship", 185000, 1180, 3.2, 155, 620, 120));
                break;
        }

        return trims;
    }

    // get available colors for a vehicle level
    private List<Map<String, Object>> getColorsForLevel(int level) {
        List<Map<String, Object>> colors = new ArrayList<>();

        switch (level) {
            case 1:
            case 2:
                colors.add(createColorInfo("white", "Pearl White", "#FFFFFF"));
                colors.add(createColorInfo("black", "Obsidian Black", "#000000"));
                colors.add(createColorInfo("silver", "Liquid Silver", "#C0C0C0"));
                colors.add(createColorInfo("blue", "Electric Blue", "#0066CC"));
                break;
            case 3:
                colors.add(createColorInfo("purple", "Ultraviolet Purple", "#6A0DAD"));
                colors.add(createColorInfo("burgundy", "Deep Burgundy", "#800020"));
                colors.add(createColorInfo("green", "Racing Green", "#00563B"));
                break;
            case 4:
                colors.add(createColorInfo("black", "Obsidian Black", "#000000"));
                break;
        }

        return colors;
    }

    // get available options for a vehicle level
    private List<Map<String, Object>> getOptionsForLevel(int level) {
        List<Map<String, Object>> options = new ArrayList<>();

        // options available on all levels
        options.add(createOptionInfo("enhanced-autopilot", "Enhanced Autopilot", 6000,
                "Navigate on Autopilot, Auto Lane Change, Autopark, Summon, Smart Summon"));
        options.add(createOptionInfo("full-self-driving", "Full Self-Driving Capability", 8000,
                "All Enhanced Autopilot features plus Traffic Light and Stop Sign Control"));
        options.add(createOptionInfo("custom-paint", "Custom Paint Color", 2000,
                "Exclusive custom paint finish beyond standard color options"));

        // level-specific options
        if (level == 2 || level == 3) {
            options.add(createOptionInfo("massage-seats", "Massage Seats (Front & Rear)", 3000,
                    "Multi-point massage functionality for front and rear seats"));
        }

        if (level == 3) {
            options.add(createOptionInfo("track-package", "Track Package", 10000,
                    "Carbon ceramic brakes, track telemetry system, lap timer with GPS"));
        }

        if (level == 4) {
            options.add(createOptionInfo("massage-seats", "Massage Seats (Front & Rear)", 5000,
                    "18-point massage functionality for front and rear executive seats"));
        }

        return options;
    }

    // get available accessories for all levels (accessories are universal)
    private List<Map<String, Object>> getAccessoriesForLevel(int level) {
        List<Map<String, Object>> accessories = new ArrayList<>();

        accessories.add(createAccessoryInfo("floor-mats", "Premium Floor Mats", 400,
                "All-weather floor mats with Raion logo for all rows"));
        accessories.add(createAccessoryInfo("home-charger", "Home EV Charger (Level 2, 240V)", 800,
                "Wall-mounted Level 2 charger with 25-foot cable"));
        accessories.add(createAccessoryInfo("paint-protection", "Paint Protection Film (Full Front)", 2000,
                "Clear protective film for front bumper, hood, fenders, and mirrors"));
        accessories.add(createAccessoryInfo("ceramic-coating", "Ceramic Coating (Full Vehicle)", 1500,
                "Professional-grade ceramic coating for entire vehicle"));

        return accessories;
    }

    // helper method to create trim info with all specs
    private Map<String, Object> createTrimInfo(String name, double price, int power,
                                               double acceleration, int topSpeed, int range, int battery) {
        Map<String, Object> trim = new HashMap<>();
        trim.put("name", name);
        trim.put("price", price);
        trim.put("power", power);
        trim.put("acceleration", acceleration);
        trim.put("topSpeed", topSpeed);
        trim.put("range", range);
        trim.put("battery", battery);
        return trim;
    }

    // helper method to create color info
    private Map<String, Object> createColorInfo(String name, String displayName, String hex) {
        Map<String, Object> color = new HashMap<>();
        color.put("name", name);  // lowercase for frontend
        color.put("displayName", displayName);
        color.put("hex", hex);
        return color;
    }

    // helper method to create option info with id
    private Map<String, Object> createOptionInfo(String id, String name, double price, String description) {
        Map<String, Object> option = new HashMap<>();
        option.put("id", id);  // frontend uses this to track selections
        option.put("name", name);
        option.put("price", price);
        option.put("description", description);
        return option;
    }

    // helper method to create accessory info with id
    private Map<String, Object> createAccessoryInfo(String id, String name, double price, String description) {
        Map<String, Object> accessory = new HashMap<>();
        accessory.put("id", id);  // frontend uses this to track selections
        accessory.put("name", name);
        accessory.put("price", price);
        accessory.put("description", description);
        return accessory;
    }
}