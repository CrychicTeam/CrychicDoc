package com.mna.api.tools;

import net.minecraft.resources.ResourceLocation;

public class RLoc {

    public static ResourceLocation create(String path) {
        return new ResourceLocation("mna", path);
    }
}