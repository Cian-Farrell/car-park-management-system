package com.carpark.service;

import java.util.*;

import com.carpark.model.Vehicle;
import com.carpark.model.Car;
import com.carpark.model.Van;
import com.carpark.model.Motorcycle;
import com.carpark.model.Ticket;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CarPark {

    static Scanner scanner = new Scanner(System.in);
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private int capacity = 50;

    // Supplier<T> takes no arguments and returns a value when .get() is called
    // It produces a StandardFeeCalculator instance
    // StandardFeeCalculator::new is shorthand for () -> new StandardFeeCalculator()
    private final Supplier<ParkingFeeCalculator> calculatorSupplier = StandardFeeCalculator::new;
    private ParkingFeeCalculator feeCalculator = calculatorSupplier.get();

    private ArrayList<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        CarPark carPark = new CarPark();
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Car Park Management System");
            System.out.println("1. Add a Vehicle");
            System.out.println("2. Remove a Vehicle");
            System.out.println("3. View Parked Vehicles");
            System.out.println("4. View Parking Tickets");
            System.out.println("5. Vehicle Reports");
            System.out.println("6. Revenue Statistics");
            System.out.println("7. Exit");
            System.out.print("Please select an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline
                switch (choice) {
                    case 1 -> carPark.addVehicle(); // interactive
                    case 2 -> carPark.removeVehicle();
                    case 3 -> carPark.viewParkedVehicles();
                    case 4 -> carPark.viewParkingTickets();
                    case 5 -> carPark.vehicleReports();
                    case 6 -> carPark.revenueStatistics();
                    case 7 -> {
                        System.out.println("Thank you for using the Car Park Management System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        scanner.close();
    }

    // Submenu for revenue statistics
    // Each option calls a dedicated method demonstrating a specific stream/collector feature
    public void revenueStatistics() {
        System.out.println("\n--- Revenue Statistics ---");
        System.out.println("1. Vehicle count grouped by type");
        System.out.println("2. Revenue summary by vehicle type");
        System.out.println("3. Find most expensive ticket");
        System.out.print("Select a report: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> countVehiclesByType();
                case 2 -> revenueSummaryByType();
                case 3 -> findMostExpensiveTicket();
                default -> System.out.println("Invalid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
    }

    // map() transforms each element from one type to another — intermediate operation
    // Here it converts each Vehicle object into just its registration number String
    // The stream changes from Stream<Vehicle> to Stream<String> after map()
    // sorted() then sorts the resulting strings alphabetically — intermediate operation
    // forEach() prints each string — terminal operation
    public void viewAllRegistrations(){
        System.out.println("\nAll parked vehicle registration numbers (sorted alphabetically):");

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles currently parked.");
            return;
        }

        vehicles.stream()
                .map(Vehicle::getRegistrationNo) // extract registration number
                .sorted() // sort alphabetically
                .forEach(reg -> System.out.println(" - " + reg)); // print each registration
    }

    // sorted() orders the stream by entry time — intermediate operation
    // limit() truncates the stream to the first N elements — intermediate operation
    // forEach() prints each remaining element — terminal operation
    public void viewFirstNParkedVehicles(){
        System.out.println("\nEnter number of parked vehicles to view (e.g., 5): ");
        int n;

        try {
            n = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return;
        }

        System.out.println("\nFirst " + n + " parked vehicles (sorted by entry time):");

        vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getEntryTime))
                .limit(n)
                .forEach(printVehicle);
    }

    // map() transforms each element in the stream to something else — intermediate operation
    // Here it extracts the type String from each Vehicle
    // distinct() removes duplicate values from the stream — intermediate operation
    // collect() gathers the unique results into a List — terminal operation
    public void viewDistinctVehicleTypes() {
        System.out.println("\nDistinct vehicle types currently parked:");

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles currently parked.");
            return;
        }

        List<String> types = vehicles.stream()
                .map(Vehicle::getType)
                .distinct()
                .collect(Collectors.toList()); //gathers into a list

        types.forEach(type -> System.out.println(" - " + type));
    }

    // filter() narrows the stream to matching elements — intermediate operation
    // findFirst() returns the first element that made it through the filter — terminal operation
    // Returns Optional<Vehicle> since there may be no match
    public void findFirstVehicleByRegPrefix(){
        System.out.println("\nEnter registration prefix to search for (e.g., 'AB'): ");
        String prefix = scanner.nextLine().trim();

        Optional<Vehicle> found = vehicles.stream()
                .filter(v -> v.getRegistrationNo().toUpperCase().startsWith(prefix.toUpperCase()))
                .findFirst();

        found.ifPresentOrElse(
                printVehicle::accept,
                () -> System.out.println("No vehicles found with registration starting with " + prefix)
        );
    }

    // Step 1 uses toMap() — a collector that builds a Map from a stream
    // Two Functions are provided: one for the key, one for the value
    // Step 2 uses groupingBy() + summingDouble() to total fees per vehicle type
    // summingDouble() is a downstream collector that sums a double field across each group
    private void revenueSummaryByType() {
        System.out.println("\nRevenue summary by vehicle type:");

        if (tickets.isEmpty()) {
            System.out.println("No tickets issued yet, so no revenue data available.");
            return;
        }

        Map<String, String> regToType = vehicles.stream().collect(Collectors.toMap(Vehicle::getRegistrationNo, Vehicle::getType));

        Map<String, Double> revenueByType = tickets.stream().collect(
                Collectors.groupingBy(
                        ticket -> regToType.getOrDefault(ticket.registrationNo(), "Unknown"),
                        Collectors.summingDouble(Ticket::parkingFee)
                ));

        double total = 0;
        for (Map.Entry<String, Double> entry : revenueByType.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + ParkingFeeCalculator.format(entry.getValue()));
            total += entry.getValue();
        }
        System.out.println("Total Revenue: " + ParkingFeeCalculator.format(total));
    }

    // Function<T, R> takes one argument and returns a value
    // Used here to extract the vehicle type as the grouping key
    // groupingBy() is a collector that groups elements by the Function's result
    // counting() is a downstream collector that counts how many elements are in each group
    // Returns Map<String, Long> — key is vehicle type, value is the count
    private void countVehiclesByType() {
        System.out.println("\nVehicle count grouped by type:");

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles currently parked.");
            return;
        }

        Function<Vehicle, String> byType = Vehicle::getType;

        Map<String, Long> countByType = vehicles.stream().collect(Collectors.groupingBy(byType, Collectors.counting()));

        countByType.forEach((type, count) -> System.out.println("  " + type + ": " + count + " vehicle(s)"));
    }

    // Menu options for vehicle reports
    // Each option calls a dedicated method that shows a specific stream/lambda feature
    private void vehicleReports() {
        System.out.println("\n--- Vehicle Reports ---");
        System.out.println("1. View vehicles sorted by entry time");
        System.out.println("2. Filter vehicles by type");
        System.out.println("3. Find longest-parked vehicle");
        System.out.println("4. Check if a vehicle type is present");
        System.out.println("5. Partition vehicles: Vans vs Non-Vans");
        System.out.println("6. Find vehicle by registration prefix");
        System.out.println("7. View distinct vehicle types");
        System.out.println("8. View first N parked vehicles");
        System.out.println("9. View all registration numbers");
        System.out.println("10. Inspect a vehicle");
        System.out.print("Select a report: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> viewVehiclesSortedByEntryTime();
                case 2 -> filterVehiclesByType();
                case 3 -> findLongestParkedVehicle();
                case 4 -> checkVehicleTypePresent();
                case 5 -> partitionVanVsNonVan();
                case 6 -> findFirstVehicleByRegPrefix();
                case 7 -> viewDistinctVehicleTypes();
                case 8 -> viewFirstNParkedVehicles();
                case 9 -> viewAllRegistrations();
                case 10 -> inspectVehicle();
                default -> System.out.println("Invalid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
    }

    // Switch expression with pattern matching — checks the type AND binds to a pattern variable
    // in a single step, removing the need for an explicit cast
    // No default needed — Vehicle is sealed so the compiler knows all possible subtypes
    // Combines with filter() + findFirst() to locate the vehicle by registration
    public void inspectVehicle() {
        System.out.print("Enter registration number to inspect: ");
        String reg = scanner.nextLine().trim();

        Optional<Vehicle> found = vehicles.stream()
                .filter(v -> v.getRegistrationNo().equalsIgnoreCase(reg))
                .findFirst();

        found.ifPresentOrElse(vehicle -> {
                    System.out.println("\nInspecting vehicle: " + vehicle.getRegistrationNo());

                    String description = switch (vehicle) {
                        case Car car         -> "Type: Car | Rate: EUR " + car.getRatePerHour()
                                + " | Standard passenger vehicle.";
                        case Van van         -> "Type: Van | Rate: EUR " + van.getRatePerHour()
                                + " | Larger vehicle — takes up more space.";
                        case Motorcycle moto -> "Type: Motorcycle | Rate: EUR " + moto.getRatePerHour()
                                + " | Compact vehicle — discounted rate.";
                    };

                    System.out.println("  " + description);
                },
                () -> System.out.println("No vehicle found with registration '" + reg + "'."));
    }

    // Collectors.partitioningBy() splits the stream into exactly two groups — true and false
    // The Predicate determines which group each element goes into
    // Returns Map<Boolean, List<Vehicle>> — true = Vans, false = everything else
    private void partitionVanVsNonVan() {
        System.out.println("\nPartitioning vehicles: Vans vs Non-Vans");

        Map<Boolean, List<Vehicle>> partitioned = vehicles.stream()
                .collect(Collectors.partitioningBy(v -> v.getType().equalsIgnoreCase("Van")));

        List<Vehicle> vans = partitioned.get(true);
        List<Vehicle> nonVans = partitioned.get(false);

        System.out.println("\nVans (" + vans.size() + "):");
        if (vans.isEmpty()) {
            System.out.println("No vans currently parked.");
        } else {
            vans.forEach(printVehicle);
        }

        System.out.println("\nVans (" + nonVans.size() + "):");
        if (nonVans.isEmpty()) {
            System.out.println("No vans currently parked.");
        } else {
            nonVans.forEach(printVehicle);
        }
    }

    // anyMatch() is a terminal operation — returns true if any element matches the Predicate
    // Short-circuits: stops as soon as the first match is found, doesn't process the whole list
    // Predicate<Vehicle> is reused here — same concept as filterVehiclesByType()
    private void checkVehicleTypePresent() {
        System.out.println("\nEnter vehicle type to check for (Car, Van, Motorcycle): ");
        String input = scanner.nextLine().trim();

        Predicate<Vehicle> typeCheck = v -> v.getType().equalsIgnoreCase(input);

        boolean present = vehicles.stream().anyMatch(typeCheck);

        if (present){
            System.out.println("Yes, there is at least one " + input + " currently parked.");
        } else {
            System.out.println("No " + input + "s found in the car park.");
        }
    }

    // min() is a terminal operation — returns the smallest element based on the Comparator
    // The vehicle with the earliest entryTime has been parked the longest so min() is used
    // Returns Optional<Vehicle> since the list could be empty
    private void findLongestParkedVehicle() {
        System.out.println("\nLongest parked vehicle: ");

        Optional<Vehicle> longestParked = vehicles.stream().min(Comparator.comparing(Vehicle::getEntryTime));

        longestParked.ifPresentOrElse(
                v -> {
                    printVehicle.accept(v); // calling Consumer<Vehicle> directly
                    long minutes = java.time.Duration.between(v.getEntryTime(), LocalDateTime.now()).toMinutes();
                    System.out.println("   Time parked: " + minutes + " minute(s)");
                },
                () -> System.out.println("No vehicles currently parked.")
        );
    }

    // Predicate<T> takes one argument and returns a boolean
    // Used here to test whether a vehicle's type matches the user's input
    // filter() is an intermediate operation — only elements where the Predicate returns true pass through
    // collect() is the terminal operation — gathers the filtered results into a new List
    private void filterVehiclesByType() {
        System.out.println("Enter vehicle type to filter by (Car, Van, Motorcycle): ");
        String input = scanner.nextLine().trim();

        Predicate<Vehicle> typeFilter = v -> v.getType().equalsIgnoreCase(input);

        List<Vehicle> filtered = vehicles.stream().filter(typeFilter).collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No vehicles of type " + input + " found.");
        } else {
            System.out.println("Vehicles of type " + input + ":");
            filtered.forEach(printVehicle);
        }
    }

    // Comparator.comparing() extracts the entryTime field from each Vehicle to sort by
    // sorted() is an intermediate operation — it doesn't trigger the stream on its own
    // forEach() is the terminal operation — it triggers the stream and prints each vehicle
    // printVehicle is the Consumer<Vehicle> we defined earlier — reused here
    private void viewVehiclesSortedByEntryTime() {
        System.out.println("\nVehicles sorted by entry time (oldest first)");

        if (vehicles.isEmpty()){
            System.out.println("No vehicles currently parked.");
            return;
        }

        vehicles.stream().sorted(Comparator.comparing(Vehicle::getEntryTime)).forEach(printVehicle);
    }

    // ===============================
    // Interactive method (menu option)
    // ===============================
    public void addVehicle() {
        // Collect owner name
        String ownerName;
        while (true) {
            System.out.print("Please enter the vehicle's owner name: ");
            ownerName = scanner.nextLine().trim();
            if (ownerName.isEmpty()) {
                System.out.println("Owner name cannot be empty.");
            } else {
                break;
            }
        }

        // Collect registration number
        String registrationNo;
        while (true) {
            System.out.print("Please enter the vehicle's registration number: ");
            registrationNo = scanner.nextLine().trim();
            if (registrationNo.isEmpty()) {
                System.out.println("Registration number cannot be empty.");
            } else {
                break;
            }
        }

        // Validate vehicle type and create correct subclass
        Vehicle vehicle = null;
        while (vehicle == null) {
            System.out.print("Please enter the vehicle's type (Car, Van, Motorcycle): ");
            var typeInput = scanner.nextLine().trim().toLowerCase();

            vehicle = switch (typeInput) {
                case "car" -> new Car(registrationNo, ownerName);
                case "van" -> new Van(registrationNo, ownerName);
                case "motorcycle" -> new Motorcycle(registrationNo, ownerName);
                default -> {
                    System.out.println("Invalid vehicle type. Please enter Car, Van, or Motorcycle.");
                    yield null;
                }
            };
        }

        // Delegate to overloaded method that centralizes validation + insert
        addVehicle(vehicle);
    }

    // ===========================================
    // Overload #1: add by object (validations here)
    // ===========================================
    public void addVehicle(Vehicle vehicle) {
        // Capacity check
        if (vehicles.size() >= capacity) {
            System.out.println("Car park is full. Cannot add more vehicles.");
            return;
        }

        // Duplicate registration check
        for (Vehicle v : vehicles) {
            if (v.getRegistrationNo().equalsIgnoreCase(vehicle.getRegistrationNo())) {
                System.out.println("A vehicle with that registration already exists in the car park.");
                return;
            }
        }

        // Add and confirm
        vehicles.add(vehicle);
        System.out.println("Vehicle added successfully: " + vehicle.getRegistrationNo()
                + " (" + vehicle.getType() + ")");
        System.out.printf("%d out of %d spaces occupied.%n", vehicles.size(), capacity);
    }

    // ===================================================
    // Overload #2: add by parts (convenience/test method)
    // ===================================================
    public void addVehicle(String registrationNo, String ownerName, String type) {
        var v = switch (type.toLowerCase()) {
            case "car" -> new Car(registrationNo, ownerName);
            case "van" -> new Van(registrationNo, ownerName);
            case "motorcycle" -> new Motorcycle(registrationNo, ownerName);
            default -> null;
        };
        if (v == null) {
            System.out.println("Invalid type. Use Car/Van/Motorcycle.");
            return;
        }
        addVehicle(v); // reuse centralised validation
    }

    public void removeVehicle() {
        // Collect registration number to remove
        System.out.print("Please enter the registration number of the vehicle to remove: ");
        var regToRemove = scanner.nextLine().trim();

        // Validate input
        if (regToRemove.isEmpty()) {
            System.out.println("Registration number cannot be empty.");
            return;
        }

        // Find vehicle by registration number
        Vehicle vehicle = null;
        for (var v : vehicles) {
            if (v.getRegistrationNo().equalsIgnoreCase(regToRemove)) {
                vehicle = v;
                break;
            }
        }

        // If vehicle not found
        if (vehicle == null) {
            System.out.println("Vehicle with registration number " + regToRemove + " not found.");
            return;
        }

        // Calculate parking fee (use the same exitTime for display + fee)
        var exitTime = LocalDateTime.now();
        var fee = feeCalculator.calculateFee(vehicle, exitTime);

        // (Optional) If you want to display time parked here:
        var parkedMinutes = java.time.Duration.between(vehicle.getEntryTime(), exitTime).toMinutes();
        var parkedHours = Math.max(1, (long) Math.ceil(parkedMinutes / 60.0));

        // Create and store ticket
        Ticket ticket = new Ticket(
                vehicle.getRegistrationNo(),
                vehicle.getEntryTime(),
                exitTime,
                fee);
        tickets.add(ticket);

        System.out.println();
        System.out.println(ticket);
        System.out.println("Time Parked: " + parkedHours + " hour(s)");
        System.out.println();

        System.out.println("Total Fee: " + ParkingFeeCalculator.format(fee));

        // Remove vehicle
        vehicles.remove(vehicle);
        System.out.println("Vehicle removed successfully: " + vehicle.getRegistrationNo()
                + " (" + vehicle.getType() + ")");
    }

    public void viewParkedVehicles() {
        System.out.println("Parked Vehicles:");

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles parked.");
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }

        // Always display occupancy summary at the end
        System.out.printf("%d out of %d spaces occupied.%n", vehicles.size(), capacity);
    }

    public void viewParkingTickets() {
        System.out.println("Parking Tickets Issued:");

        if (tickets.isEmpty()) {
            System.out.println("No parking tickets issued.");
        } else {
            for (Ticket ticket : tickets) {
                System.out.println(ticket);
            }
        }

        System.out.printf("Total tickets issued: %d%n", tickets.size());
    }

    // Varargs helper: print vehicles matching one or more type names (e.g., "Car", "Van")
    public void printVehiclesByType(String... types) {
        if (types == null || types.length == 0) {
            System.out.println("No types provided.");
            return;
        }
        var wanted = java.util.Set.of(types); // LVTI used here
        System.out.println("\nVehicles matching: " + wanted);
        for (var v : vehicles) { // LVTI used here
            if (wanted.contains(v.getType())) {
                System.out.println(v);
            }
        }
    }

    // Stream max() terminal operation
    // Comparator.comparing() extracts the parkingFee field from each Ticket to be compared
    // Returns Optional<Ticket> — handles the case where no tickets have been issued yet
    public void findMostExpensiveTicket(){
        System.out.println("\nMost expensive parking ticket: ");

        Optional<Ticket> mostExpensive = tickets.stream().max(Comparator.comparing(Ticket::parkingFee));

        mostExpensive.ifPresentOrElse(ticket -> System.out.println(ticket), () -> System.out.println("No tickets issued yet"));
    }

    // Consumer<T> takes one argument and returns nothing
    // Used here as a reusable action to print a single vehicle to the console
    // Instead of writing a print loop every time, we just call printVehicle.accept(v)
    private final Consumer<Vehicle> printVehicle = vehicle -> System.out.println(" - [" + vehicle.getType() + "] "
            + vehicle.getRegistrationNo()
            + " | Owner: " + vehicle.getOwnerName()
            + " | Entry: " + vehicle.getEntryTime());
}
