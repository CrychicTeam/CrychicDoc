package com.almostreliable.ponderjs.util;

import net.minecraft.world.item.DyeColor;

public class DyeColorWrapper {

    public final DyeColor mcColor;

    public DyeColorWrapper(DyeColor color) {
        this.mcColor = color;
    }

    public static DyeColorWrapper get(String name) {
        return new DyeColorWrapper(DyeColor.byName(name, null));
    }

    public static DyeColorWrapper byId(int id) {
        return new DyeColorWrapper(DyeColor.byId(id));
    }

    public int getId() {
        return this.mcColor.getId();
    }

    public String getName() {
        return this.mcColor.getName();
    }

    public String getSerializedName() {
        return this.mcColor.getSerializedName();
    }

    public int getColorValue() {
        return this.mcColor.getTextColor();
    }
}