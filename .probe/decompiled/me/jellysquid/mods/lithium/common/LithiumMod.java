package me.jellysquid.mods.lithium.common;

import me.jellysquid.mods.lithium.common.config.LithiumConfig;
import net.minecraftforge.fml.common.Mod;

@Mod("radium")
public class LithiumMod {

    public static final String MODID = "radium";

    public static LithiumConfig CONFIG;

    public LithiumMod() {
        if (CONFIG == null) {
            throw new IllegalStateException("The mixin plugin did not initialize the config! Did it not load?");
        }
    }
}