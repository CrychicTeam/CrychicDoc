package net.minecraft.client.renderer.debug;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Collections;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CollisionBoxRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private double lastUpdateTime = Double.MIN_VALUE;

    private List<VoxelShape> shapes = Collections.emptyList();

    public CollisionBoxRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        double $$5 = (double) Util.getNanos();
        if ($$5 - this.lastUpdateTime > 1.0E8) {
            this.lastUpdateTime = $$5;
            Entity $$6 = this.minecraft.gameRenderer.getMainCamera().getEntity();
            this.shapes = ImmutableList.copyOf($$6.level().m_186431_($$6, $$6.getBoundingBox().inflate(6.0)));
        }
        VertexConsumer $$7 = multiBufferSource1.getBuffer(RenderType.lines());
        for (VoxelShape $$8 : this.shapes) {
            LevelRenderer.renderVoxelShape(poseStack0, $$7, $$8, -double2, -double3, -double4, 1.0F, 1.0F, 1.0F, 1.0F, true);
        }
    }
}