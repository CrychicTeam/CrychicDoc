package com.mna.api.affinity;

public enum Affinity {

    ARCANE,
    EARTH,
    ENDER,
    FIRE,
    WATER,
    WIND,
    HELLFIRE,
    ICE,
    LIGHTNING,
    UNKNOWN;

    private static int[] COLOR_ARCANE = new int[] { 95, 24, 125 };

    private static int[] COLOR_EARTH = new int[] { 87, 54, 9 };

    private static int[] COLOR_ENDER = new int[] { 49, 2, 51 };

    private static int[] COLOR_FIRE = new int[] { 219, 106, 20 };

    private static int[] COLOR_LIGHTNING = new int[] { 132, 163, 176 };

    private static int[] COLOR_WATER = new int[] { 57, 76, 227 };

    private static int[] COLOR_ICE = new int[] { 134, 213, 240 };

    private static int[] COLOR_WIND = new int[] { 190, 204, 209 };

    private static int[] COLOR_HELLFIRE = new int[] { 46, 123, 59 };

    private static int[] COLOR_UNKNOWN = new int[] { 255, 255, 255 };

    private static int[] COLOR_ARCANE_SECONDARY = new int[] { 77, 133, 207 };

    private static int[] COLOR_EARTH_SECONDARY = new int[] { 87, 54, 9 };

    private static int[] COLOR_ENDER_SECONDARY = new int[] { 49, 2, 51 };

    private static int[] COLOR_FIRE_SECONDARY = new int[] { 219, 106, 20 };

    private static int[] COLOR_LIGHTNING_SECONDARY = new int[] { 132, 163, 176 };

    private static int[] COLOR_WATER_SECONDARY = new int[] { 57, 76, 227 };

    private static int[] COLOR_ICE_SECONDARY = new int[] { 134, 213, 240 };

    private static int[] COLOR_WIND_SECONDARY = new int[] { 190, 204, 209 };

    private static int[] COLOR_HELLFIRE_SECONDARY = new int[] { 20, 79, 31 };

    private static int[] COLOR_UNKNOWN_SECONDARY = new int[] { 0, 0, 0 };

    public Affinity getOpposite() {
        switch(this) {
            case ARCANE:
                return ENDER;
            case EARTH:
                return WIND;
            case ENDER:
                return ARCANE;
            case FIRE:
                return WATER;
            case WATER:
                return FIRE;
            case WIND:
                return EARTH;
            case HELLFIRE:
                return WATER;
            case ICE:
                return FIRE;
            case LIGHTNING:
                return WATER;
            default:
                return UNKNOWN;
        }
    }

    public int[] getColor() {
        switch(this) {
            case ARCANE:
                return COLOR_ARCANE;
            case EARTH:
                return COLOR_EARTH;
            case ENDER:
                return COLOR_ENDER;
            case FIRE:
                return COLOR_FIRE;
            case WATER:
                return COLOR_WATER;
            case WIND:
                return COLOR_WIND;
            case HELLFIRE:
                return COLOR_HELLFIRE;
            case ICE:
                return COLOR_ICE;
            case LIGHTNING:
                return COLOR_LIGHTNING;
            default:
                return COLOR_UNKNOWN;
        }
    }

    public int[] getSecondaryColor() {
        switch(this) {
            case ARCANE:
                return COLOR_ARCANE_SECONDARY;
            case EARTH:
                return COLOR_EARTH_SECONDARY;
            case ENDER:
                return COLOR_ENDER_SECONDARY;
            case FIRE:
                return COLOR_FIRE_SECONDARY;
            case WATER:
                return COLOR_WATER_SECONDARY;
            case WIND:
                return COLOR_WIND_SECONDARY;
            case HELLFIRE:
                return COLOR_HELLFIRE_SECONDARY;
            case ICE:
                return COLOR_ICE_SECONDARY;
            case LIGHTNING:
                return COLOR_LIGHTNING_SECONDARY;
            default:
                return COLOR_UNKNOWN_SECONDARY;
        }
    }

    public Affinity getShiftAffinity() {
        switch(this) {
            case HELLFIRE:
                return FIRE;
            case ICE:
                return WATER;
            case LIGHTNING:
                return FIRE;
            default:
                return this;
        }
    }
}