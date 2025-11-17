package com.carpark.model;

import java.time.LocalDateTime;

public sealed abstract class Vehicle permits Car, Van, Motorcycle {

    private String registrationNo;
    private String ownerName;
    private String type;
    private LocalDateTime entryTime;

    public Vehicle(String registrationNo, String ownerName, String type) {

        if (registrationNo == null || registrationNo.isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }

        this.registrationNo = registrationNo;
        this.ownerName = ownerName;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public abstract double getRatePerHour();

    @Override
    public String toString() {
        return "Vehicle{" +
                "registrationNo='" + registrationNo + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", type='" + type + '\'' +
                ", entryTime=" + entryTime +
                '}';
    }
}
