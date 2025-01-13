package com.example.common.utils;

import androidx.car.app.hardware.info.EnergyProfile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FuelTypeMapper {
    private static final Map<Integer, String> FUEL_TYPE_MAP = new HashMap<>();

    static {
        try {
            for (Field field : EnergyProfile.class.getDeclaredFields()) {
                if (field.getType() == int.class && field.getName().startsWith("FUEL_TYPE_")) {
                    int value = field.getInt(null);
                    String name = field.getName().substring("FUEL_TYPE_".length());
                    FUEL_TYPE_MAP.put(value, name);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize FUEL_TYPE_MAP", e);
        }
    }

    public static String fuelTypeAsString(int fuelType) {
        return FUEL_TYPE_MAP.getOrDefault(fuelType, "UNKNOWN");
    }
}

//Blok static jest specjalnym konstruktorem - kod w nim jest wywoływany w trakcie inicjalizacji klasy
// w trakcie ładowania przez JVM - wykonuje się tylko raz

