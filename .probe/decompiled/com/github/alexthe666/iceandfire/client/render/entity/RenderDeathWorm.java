package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderDeathWorm extends MobRenderer<EntityDeathWorm, ModelDeathWorm> {

    public static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_red.png");

    public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_white.png");

    public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_yellow.png");

    public RenderDeathWorm(EntityRendererProvider.Context context) {
        super(context, new ModelDeathWorm(), 0.0F);
    }

    protected void scale(EntityDeathWorm entity, PoseStack matrixStackIn, float partialTickTime) {
        this.f_114477_ = entity.getScale() / 3.0F;
        matrixStackIn.scale(entity.getScale(), entity.getScale(), entity.getScale());
    }

    protected int getBlockLightLevel(EntityDeathWorm entityIn, @NotNull BlockPos partialTicks) {
        return entityIn.m_6060_() ? 15 : entityIn.getWormBrightness(false);
    }

    protected int getSkyLightLevel(EntityDeathWorm entity, @NotNull BlockPos pos) {
        return entity.getWormBrightness(true);
    }

    @Nullable
    public ResourceLocation getTextureLocation(EntityDeathWorm entity) {
        return entity.getVariant() == 2 ? TEXTURE_WHITE : (entity.getVariant() == 1 ? TEXTURE_RED : TEXTURE_YELLOW);
    }
}