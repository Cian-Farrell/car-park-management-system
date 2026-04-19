package com.carpark.model;

public final class Van extends Vehicle {

    // JEP 513 — Flexible Constructor Bodies (Java 25)
    // Before Java 25, super() had to be the very first line in a constructor
    // JEP 513 allows statements before super() as long as 'this' is not accessed
    // Here we validate and trim the registration number before passing to the parent
    public Van(String registrationNo, String ownerName) {
        String trimmed = registrationNo.trim().toUpperCase();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be blank.");
        }
        super(trimmed, ownerName, "Van");
    }

    @Override
    public double getRatePerHour() {
        return 3.50; // rate per hour for a van
    }

    @Override
    public String toString() {
        return String.format(
            "------------------------------\n" +
            "         VAN DETAILS\n" +
            "------------------------------\n" +
            "Registration: %s\n" +
            "Owner:        %s\n" +
            "Type:         %s\n" +
            "Entry Time:   %s\n" +
            "------------------------------",
            getRegistrationNo(),
            getOwnerName(),
            getType(),
            getEntryTime().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        );
    }

}
