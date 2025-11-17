package com.carpark.model;

public enum VehicleType {
    CAR("Car", 2.50),
    VAN("Van", 3.50),
    MOTORCYCLE("Motorcycle", 1.50);
    
    private final String displayName;
    private final double ratePerHour;

    VehicleType(String displayName, double ratePerHour) {
        this.displayName = displayName;
        this.ratePerHour = ratePerHour;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
