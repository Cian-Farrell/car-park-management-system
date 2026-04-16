package com.carpark.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public record Ticket(
    String registrationNo,
    LocalDateTime entryTime,
    LocalDateTime exitTime,
    double parkingFee
) {

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format("""
                ================================
                      PARKING TICKET
                ================================
                Registration: %s
                --------------------------------
                Entry Time:  %s
                Exit Time:   %s
                --------------------------------
                Parking Fee: EUR %.2f
                ================================""",
                registrationNo,
                entryTime.format(fmt),
                exitTime.format(fmt),
                parkingFee);
    }
}
