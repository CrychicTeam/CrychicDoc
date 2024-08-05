package de.keksuccino.konkrete.resources;

import net.minecraft.resources.ResourceLocation;

public interface ITextureResourceLocation {

    void loadTexture();

    ResourceLocation getResourceLocation();

    boolean isReady();

    int getHeight();

    int getWidth();
}