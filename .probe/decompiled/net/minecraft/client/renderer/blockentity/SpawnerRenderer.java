package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public class SpawnerRenderer implements BlockEntityRenderer<SpawnerBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public SpawnerRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.entityRenderer = blockEntityRendererProviderContext0.getEntityRenderer();
    }

    public void render(SpawnerBlockEntity spawnerBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        poseStack2.pushPose();
        poseStack2.translate(0.5F, 0.0F, 0.5F);
        BaseSpawner $$6 = spawnerBlockEntity0.getSpawner();
        Entity $$7 = $$6.getOrCreateDisplayEntity(spawnerBlockEntity0.m_58904_(), spawnerBlockEntity0.m_58904_().getRandom(), spawnerBlockEntity0.m_58899_());
        if ($$7 != null) {
            float $$8 = 0.53125F;
            float $$9 = Math.max($$7.getBbWidth(), $$7.getBbHeight());
            if ((double) $$9 > 1.0) {
                $$8 /= $$9;
            }
            poseStack2.translate(0.0F, 0.4F, 0.0F);
            poseStack2.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp((double) float1, $$6.getoSpin(), $$6.getSpin()) * 10.0F));
            poseStack2.translate(0.0F, -0.2F, 0.0F);
            poseStack2.mulPose(Axis.XP.rotationDegrees(-30.0F));
            poseStack2.scale($$8, $$8, $$8);
            this.entityRenderer.render($$7, 0.0, 0.0, 0.0, 0.0F, float1, poseStack2, multiBufferSource3, int4);
        }
        poseStack2.popPose();
    }
}