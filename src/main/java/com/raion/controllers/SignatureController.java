package com.raion.controllers;

import com.raion.models.signatures.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// rest api controller for signature vehicles
// handles pre-configured vehicle packages that save customers money
@RestController
@RequestMapping("/api/signatures")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SignatureController {

    // get /api/signatures - get information about all 4 signature vehicles
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSignatures() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> signatures = new ArrayList<>();

        // urban commuter signature
        UrbanCommuterSignature urbanCommuter = new UrbanCommuterSignature();
        signatures.add(createSignatureSummary(
                "urban-commuter",
                urbanCommuter.getSignatureName(),
                "Level 1 Premium",
                urbanCommuter.calculatePrice(),
                urbanCommuter.getSavings(),
                urbanCommuter.getTargetCustomer(),
                "Silver",
                urbanCommuter.getRange(),
                urbanCommuter.getPower(),
                urbanCommuter.getAcceleration()
        ));

        // trail titan signature
        TrailTitanSignature trailTitan = new TrailTitanSignature();
        signatures.add(createSignatureSummary(
                "trail-titan",
                trailTitan.getSignatureName(),
                "Level 2 Off-Road",
                trailTitan.calculatePrice(),
                trailTitan.getSavings(),
                trailTitan.getTargetCustomer(),
                "Black",
                trailTitan.getRange(),
                trailTitan.getPower(),
                trailTitan.getAcceleration()
        ));

        // track beast signature
        TrackBeastSignature trackBeast = new TrackBeastSignature();
        signatures.add(createSignatureSummary(
                "track-beast",
                trackBeast.getSignatureName(),
                "Level 3 Ultra",
                trackBeast.calculatePrice(),
                trackBeast.getSavings(),
                trackBeast.getTargetCustomer(),
                "Green",
                trackBeast.getRange(),
                trackBeast.getPower(),
                trackBeast.getAcceleration()
        ));

        // executive signature
        ExecutiveSignature executive = new ExecutiveSignature();
        signatures.add(createSignatureSummary(
                "executive",
                executive.getSignatureName(),
                "Level 4 Flagship",
                executive.calculatePrice(),
                executive.getSavings(),
                executive.getTargetCustomer(),
                "Black",
                executive.getRange(),
                executive.getPower(),
                executive.getAcceleration()
        ));

        response.put("signatures", signatures);
        response.put("count", signatures.size());
        return ResponseEntity.ok(response);
    }

    // get /api/signatures/{name} - get detailed info about a specific signature
    @GetMapping("/{name}")
    public ResponseEntity<?> getSignatureByName(@PathVariable String name) {
        name = name.toLowerCase().trim();

        Map<String, Object> signatureDetails = new HashMap<>();

        switch (name) {
            case "urban-commuter":
                UrbanCommuterSignature urbanCommuter = new UrbanCommuterSignature();
                signatureDetails = createDetailedSignatureInfo(
                        "urban-commuter",
                        urbanCommuter.getSignatureName(),
                        urbanCommuter.getSignatureDescription(),
                        1,
                        "Level 1 Premium",
                        urbanCommuter,
                        urbanCommuter.getIncludedFeatures(),
                        urbanCommuter.getTargetCustomer(),
                        urbanCommuter.calculatePrice(),
                        urbanCommuter.getRegularPrice(),
                        urbanCommuter.getSavings()
                );
                break;

            case "trail-titan":
                TrailTitanSignature trailTitan = new TrailTitanSignature();
                signatureDetails = createDetailedSignatureInfo(
                        "trail-titan",
                        trailTitan.getSignatureName(),
                        trailTitan.getSignatureDescription(),
                        2,
                        "Level 2 Off-Road",
                        trailTitan,
                        trailTitan.getIncludedFeatures(),
                        trailTitan.getTargetCustomer(),
                        trailTitan.calculatePrice(),
                        trailTitan.getRegularPrice(),
                        trailTitan.getSavings()
                );
                break;

            case "track-beast":
                TrackBeastSignature trackBeast = new TrackBeastSignature();
                signatureDetails = createDetailedSignatureInfo(
                        "track-beast",
                        trackBeast.getSignatureName(),
                        trackBeast.getSignatureDescription(),
                        3,
                        "Level 3 Ultra",
                        trackBeast,
                        trackBeast.getIncludedFeatures(),
                        trackBeast.getTargetCustomer(),
                        trackBeast.calculatePrice(),
                        trackBeast.getRegularPrice(),
                        trackBeast.getSavings()
                );
                break;

            case "executive":
                ExecutiveSignature executive = new ExecutiveSignature();
                signatureDetails = createDetailedSignatureInfo(
                        "executive",
                        executive.getSignatureName(),
                        executive.getSignatureDescription(),
                        4,
                        "Level 4 Flagship",
                        executive,
                        executive.getIncludedFeatures(),
                        executive.getTargetCustomer(),
                        executive.calculatePrice(),
                        executive.getRegularPrice(),
                        executive.getSavings()
                );
                break;

            default:
                return ResponseEntity.badRequest().body(
                        Map.of("error", "invalid signature name. must be: urban-commuter, trail-titan, track-beast, or executive")
                );
        }

        return ResponseEntity.ok(signatureDetails);
    }

    // helper method to create signature summary (for list view)
    private Map<String, Object> createSignatureSummary(
            String id,
            String name,
            String basedOn,
            double price,
            double savings,
            String targetCustomer,
            String color,
            int range,
            int power,
            double acceleration
    ) {
        Map<String, Object> signature = new HashMap<>();
        signature.put("id", id);
        signature.put("name", name);
        signature.put("basedOn", basedOn);
        signature.put("price", price);
        signature.put("savings", savings);
        signature.put("targetCustomer", targetCustomer);
        signature.put("color", color);

        // key specs
        Map<String, Object> specs = new HashMap<>();
        specs.put("range", range);
        specs.put("power", power);
        specs.put("acceleration", acceleration);
        signature.put("specs", specs);

        return signature;
    }

    // helper method to create detailed signature info (for detail view)
    private Map<String, Object> createDetailedSignatureInfo(
            String id,
            String name,
            String description,
            int baseLevel,
            String basedOn,
            Object vehicle,
            String includedFeatures,
            String targetCustomer,
            double signaturePrice,
            double regularPrice,
            double savings
    ) {
        Map<String, Object> details = new HashMap<>();

        // basic info
        details.put("id", id);
        details.put("name", name);
        details.put("description", description);
        details.put("baseLevel", baseLevel);
        details.put("basedOn", basedOn);

        // pricing
        Map<String, Object> pricing = new HashMap<>();
        pricing.put("signaturePrice", signaturePrice);
        pricing.put("regularPrice", regularPrice);
        pricing.put("savings", savings);
        pricing.put("savingsPercentage", Math.round((savings / regularPrice) * 100 * 100.0) / 100.0);
        details.put("pricing", pricing);

        // features and target customer
        details.put("includedFeatures", includedFeatures);
        details.put("targetCustomer", targetCustomer);

        // vehicle specifications
        Map<String, Object> specs = new HashMap<>();
        if (vehicle instanceof UrbanCommuterSignature) {
            UrbanCommuterSignature v = (UrbanCommuterSignature) vehicle;
            specs.put("color", v.getColor().getDisplayName());
            specs.put("colorHex", v.getColor().getHexCode());
            specs.put("drivetrain", v.getDrivetrain());
            specs.put("power", v.getPower());
            specs.put("range", v.getRange());
            specs.put("acceleration", v.getAcceleration());
            specs.put("topSpeed", v.getTopSpeed());
            specs.put("battery", v.getBatteryCapacity());
            specs.put("homeChargingTime", Math.round(v.getHomeChargingTime() * 10.0) / 10.0);
            specs.put("fastChargingTime", Math.round(v.getFastChargingTime()));
        } else if (vehicle instanceof TrailTitanSignature) {
            TrailTitanSignature v = (TrailTitanSignature) vehicle;
            specs.put("color", v.getColor().getDisplayName());
            specs.put("colorHex", v.getColor().getHexCode());
            specs.put("drivetrain", v.getDrivetrain());
            specs.put("power", v.getPower());
            specs.put("range", v.getRange());
            specs.put("acceleration", v.getAcceleration());
            specs.put("topSpeed", v.getTopSpeed());
            specs.put("battery", v.getBatteryCapacity());
            specs.put("seating", v.getSeatingCapacity());
            specs.put("homeChargingTime", Math.round(v.getHomeChargingTime() * 10.0) / 10.0);
            specs.put("fastChargingTime", Math.round(v.getFastChargingTime()));
        } else if (vehicle instanceof TrackBeastSignature) {
            TrackBeastSignature v = (TrackBeastSignature) vehicle;
            specs.put("color", v.getColor().getDisplayName());
            specs.put("colorHex", v.getColor().getHexCode());
            specs.put("drivetrain", v.getDrivetrain());
            specs.put("power", v.getPower());
            specs.put("range", v.getRange());
            specs.put("acceleration", v.getAcceleration());
            specs.put("topSpeed", v.getTopSpeed());
            specs.put("battery", v.getBatteryCapacity());
            specs.put("homeChargingTime", Math.round(v.getHomeChargingTime() * 10.0) / 10.0);
            specs.put("fastChargingTime", Math.round(v.getFastChargingTime()));
        } else if (vehicle instanceof ExecutiveSignature) {
            ExecutiveSignature v = (ExecutiveSignature) vehicle;
            specs.put("color", v.getColor().getDisplayName());
            specs.put("colorHex", v.getColor().getHexCode());
            specs.put("drivetrain", v.getDrivetrain());
            specs.put("power", v.getPower());
            specs.put("range", v.getRange());
            specs.put("acceleration", v.getAcceleration());
            specs.put("topSpeed", v.getTopSpeed());
            specs.put("battery", v.getBatteryCapacity());
            specs.put("seating", v.getSeatingCapacity());
            specs.put("homeChargingTime", Math.round(v.getHomeChargingTime() * 10.0) / 10.0);
            specs.put("fastChargingTime", Math.round(v.getFastChargingTime()));
            specs.put("fullSelfDrivingIncluded", true);
        }
        details.put("specifications", specs);

        return details;
    }
}