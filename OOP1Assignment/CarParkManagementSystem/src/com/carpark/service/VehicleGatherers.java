package com.carpark.service;

import com.carpark.model.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Gatherer;

// Stream Gatherers (Java 25)
public class VehicleGatherers {

    public static Gatherer<Vehicle, Map<String, Integer>, Vehicle> firstNPerType(int n) {

        return Gatherer.ofSequential(
                // Initialiser: creates the state — a map to count vehicles per type
                HashMap::new,

                // Integrator: processes each element
                // state = the map, element = current vehicle, downstream = next operation
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
                    String type = element.getType();

                    // Get current count for this type, default to 0
                    int count = state.getOrDefault(type, 0);

                    // Only pass the element downstream if we haven't hit the limit yet
                    if (count < n) {
                        state.put(type, count + 1);
                        downstream.push(element);
                    }

                    return true; // continue processing
                })
        );
    }
}
