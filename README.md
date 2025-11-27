# Car Park Management System (OOP1 Assignment)

This project is a Java-based console application developed for the OOP1 module.  
It demonstrates core Object-Oriented Programming principles as well as modern Java features introduced in Java 17+.

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

---

## Object-Oriented Design

This project demonstrates:

### Sealed classes
- `Vehicle` is a **sealed** base class  
- `Car`, `Van`, `Motorcycle` are **final** subclasses

### Polymorphism & overriding
Each subclass overrides `getRatePerHour()`.

### Interface + implementation
- `ParkingFeeCalculator` (interface)
- `StandardFeeCalculator` (implementation)
- Includes **static** and **default** methods

### Modern Java Features
- **switch expressions** for selecting vehicle type
- **LVTI** (`var`) for cleaner variable declarations
- **varargs** in `printVehiclesByType(...)`
- **records** for `Ticket`
- **method overloading** (`addVehicle()` used in 3 forms)
- **trimmed + validated console input**

---

## Project Structure
src/
└── com.carpark/
├── model/
│ Vehicle (sealed)
│ Car, Van, Motorcycle
│ VehicleType (enum)
│ Ticket (record)
└── service/
CarPark (main flow + menu)
ParkingFeeCalculator (interface)
StandardFeeCalculator (implementation)

