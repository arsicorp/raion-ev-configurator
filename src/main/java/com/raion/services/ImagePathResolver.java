package com.raion.services;

import com.raion.models.Vehicle;
import com.raion.models.VehicleColor;

import java.io.File;

// Resolves image paths for vehicle photos
// Handles the 48 total images across all levels (4 views per color per level)
public class ImagePathResolver {

    private static final String BASE_IMAGE_PATH = "images";
    private static final String[] VALID_VIEWS = {"front", "side", "back", "interior"};

    // Private constructor - utility class with only static methods
    private ImagePathResolver() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Get image path for a specific vehicle and view
    // Returns path like: images/level1/white/front.png
    public static String getImagePath(Vehicle vehicle, String view) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        return getImagePath(vehicle.getLevel(), vehicle.getColor(), view);
    }

    // Get image path for specific level, color, and view
    public static String getImagePath(int level, VehicleColor color, String view) {
        if (level < 1 || level > 4) {
            throw new IllegalArgumentException("Level must be between 1 and 4");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (view == null || view.trim().isEmpty()) {
            throw new IllegalArgumentException("View cannot be empty");
        }

        // Normalize view to lowercase
        view = view.toLowerCase().trim();

        // Validate view is one of the supported types
        boolean validView = false;
        for (String validViewType : VALID_VIEWS) {
            if (validViewType.equals(view)) {
                validView = true;
                break;
            }
        }

        if (!validView) {
            throw new IllegalArgumentException(
                    "View must be one of: front, side, back, interior"
            );
        }

        // Convert color enum to lowercase for folder name
        String colorFolder = color.name().toLowerCase();

        // Build path: images/level{X}/{color}/{view}.png
        return BASE_IMAGE_PATH + "/level" + level + "/" + colorFolder + "/" + view + ".png";
    }

    // Get all image paths for a vehicle (all 4 views)
    public static String[] getAllImagePaths(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        String[] paths = new String[VALID_VIEWS.length];
        for (int i = 0; i < VALID_VIEWS.length; i++) {
            paths[i] = getImagePath(vehicle, VALID_VIEWS[i]);
        }

        return paths;
    }

    // Check if an image file exists at the given path
    public static boolean imageExists(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return false;
        }

        File imageFile = new File(imagePath);
        return imageFile.exists() && imageFile.isFile();
    }

    // Get placeholder image URL for when actual image doesn't exist
    // Uses placeholder service for development
    public static String getPlaceholderUrl(int width, int height, String text) {
        // Using a placeholder service like placehold.co
        String encodedText = text.replace(" ", "+");
        return "https://placehold.co/" + width + "x" + height + "/667eea/ffffff?text=" + encodedText;
    }

    // Get placeholder for vehicle view
    public static String getVehiclePlaceholder(Vehicle vehicle, String view) {
        if (vehicle == null) {
            return getPlaceholderUrl(800, 600, "No+Vehicle");
        }

        String text = vehicle.getModelName().replace(" ", "+") + "+" +
                view.substring(0, 1).toUpperCase() + view.substring(1);
        return getPlaceholderUrl(800, 600, text);
    }

    // Get image path or placeholder if image doesn't exist
    public static String getImagePathOrPlaceholder(Vehicle vehicle, String view) {
        String imagePath = getImagePath(vehicle, view);

        if (imageExists(imagePath)) {
            return imagePath;
        } else {
            return getVehiclePlaceholder(vehicle, view);
        }
    }

    // Get all supported views
    public static String[] getSupportedViews() {
        return VALID_VIEWS.clone();
    }

    // Get base image directory path
    public static String getBaseImagePath() {
        return BASE_IMAGE_PATH;
    }
}
