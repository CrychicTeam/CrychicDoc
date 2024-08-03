package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelFly;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderFly extends MobRenderer<EntityFly, ModelFly> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/fly.png");

    public RenderFly(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelFly(), 0.2F);
    }

    protected void scale(EntityFly entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    protected boolean isShaking(EntityFly fly) {
        return fly.isInNether();
    }

    protected void setupRotations(EntityFly entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (this.isShaking(entityLiving)) {
            rotationYaw += (float) (Math.cos((double) entityLiving.f_19797_ * 7.0) * Math.PI * 0.9F);
            float vibrate = 0.05F;
            matrixStackIn.translate((entityLiving.m_217043_().nextFloat() - 0.5F) * vibrate, (entityLiving.m_217043_().nextFloat() - 0.5F) * vibrate, (entityLiving.m_217043_().nextFloat() - 0.5F) * vibrate);
        }
        super.m_7523_(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    public ResourceLocation getTextureLocation(EntityFly entity) {
        return TEXTURE;
    }
}