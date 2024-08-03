package net.minecraft.client.renderer.debug;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;

public class WorldGenAttemptRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final List<BlockPos> toRender = Lists.newArrayList();

    private final List<Float> scales = Lists.newArrayList();

    private final List<Float> alphas = Lists.newArrayList();

    private final List<Float> reds = Lists.newArrayList();

    private final List<Float> greens = Lists.newArrayList();

    private final List<Float> blues = Lists.newArrayList();

    public void addPos(BlockPos blockPos0, float float1, float float2, float float3, float float4, float float5) {
        this.toRender.add(blockPos0);
        this.scales.add(float1);
        this.alphas.add(float5);
        this.reds.add(float2);
        this.greens.add(float3);
        this.blues.add(float4);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        VertexConsumer $$5 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
        for (int $$6 = 0; $$6 < this.toRender.size(); $$6++) {
            BlockPos $$7 = (BlockPos) this.toRender.get($$6);
            Float $$8 = (Float) this.scales.get($$6);
            float $$9 = $$8 / 2.0F;
            LevelRenderer.addChainedFilledBoxVertices(poseStack0, $$5, (double) ((float) $$7.m_123341_() + 0.5F - $$9) - double2, (double) ((float) $$7.m_123342_() + 0.5F - $$9) - double3, (double) ((float) $$7.m_123343_() + 0.5F - $$9) - double4, (double) ((float) $$7.m_123341_() + 0.5F + $$9) - double2, (double) ((float) $$7.m_123342_() + 0.5F + $$9) - double3, (double) ((float) $$7.m_123343_() + 0.5F + $$9) - double4, (Float) this.reds.get($$6), (Float) this.greens.get($$6), (Float) this.blues.get($$6), (Float) this.alphas.get($$6));
        }
    }
}