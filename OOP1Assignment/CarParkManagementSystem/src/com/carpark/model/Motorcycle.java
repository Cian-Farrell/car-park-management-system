package com.carpark.model;

public final class Motorcycle extends Vehicle {

    public Motorcycle(String registrationNo, String ownerName) {
        super(registrationNo, ownerName, "Motorcycle");
    }

    @Override
    public double getRatePerHour() {
        return 1.50; // rate per hour for a motorcycle
    }

    @Override
    public String toString() {
        return String.format(
            "------------------------------\n" +
            "     MOTORCYCLE DETAILS\n" +
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
