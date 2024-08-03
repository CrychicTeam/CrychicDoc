package de.keksuccino.fancymenu.util.resource;

import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RenderableResource extends Resource {

    ResourceLocation MISSING_TEXTURE_LOCATION = TextureManager.INTENTIONAL_MISSING_TEXTURE;

    ResourceLocation FULLY_TRANSPARENT_TEXTURE = new ResourceLocation("fancymenu", "textures/fully_transparent.png");

    @Nullable
    ResourceLocation getResourceLocation();

    int getWidth();

    int getHeight();

    @NotNull
    AspectRatio getAspectRatio();

    void reset();
}