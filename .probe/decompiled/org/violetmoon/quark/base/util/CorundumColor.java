package org.violetmoon.quark.base.util;

import net.minecraft.world.level.material.MapColor;

public enum CorundumColor {

    RED("red", 16711680, MapColor.COLOR_RED),
    ORANGE("orange", 16744448, MapColor.COLOR_ORANGE),
    YELLOW("yellow", 16776960, MapColor.COLOR_YELLOW),
    GREEN("green", 65280, MapColor.COLOR_GREEN),
    BLUE("blue", 65535, MapColor.COLOR_LIGHT_BLUE),
    INDIGO("indigo", 255, MapColor.COLOR_BLUE),
    VIOLET("violet", 16711935, MapColor.COLOR_MAGENTA),
    WHITE("white", 16777215, MapColor.SNOW),
    BLACK("black", 0, MapColor.COLOR_BLACK);

    public final String name;

    public final int beaconColor;

    public final MapColor mapColor;

    private CorundumColor(String name, int beaconColor, MapColor mapColor) {
        this.name = name;
        this.beaconColor = beaconColor;
        this.mapColor = mapColor;
    }
}