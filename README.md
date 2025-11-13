# Raion Motors
### Electric Vehicle Configurator Platform

A full-stack web application that brings the car-buying experience into the digital age. Built for speed, scalability, and user experience, this configurator allows customers to design their perfect electric vehicle with real-time pricing, financing calculations, and instant order processing.

The platform handles everything from vehicle customization to payment estimation, environmental impact analysis, and automated receipt generation. Think of it as the foundation for a modern EV dealership's online presence.

## What This Does

Raion Motors provides a complete vehicle configuration experience across four model lines. Customers can choose from twelve different trim levels, customize colors, add performance packages, and see their total price update in real-time. The system also features pre-built signature vehicles for customers who want expert recommendations without the decision fatigue.

The backend is built with Java and Spring Boot, handling all the business logic, pricing calculations, and order management. The frontend uses vanilla JavaScript for a lightweight, fast interface that works on any device. No frameworks, no bloat, just clean code that does what it needs to do.

**Core capabilities:**
- Real-time vehicle configuration with dynamic pricing
- Advanced payment calculator with customizable loan terms
- Environmental impact tracking and CO2 savings metrics
- Automated order processing with receipt generation
- Signature vehicle collection with pre-configured packages
- Responsive design that works on desktop, tablet, and mobile

## Technical Highlights

The platform architecture demonstrates several key engineering decisions that make it production-ready:

**Object-Oriented Design**
The vehicle hierarchy uses abstract base classes and multi-level inheritance. Each vehicle type (Level 1 through 4) extends a common Vehicle class, while signature models extend their base vehicle types. This creates a flexible system where new models can be added without touching existing code.

**RESTful API Structure**
All backend logic is exposed through clean REST endpoints. The API handles vehicle data, pricing calculations, environmental metrics, and order processing. CORS is configured for cross-origin requests, making it easy to integrate with any frontend.

**Design Patterns in Action**
Factory methods handle object creation for options, packages, and accessories. The Strategy pattern manages different pricing calculations. Builder pattern constructs complex orders step by step. These patterns make the codebase maintainable and extensible.

**Frontend Architecture**
The interface uses vanilla JavaScript with no frameworks. This keeps the bundle size minimal and load times fast. The code uses modern ES6+ features like async/await, destructuring, and arrow functions. State management is simple and predictable.

**Real-Time Calculations**
Every price change triggers immediate recalculation of subtotals, tax, monthly payments, and environmental impact. The payment calculator uses standard amortization formulas. Charging time estimates are based on actual battery capacities and charging rates.

## The Vehicle Lineup

**Level 1 - Compact Sedan**
Starting at $45,000. Available in Standard, Premium, and Performance trims. Think daily driver with style. RWD, 290-360 hp, 400 mile range. Comes in White, Black, Silver, and Blue.

**Level 2 - Full-Size SUV**
Starting at $85,000. Standard, Premium, and Off-Road configurations. Seven seats, AWD, 670 hp, 450 mile range. Same color options as Level 1. The Off-Road trim adds lifted suspension and all-terrain capability.

**Level 3 - Performance Sedan**
Starting at $125,000. Pro, Max, and Ultra trims. This is where things get serious. Tri-motor AWD, 1,527-1,600 hp, 0-60 in under 2 seconds. Exclusive colors: Purple, Burgundy, Green. The Ultra trim hits 224 mph.

**Level 4 - Ultra-Luxury SUV**
$185,000. Single Flagship configuration. Quad-motor AWD, 1,180 hp, 620 mile range. Four-seat executive layout with every luxury feature included. Black only. This is the halo vehicle.

## Signature Collection

Four pre-configured vehicles designed for specific use cases. Each signature saves money compared to building the same configuration manually.

**Urban Commuter** - $58,000 (save $500)
Level 1 Premium in Silver with Enhanced Autopilot. Built for city professionals who want luxury and autonomy without the hassle of choosing every option.

**Trail Titan** - $98,500 (save $1,000)
Level 2 Off-Road in Black with Premium Maintenance Package. For weekend adventurers who need capability and peace of mind.

**Track Beast** - $145,000 (save $2,000)
Level 3 Ultra in Green with Track Package. Maximum performance with telemetry systems and carbon ceramic brakes. Built for speed enthusiasts.

**Executive** - $195,000 (save $2,000)
Level 4 Flagship in Black with massage seats and extended warranty. The complete luxury experience with comprehensive coverage.

## Technology Stack

**Backend**
- Java 17 with Spring Boot 3.x
- Maven for build management
- RESTful API architecture
- CORS enabled for frontend integration

**Frontend**
- HTML5 semantic markup
- CSS3 with modern features (variables, flexbox, grid)
- Vanilla JavaScript ES6+
- Fetch API for async requests
- Mobile-first responsive design

**Development Tools**
- IntelliJ IDEA for Java development
- Git for version control
- Maven for dependency management
- Browser DevTools for frontend debugging

## Project Structure

```
raion-ev-configurator/
├── backend/
│   ├── src/main/java/com/raion/
│   │   ├── models/
│   │   │   ├── Vehicle.java                 (Abstract base class)
│   │   │   ├── Level1.java                  (Compact Sedan)
│   │   │   ├── Level2.java                  (Full-Size SUV)
│   │   │   ├── Level3.java                  (Performance Sedan)
│   │   │   ├── Level4.java                  (Ultra-Luxury SUV)
│   │   │   ├── signatures/
│   │   │   │   ├── UrbanCommuterSignature.java
│   │   │   │   ├── TrailTitanSignature.java
│   │   │   │   ├── TrackBeastSignature.java
│   │   │   │   └── ExecutiveSignature.java
│   │   │   ├── Feature.java                 (Interface)
│   │   │   ├── Option.java
│   │   │   ├── ServicePackage.java
│   │   │   ├── Accessory.java
│   │   │   ├── Order.java
│   │   │   ├── TrimLevel.java               (Enum)
│   │   │   └── VehicleColor.java            (Enum)
│   │   ├── controllers/
│   │   │   ├── VehicleController.java
│   │   │   ├── SignatureController.java
│   │   │   └── OrderController.java
│   │   ├── services/
│   │   │   ├── PriceCalculator.java
│   │   │   ├── ReceiptGenerator.java
│   │   │   ├── EnvironmentalCalculator.java
│   │   │   └── ImagePathResolver.java
│   │   └── RaionConfiguratorApplication.java
│   ├── receipts/                            (Generated order receipts)
│   └── pom.xml
├── frontend/
│   ├── index.html
│   ├── styles.css
│   ├── script.js
│   └── images/
│       ├── level1/
│       ├── level2/
│       ├── level3/
│       └── level4/
└── README.md
```

## Getting Started

### Requirements
- Java 17 or higher
- Maven 3.6+
- Modern web browser
- Git

### Running the Backend

Clone the repository and navigate to the backend directory:
```bash
git clone https://github.com/yourusername/raion-ev-configurator.git
cd raion-ev-configurator/backend
```

Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. You should see "Raion EV Configurator API is running!" in the console.

### Running the Frontend

From the frontend directory, start a local server:

Using Python:
```bash
cd ../frontend
python -m http.server 5500
```

Using Node.js:
```bash
npx http-server -p 5500
```

Using VS Code Live Server:
- Install the Live Server extension
- Right-click `index.html` and select "Open with Live Server"

Access the application at `http://localhost:5500`

### Setting Up Receipts

Create the receipts directory where order confirmations will be saved:
```bash
mkdir backend/receipts
```

### Adding Vehicle Images

The app uses placeholder images by default. To add real images, organize them like this:

```
frontend/images/
├── level1/
│   ├── white/     (front.png, side.png, back.png, interior.png)
│   ├── black/
│   ├── silver/
│   └── blue/
├── level2/        (same structure)
├── level3/        (purple, burgundy, green)
└── level4/        (black only)
```

Each vehicle needs 4 views: front, side, back, interior. Total of 48 images across all models.

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Vehicle Endpoints

**Get All Vehicles**
```http
GET /vehicles
```
Returns a list of all four vehicle models with basic information.

**Get Vehicle Details**
```http
GET /vehicles/{level}
```
Parameters: `level` (1-4)
Returns detailed information about the specified vehicle level.

**Get Available Trims**
```http
GET /vehicles/{level}/trims
```
Returns all trim levels for the vehicle with pricing and performance specs.

**Get Available Colors**
```http
GET /vehicles/{level}/colors
```
Returns color options with hex codes for the specified vehicle.

**Get Available Options**
```http
GET /vehicles/{level}/options
```
Returns upgrades and packages available for the vehicle level.

### Signature Endpoints

**Get All Signatures**
```http
GET /signatures
```
Returns all four signature vehicles with summary information.

**Get Signature Details**
```http
GET /signatures/{name}
```
Parameters: `name` (urban-commuter, trail-titan, track-beast, executive)
Returns complete details for the specified signature vehicle.

### Order Endpoints

**Place Custom Order**
```http
POST /order
Content-Type: application/json

{
  "level": 2,
  "trim": "PREMIUM",
  "color": "SILVER",
  "options": ["enhanced-autopilot", "massage-seats"],
  "servicePackages": ["warranty-8yr"],
  "accessories": ["floor-mats", "home-charger"]
}
```
Response includes order confirmation with receipt file path.

**Place Signature Order**
```http
POST /order/signature
Content-Type: application/json

{
  "signatureName": "trail-titan",
  "additionalOptions": ["full-self-driving"],
  "accessories": ["ceramic-coating"]
}
```
Response includes order confirmation with signature vehicle details.

## Architecture Decisions

### Why This Structure Works

The codebase is organized around a vehicle hierarchy that makes adding new models straightforward. Every vehicle extends an abstract Vehicle class, which defines the common interface. Level 1 through 4 implement the specifics for each model. Signature vehicles extend their base models, inheriting all the behavior and just customizing a few values.

This means adding a new model is as simple as creating a new class that extends Vehicle. No need to modify existing code. Same goes for signature vehicles - they just extend an existing level and override the price and features.

### Inheritance Structure

```
Vehicle (abstract)
├── Level1 (Compact Sedan)
│   └── UrbanCommuterSignature
├── Level2 (Full-Size SUV)
│   └── TrailTitanSignature
├── Level3 (Performance Sedan)
│   └── TrackBeastSignature
└── Level4 (Ultra-Luxury SUV)
    └── ExecutiveSignature
```

The Vehicle class forces every subclass to implement `calculatePrice()`, `getSpecifications()`, and `getLevel()`. This ensures consistency across the entire lineup. Each level sets its own specs in the constructor based on the selected trim.

### Polymorphism in Practice

The Feature interface lets us treat all add-ons the same way, whether they're options, service packages, or accessories:

```java
List<Feature> features = new ArrayList<>();
features.add(Option.createEnhancedAutopilot());
features.add(ServicePackage.createExtendedWarranty8Year());
features.add(Accessory.createPremiumFloorMats());

for (Feature feature : features) {
    total += feature.getPrice();
}
```

The Order class doesn't care what type of feature it is. It just calls `getPrice()` on each one. This makes the code flexible - adding a new feature type doesn't require changing the Order class.

### Abstraction Layer

The Vehicle class is abstract because there's no such thing as just a "Vehicle" - you need to pick a specific level. The abstract class enforces this:

```java
public abstract class Vehicle {
    protected String modelName;
    protected TrimLevel trimLevel;
    
    public abstract double calculatePrice();
    public abstract String getSpecifications();
    public abstract int getLevel();
}
```

Each subclass has to implement these methods. This prevents incomplete implementations and ensures every vehicle can calculate its price and display its specs.

### Data Protection

The Order class keeps its internal state private and exposes it through controlled methods:

```java
public class Order {
    private final String orderId;
    private final Vehicle vehicle;
    private final List<Feature> features;
    
    public void addFeature(Feature feature) {
        if (!feature.isEligibleFor(vehicle.getLevel())) {
            throw new IllegalArgumentException("Feature not eligible");
        }
        features.add(feature);
    }
    
    public List<Feature> getFeatures() {
        return new ArrayList<>(features);  // Defensive copy
    }
}
```

The order ID can't be changed after creation. Adding features goes through validation. Getting the features list returns a copy, so external code can't modify the internal list.

### Pattern Usage

**Factory Pattern** for creating common objects:
```java
public static Option createEnhancedAutopilot() {
    return new Option("Enhanced Autopilot", 6000.00, 
                     "Navigate on Autopilot...", "Autopilot");
}
```

**Strategy Pattern** for different calculation methods:
```java
// Different strategies based on APR
if (monthlyRate == 0) {
    return loanAmount / months;
} else {
    return loanAmount * amortizationFormula;
}
```

**Builder Pattern** for constructing orders:
```java
Order order = new Order(vehicle);
order.addFeature(option1);
order.addFeature(option2);
order.addFeature(accessory);
```

These patterns emerged naturally from the problem domain. They weren't forced in for the sake of using patterns.

## Code Quality Highlights

### Input Validation Everywhere

Every constructor and method validates its inputs. No assumptions about what gets passed in:

```java
if (name == null || name.trim().isEmpty()) {
    throw new IllegalArgumentException("Name cannot be empty");
}
if (price < 0) {
    throw new IllegalArgumentException("Price cannot be negative");
}
```

This catches bugs early and makes debugging easier. Better to fail fast with a clear message than to let bad data propagate through the system.

### Smart Enum Design

The enums aren't just constants - they have behavior. TrimLevel knows which vehicle levels can use it:

```java
public enum TrimLevel {
    STANDARD("Standard", "Essential features..."),
    PREMIUM("Premium", "Enhanced luxury...");
    
    public boolean isLevel1Trim() {
        return this == STANDARD || this == PREMIUM || this == PERFORMANCE;
    }
}
```

This moves validation logic into the enum itself, keeping it close to the data it validates.

### Clean Factory Methods

Creating complex objects is simplified with factory methods that set sensible defaults:

```java
public static ServicePackage createExtendedWarranty8Year() {
    return new ServicePackage(
        "Extended Warranty - 8 Years",
        5000.00,
        "Extends coverage to 8 years / 100,000 miles",
        8
    );
}
```

Client code just calls the factory method. No need to remember all the parameters or their correct order.

### Professional Output Formatting

Receipt generation uses proper business formatting:

```java
private static final String LINE_SEPARATOR = "=".repeat(60);

receipt.append(LINE_SEPARATOR).append("\n");
receipt.append("RAION MOTORS\n");
receipt.append("Electric Vehicle Order Receipt\n");
receipt.append(LINE_SEPARATOR).append("\n");
```

The receipts look professional and are easy to read. Customers could actually use these for their records.

### Flexible Image Path Resolution

The system builds image paths dynamically and falls back gracefully:

```java
public static String getImagePath(int level, VehicleColor color, String view) {
    String colorFolder = color.name().toLowerCase();
    return BASE_IMAGE_PATH + "/level" + level + "/" + 
           colorFolder + "/" + view + ".png";
}
```

If an image is missing, the frontend shows a placeholder. The app doesn't break.

## Design Philosophy

### User Experience First

The interface prioritizes clarity over cleverness. Every interaction is predictable. The price updates immediately when you change something. The payment calculator responds in real-time. No surprises, no hidden complexity.

Tab navigation separates two distinct user journeys: building from scratch or choosing a signature model. This prevents decision fatigue. Customers who want control can customize everything. Customers who want speed can pick a pre-configured package.

### Performance Matters

The frontend uses vanilla JavaScript with no framework overhead. The entire application loads in under a second on a decent connection. API calls are optimized - the backend only sends what the frontend needs. Images lazy-load and fall back to placeholders if missing.

### Visual Design

Clean lines, subtle shadows, and a restrained color palette. The interface looks professional without being boring. Animations are quick (150-350ms) and purposeful. Nothing bounces or slides unnecessarily. The design language draws from Tesla and Apple - minimalist, confident, and focused on the product.

Typography uses system fonts for optimal rendering across devices. The layout adapts smoothly from desktop to mobile. Nothing feels cramped on small screens or wasted on large ones.

### Code Principles

The backend is organized around domain concepts. Each class has a single, clear responsibility. Methods are short and focused. Variable names explain themselves. Comments explain why, not what.

The API follows REST conventions. Endpoints are predictable. Error messages are helpful. CORS is configured for easy frontend integration.

The frontend keeps state management simple. No complex state machines or reactive frameworks. Just plain objects that get updated when things change. Event handlers are attached once during initialization. No memory leaks, no performance degradation over time.

## Project Stats

- Lines of code: ~4,500+
- Java classes: 24
- REST endpoints: 11
- Vehicle configurations: 100+ possible combinations
- Signature vehicles: 4 pre-configured
- Required images: 48 (4 views per color per level)

## Roadmap

### Planned Features

**User Accounts**
Login system for saved configurations. Customers could come back later and pick up where they left off. Also enables configuration sharing via links.

**Comparison Tool**
Side-by-side comparison of different models or configurations. Helps customers make informed decisions without juggling multiple browser tabs.

**3D Visualization**
Interactive 360-degree vehicle viewer. More engaging than static images and gives customers a better sense of the vehicle's proportions.

**Virtual Test Drive**
AR experience that shows the vehicle in the customer's driveway or garage. Helps with size visualization and purchase confidence.

**Live Inventory**
Real-time availability checking against actual dealer inventory. Prevents customers from configuring something that's months away from delivery.

**Finance Integration**
Connect to lending partners for real loan approvals during the configuration process. Removes uncertainty about whether the customer can actually afford their build.

### Technical Improvements

**Database Integration**
Move from file-based receipts to PostgreSQL. Enables order history, customer accounts, and better reporting.

**Automated Testing**
JUnit tests for all business logic. Integration tests for API endpoints. Prevents regressions when adding new features.

**Containerization**
Docker setup for consistent deployment across environments. Makes it easy to run the application anywhere.

**Admin Dashboard**
Internal tool for managing inventory, updating prices, adding new options. Currently these changes require code modifications.

**Analytics**
Track which configurations are popular, where customers drop off, which features drive conversions. Data-driven product decisions.

**Payment Processing**
Stripe or PayPal integration for deposits. Takes the configurator from "quote" to "purchase".

## About This Project

Raion Motors was built to demonstrate what a modern EV configurator should be. Fast, clean, and focused on the customer experience. The technology choices were deliberate - Spring Boot for the backend because it's production-ready and scalable, vanilla JavaScript for the frontend because frameworks add complexity that isn't needed here.

The design takes inspiration from Tesla's configurator and Apple's product pages. Both companies understand that buying a high-value product online requires clarity and confidence. The interface removes friction, the pricing is transparent, and the calculations happen instantly.

This isn't just a portfolio piece. The architecture is sound enough to run a real business. Add a payment processor, connect it to inventory management, and you'd have a functional e-commerce platform for electric vehicles.

## Technical Credits

Built with:
- Spring Boot for robust backend services
- Java 17 for modern language features
- Vanilla JavaScript for lightweight frontend
- Professional design inspired by industry leaders

The receipt generation system uses standard business formatting. The payment calculator uses industry-standard amortization formulas. The API follows REST best practices. Every technical decision was made with production deployment in mind.

## License

This project is available for educational and portfolio purposes. Feel free to use it as a reference for your own work.

## Contact

Questions or collaboration opportunities:
- GitHub: [Your Profile]
- LinkedIn: [Your Profile]
- Email: [Your Email]

---

**Raion Motors - Where Performance Meets Precision**
