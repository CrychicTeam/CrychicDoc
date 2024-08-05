package net.minecraft.client.renderer.debug;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleSupplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.CollisionContext;

public class SupportBlockRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private double lastUpdateTime = Double.MIN_VALUE;

    private List<Entity> surroundEntities = Collections.emptyList();

    public SupportBlockRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        double $$5 = (double) Util.getNanos();
        if ($$5 - this.lastUpdateTime > 1.0E8) {
            this.lastUpdateTime = $$5;
            Entity $$6 = this.minecraft.gameRenderer.getMainCamera().getEntity();
            this.surroundEntities = ImmutableList.copyOf($$6.level().m_45933_($$6, $$6.getBoundingBox().inflate(16.0)));
        }
        Player $$7 = this.minecraft.player;
        if ($$7 != null && $$7.f_285638_.isPresent()) {
            this.drawHighlights(poseStack0, multiBufferSource1, double2, double3, double4, $$7, () -> 0.0, 1.0F, 0.0F, 0.0F);
        }
        for (Entity $$8 : this.surroundEntities) {
            if ($$8 != $$7) {
                this.drawHighlights(poseStack0, multiBufferSource1, double2, double3, double4, $$8, () -> this.getBias($$8), 0.0F, 1.0F, 0.0F);
            }
        }
    }

    private void drawHighlights(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4, Entity entity5, DoubleSupplier doubleSupplier6, float float7, float float8, float float9) {
        entity5.mainSupportingBlockPos.ifPresent(p_286428_ -> {
            double $$11 = doubleSupplier6.getAsDouble();
            BlockPos $$12 = entity5.getOnPos();
            this.highlightPosition($$12, poseStack0, double2, double3, double4, multiBufferSource1, 0.02 + $$11, float7, float8, float9);
            BlockPos $$13 = entity5.getOnPosLegacy();
            if (!$$13.equals($$12)) {
                this.highlightPosition($$13, poseStack0, double2, double3, double4, multiBufferSource1, 0.04 + $$11, 0.0F, 1.0F, 1.0F);
            }
        });
    }

    private double getBias(Entity entity0) {
        return 0.02 * (double) (String.valueOf((double) entity0.getId() + 0.132453657).hashCode() % 1000) / 1000.0;
    }

    private void highlightPosition(BlockPos blockPos0, PoseStack poseStack1, double double2, double double3, double double4, MultiBufferSource multiBufferSource5, double double6, float float7, float float8, float float9) {
        double $$10 = (double) blockPos0.m_123341_() - double2 - 2.0 * double6;
        double $$11 = (double) blockPos0.m_123342_() - double3 - 2.0 * double6;
        double $$12 = (double) blockPos0.m_123343_() - double4 - 2.0 * double6;
        double $$13 = $$10 + 1.0 + 4.0 * double6;
        double $$14 = $$11 + 1.0 + 4.0 * double6;
        double $$15 = $$12 + 1.0 + 4.0 * double6;
        LevelRenderer.renderLineBox(poseStack1, multiBufferSource5.getBuffer(RenderType.lines()), $$10, $$11, $$12, $$13, $$14, $$15, float7, float8, float9, 0.4F);
        LevelRenderer.renderVoxelShape(poseStack1, multiBufferSource5.getBuffer(RenderType.lines()), this.minecraft.level.m_8055_(blockPos0).m_60742_(this.minecraft.level, blockPos0, CollisionContext.empty()).move((double) blockPos0.m_123341_(), (double) blockPos0.m_123342_(), (double) blockPos0.m_123343_()), -double2, -double3, -double4, float7, float8, float9, 1.0F, false);
    }
}