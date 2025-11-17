package com.carpark.model;

public final class Van extends Vehicle {

    public Van(String registrationNo, String ownerName) {
        super(registrationNo, ownerName, "Van");
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
