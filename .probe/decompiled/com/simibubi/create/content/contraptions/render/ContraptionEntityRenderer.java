package com.simibubi.create.content.contraptions.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ContraptionEntityRenderer<C extends AbstractContraptionEntity> extends EntityRenderer<C> {

    public ContraptionEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(C entity) {
        return null;
    }

    public boolean shouldRender(C entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ) {
        if (entity.getContraption() == null) {
            return false;
        } else if (!entity.isAliveOrStale()) {
            return false;
        } else {
            return !entity.isReadyForRender() ? false : super.shouldRender(entity, clippingHelper, cameraX, cameraY, cameraZ);
        }
    }

    public void render(C entity, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int overlay) {
        super.render(entity, yaw, partialTicks, ms, buffers, overlay);
        Contraption contraption = entity.getContraption();
        if (contraption != null) {
            ContraptionRenderDispatcher.renderFromEntity(entity, contraption, buffers);
        }
    }
}