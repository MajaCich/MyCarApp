package com.example.common.utils;

import androidx.car.app.hardware.info.EnergyProfile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EVConnectorTypeMapper {

    private static final Map<Integer, String> CONNECTOR_TYPE_MAP = new HashMap<>();

    static {
        try {
            for (Field field : EnergyProfile.class.getDeclaredFields()) {
                if (field.getType() == int.class && field.getName().startsWith("EVCONNECTOR_TYPE_")) {
                    int value = field.getInt(null);
                    String name = field.getName().substring("EVCONNECTOR_TYPE_".length());
                    CONNECTOR_TYPE_MAP.put(value, name);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize CONNECTOR_TYPE_MAP", e);
        }
    }

    public static String evConnectorTypeAsString(int connectorType) {
        return CONNECTOR_TYPE_MAP.getOrDefault(connectorType, "UNKNOWN");
    }
}

