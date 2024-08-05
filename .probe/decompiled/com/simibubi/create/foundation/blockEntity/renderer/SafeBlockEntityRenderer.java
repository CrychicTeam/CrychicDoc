package com.simibubi.create.foundation.blockEntity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class SafeBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    @Override
    public final void render(T be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (!this.isInvalid(be)) {
            this.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);
        }
    }

    protected abstract void renderSafe(T var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, int var6);

    public boolean isInvalid(T be) {
        return !be.hasLevel() || be.getBlockState().m_60734_() == Blocks.AIR;
    }
}