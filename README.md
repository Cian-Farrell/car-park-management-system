# Car Park Management System (OOP2 Assignment)

This project is a Java-based console application developed for the OOP2 module, extended from the OOP1 assignment.  
It demonstrates core Object-Oriented Programming principles, modern Java features from Java 17+, and advanced Java 25 preview features.

---

## Features

### Vehicle Management
- Add vehicles (Car, Van, Motorcycle)
- Prevent duplicates based on registration (case-insensitive, trimmed)
- Block new entries when the car park is full (capacity = 50)
- Remove vehicles by registration
- View current capacity (e.g., "12 out of 50 spaces occupied")

### Parking Fee & Ticket System
- Calculate fees based on vehicle type and time parked  
  (Rounded up to nearest hour; minimum 1 hour)
- Generate printable ticket (using a Java `record`)
- Store all generated tickets in memory
- View list of issued tickets
- Save and load tickets to/from file using NIO2

### Vehicle Reports
- Sort vehicles by entry time
- Filter vehicles by type
- Find the longest-parked vehicle
- Check if a vehicle type is present
- Partition vehicles into Vans vs Non-Vans
- Find a vehicle by registration prefix
- View distinct vehicle types
- View first N parked vehicles
- View all registration numbers
- Inspect a vehicle using pattern matching

### Revenue Statistics
- Count vehicles grouped by type
- Revenue summary grouped by vehicle type
- Find the most expensive ticket

---

## Object-Oriented Design

### Sealed Classes
- `Vehicle` is a **sealed** base class
- `Car`, `Van`, `Motorcycle` are **final** subclasses

### Polymorphism & Overriding
Each subclass overrides `getRatePerHour()`.

### Interface + Implementation
- `ParkingFeeCalculator` (interface)
- `StandardFeeCalculator` (implementation)
- Includes **static** and **default** methods

---

## Java Features Demonstrated

### OOP1 Fundamentals
- **Sealed classes** — `Vehicle` with final subclasses
- **Records** — `Ticket` as an immutable data structure
- **Switch expressions** — for selecting vehicle type
- **LVTI** (`var`) — for cleaner variable declarations
- **Varargs** — `printVehiclesByType(...)`
- **Method overloading** — `addVehicle()` in 3 forms
- **Enums** — `VehicleType` with rates and display names

### OOP2 — Streams & Lambdas
- **Consumer\<T\>** — reusable vehicle printer
- **Supplier\<T\>** — factory for fee calculator
- **Predicate\<T\>** — filtering and matching
- **Function\<T, R\>** — type extraction for grouping
- **Intermediate operations** — `filter()`, `sorted()`, `map()`, `distinct()`, `limit()`
- **Terminal operations** — `forEach()`, `collect()`, `min()`, `max()`, `anyMatch()`, `findFirst()`
- **Collectors** — `groupingBy()`, `partitioningBy()`, `toMap()`, `counting()`, `summingDouble()`
- **Comparator.comparing()** — sorting by field
- **Switch expression with pattern matching** — inspect vehicle by type

### OOP2 — Advanced Features
- **NIO2** — save and load tickets using `Files`, `Path`, `StandardOpenOption`
- **Localisation** — format fees and dates by `Locale` using `NumberFormat` and `DateTimeFormatter`
- **Concurrency** — concurrent fee calculation using `ExecutorService`, `Callable<T>`, `Future<T>`

### OOP2 — Java 25 Preview Features
- **JEP 512** — Instance main method (`void main()` in `App.java`)
- **JEP 513** — Flexible constructor bodies (statements before `super()` in `Car`, `Van`, `Motorcycle`)
- **Scoped Values** — immutable thread context using `ScopedValue` in `SessionContext`
- **Stream Gatherers** — custom stateful intermediate operation using `Gatherer` in `VehicleGatherers`

---

## Project Structure

```
src/
└── com.carpark/
    ├── App.java                        (JEP 512 instance main)
    ├── model/
    │   ├── Vehicle.java                (sealed abstract class)
    │   ├── Car.java                    (final subclass — JEP 513)
    │   ├── Van.java                    (final subclass — JEP 513)
    │   ├── Motorcycle.java             (final subclass — JEP 513)
    │   ├── VehicleType.java            (enum)
    │   └── Ticket.java                 (record)
    └── service/
        ├── CarPark.java                (main class — streams, lambdas, NIO2, concurrency)
        ├── ParkingFeeCalculator.java   (interface)
        ├── StandardFeeCalculator.java  (implementation)
        ├── LocalisationUtil.java       (Locale, NumberFormat, DateTimeFormatter)
        ├── SessionContext.java         (Java 25 Scoped Values)
        └── VehicleGatherers.java       (Java 25 Stream Gatherers)
```

---

## Requirements

- Java 25 (Preview features enabled)
- IntelliJ IDEA with `--enable-preview` flag set in run configuration

---

## Running the Application

Run from `App.java` with the `--enable-preview` flag enabled in your run configuration.

---

## GitHub

[https://github.com/Cian-Farrell/car-park-management-system](https://github.com/Cian-Farrell/car-park-management-system)
