package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SquidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.GlowSquid;

public class GlowSquidRenderer extends SquidRenderer<GlowSquid> {

    private static final ResourceLocation GLOW_SQUID_LOCATION = new ResourceLocation("textures/entity/squid/glow_squid.png");

    public GlowSquidRenderer(EntityRendererProvider.Context entityRendererProviderContext0, SquidModel<GlowSquid> squidModelGlowSquid1) {
        super(entityRendererProviderContext0, squidModelGlowSquid1);
    }

    public ResourceLocation getTextureLocation(GlowSquid glowSquid0) {
        return GLOW_SQUID_LOCATION;
    }

    protected int getBlockLightLevel(GlowSquid glowSquid0, BlockPos blockPos1) {
        int $$2 = (int) Mth.clampedLerp(0.0F, 15.0F, 1.0F - (float) glowSquid0.getDarkTicksRemaining() / 10.0F);
        return $$2 == 15 ? 15 : Math.max($$2, super.m_6086_(glowSquid0, blockPos1));
    }
}