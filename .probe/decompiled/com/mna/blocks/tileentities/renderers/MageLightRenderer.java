package com.mna.blocks.tileentities.renderers;

import com.mna.api.config.ClientConfigValues;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.MagelightTile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;

public class MageLightRenderer implements BlockEntityRenderer<MagelightTile> {

    public MageLightRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(MagelightTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!Minecraft.getInstance().isPaused()) {
            this.spawnGlowParticles(tileEntityIn);
        }
    }

    private void spawnGlowParticles(MagelightTile tileEntityIn) {
        Level level = tileEntityIn.m_58904_();
        BlockPos worldPosition = tileEntityIn.m_58899_();
        float offsetScale = 0.25F;
        float offset = level.random.nextFloat();
        offset *= offsetScale;
        float flitChance = ClientConfigValues.FancyMagelights ? 0.3F : 0.1F;
        float centerChance = ClientConfigValues.FancyMagelights ? 1.0F : 0.5F;
        int color = tileEntityIn.getColor();
        int[] colors = null;
        if (color != -1) {
            colors = new int[] { FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color) };
        }
        if (Math.random() < (double) flitChance) {
            MAParticleType particle = new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
            if (color != -1) {
                particle.setColor(colors[0], colors[1], colors[2]);
            }
            level.addParticle(particle, (double) worldPosition.m_123341_() + 0.5 + (double) (-offset) + level.random.nextDouble() * (double) offset * 2.0, (double) worldPosition.m_123342_() + 0.5 + (double) (-offset) + level.random.nextDouble() * (double) offset * 2.0, (double) worldPosition.m_123343_() + 0.5 + (double) (-offset) + level.random.nextDouble() * (double) offset * 2.0, 0.0, 0.05F, 0.0);
        }
        if (Math.random() < (double) centerChance) {
            MAParticleType particle = new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
            if (color != -1) {
                particle.setColor(colors[0], colors[1], colors[2]);
            }
            level.addParticle(particle, (double) worldPosition.m_123341_() + 0.5, (double) worldPosition.m_123342_() + 0.5, (double) worldPosition.m_123343_() + 0.5, 0.0, 0.0, 0.0);
        }
    }
}