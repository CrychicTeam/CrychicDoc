package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import org.jetbrains.annotations.NotNull;

public class RenderDreadSpawner<T extends TileEntityDreadSpawner> implements BlockEntityRenderer<T> {

    public RenderDreadSpawner(BlockEntityRendererProvider.Context context) {
    }

    public void render(TileEntityDreadSpawner tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.0, 0.5);
        BaseSpawner abstractspawner = tileEntityIn.getSpawner();
        Entity entity = abstractspawner.getOrCreateDisplayEntity(tileEntityIn.m_58904_(), RandomSource.create(), tileEntityIn.m_58899_());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0) {
                f /= f1;
            }
            matrixStackIn.translate(0.0, 0.4F, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp((double) partialTicks, abstractspawner.getoSpin(), abstractspawner.getSpin()) * 10.0F));
            matrixStackIn.translate(0.0, -0.2F, 0.0);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-30.0F));
            matrixStackIn.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        }
        matrixStackIn.popPose();
    }
}