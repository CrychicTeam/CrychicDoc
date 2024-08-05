package net.minecraftforge.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class PlantType {

    private static final Pattern INVALID_CHARACTERS = Pattern.compile("[^a-z_]");

    private static final Map<String, PlantType> VALUES = new ConcurrentHashMap();

    public static final PlantType PLAINS = get("plains");

    public static final PlantType DESERT = get("desert");

    public static final PlantType BEACH = get("beach");

    public static final PlantType CAVE = get("cave");

    public static final PlantType WATER = get("water");

    public static final PlantType NETHER = get("nether");

    public static final PlantType CROP = get("crop");

    private final String name;

    public static PlantType get(String name) {
        return (PlantType) VALUES.computeIfAbsent(name, e -> {
            if (INVALID_CHARACTERS.matcher(e).find()) {
                throw new IllegalArgumentException("PlantType.get() called with invalid name: " + name);
            } else {
                return new PlantType(e);
            }
        });
    }

    private PlantType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}