package net.blay09.mods.waystones.api;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum WaystoneOrigin implements StringRepresentable {

    UNKNOWN, WILDERNESS, DUNGEON, VILLAGE, PLAYER;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}