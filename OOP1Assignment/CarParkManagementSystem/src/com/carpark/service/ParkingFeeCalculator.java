package com.carpark.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.carpark.model.Vehicle;

public interface ParkingFeeCalculator {
    double calculateFee(Vehicle vehicle, LocalDateTime exitTime);

    // Default helper: ceil to full hours, minimum 1 hour
    default long roundUpHours(Duration duration) {
        return Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));
    }

    // Static helper: consistent currency formatting
    static String format(double fee) {
        return String.format("EUR %.2f", fee);
    }
}
