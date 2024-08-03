package com.github.alexthe666.alexsmobs.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;

public class AMBannerRegistry {

    public static final DeferredRegister<BannerPattern> DEF_REG = DeferredRegister.create(Registries.BANNER_PATTERN, "alexsmobs");

    static {
        DEF_REG.register("bear", () -> new BannerPattern("bear"));
        DEF_REG.register("australia_0", () -> new BannerPattern("australia_0"));
        DEF_REG.register("australia_1", () -> new BannerPattern("australia_1"));
        DEF_REG.register("new_mexico", () -> new BannerPattern("new_mexico"));
        DEF_REG.register("brazil", () -> new BannerPattern("brazil"));
    }
}