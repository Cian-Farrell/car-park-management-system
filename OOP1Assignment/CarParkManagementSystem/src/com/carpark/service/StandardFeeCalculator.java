package com.carpark.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.carpark.model.Vehicle;

public class StandardFeeCalculator implements ParkingFeeCalculator {

    @Override
    public double calculateFee(Vehicle vehicle, LocalDateTime exitTime) {
        // TODO Auto-generated method stub
        // Calculate the time difference between entry and exit times
        var duration = Duration.between(vehicle.getEntryTime(), exitTime);
        var minutes = duration.toMinutes();

        // Convert the hours (round up to the next hour)
        long hours = (minutes + 59) / 60; // Round up to the next hour

        if (hours < 1) {
            hours = 1; // Minimum charge for 1 hour
        }

        // Mulitply by rate per hour
        double rate = vehicle.getRatePerHour();
        return hours * rate;
    }

}
