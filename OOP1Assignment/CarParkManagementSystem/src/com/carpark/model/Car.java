package com.carpark.model;

public final class Car extends Vehicle {

    // this(): chain to the 3-arg constructor in the same class
    public Car(String registrationNo, String ownerName) {
        this(registrationNo, ownerName, "Car");
    }

    // super(...): delegate to Vehicle; this.: call instance method
    public Car(String registrationNo, String ownerName, String type) {
        super(registrationNo, ownerName, type);
        this.validate(); // demonstrates this.
    }

    private void validate() {
        // method left intentionally blank, used to contrast this. and this()
    }

    @Override
    public double getRatePerHour() {
        return 2.50;
    }

    @Override
    public String toString() {
        return String.format(
            "------------------------------\n" +
            "         CAR DETAILS\n" +
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
