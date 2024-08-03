package org.violetmoon.quark.content.building.client.render.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.building.entity.Stool;

public class StoolEntityRenderer extends EntityRenderer<Stool> {

    public StoolEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull Stool entity) {
        return null;
    }

    public boolean shouldRender(@NotNull Stool livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        return false;
    }
}