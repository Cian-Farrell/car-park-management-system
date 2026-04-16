package com.carpark.service;

import java.util.*;

import com.carpark.model.Vehicle;
import com.carpark.model.Car;
import com.carpark.model.Van;
import com.carpark.model.Motorcycle;
import com.carpark.model.Ticket;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    // Menu options for vehicle reports
    // Each option calls a dedicated method that shows a specific stream/lambda feature
    private void vehicleReports() {
        System.out.println("\n--- Vehicle Reports ---");
        System.out.println("1. View vehicles sorted by entry time");
        System.out.println("2. Filter vehicles by type");
        System.out.println("3. Find longest-parked vehicle");
        System.out.println("4. Check if a vehicle type is present");
        System.out.println("5. Partition vehicles: Vans vs Non-Vans");
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
                default -> System.out.println("Invalid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
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
