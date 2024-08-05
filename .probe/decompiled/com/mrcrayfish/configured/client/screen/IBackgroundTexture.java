package com.mrcrayfish.configured.client.screen;

import net.minecraft.resources.ResourceLocation;

public interface IBackgroundTexture {

    ResourceLocation getBackgroundTexture();

    static ResourceLocation loadTexture(Object object, ResourceLocation original) {
        return object instanceof IBackgroundTexture ? ((IBackgroundTexture) object).getBackgroundTexture() : original;
    }
}