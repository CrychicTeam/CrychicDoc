package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.WitherArmorLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class WitherBossRenderer extends MobRenderer<WitherBoss, WitherBossModel<WitherBoss>> {

    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");

    private static final ResourceLocation WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");

    public WitherBossRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new WitherBossModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.WITHER)), 1.0F);
        this.m_115326_(new WitherArmorLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    protected int getBlockLightLevel(WitherBoss witherBoss0, BlockPos blockPos1) {
        return 15;
    }

    public ResourceLocation getTextureLocation(WitherBoss witherBoss0) {
        int $$1 = witherBoss0.getInvulnerableTicks();
        return $$1 > 0 && ($$1 > 80 || $$1 / 5 % 2 != 1) ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }

    protected void scale(WitherBoss witherBoss0, PoseStack poseStack1, float float2) {
        float $$3 = 2.0F;
        int $$4 = witherBoss0.getInvulnerableTicks();
        if ($$4 > 0) {
            $$3 -= ((float) $$4 - float2) / 220.0F * 0.5F;
        }
        poseStack1.scale($$3, $$3, $$3);
    }
}