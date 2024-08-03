package me.jellysquid.mods.lithium.common.compat.worldedit;

import net.minecraftforge.fml.loading.LoadingModList;

public class WorldEditCompat {

    public static final boolean WORLD_EDIT_PRESENT = LoadingModList.get().getModFileById("worldedit") != null;
}