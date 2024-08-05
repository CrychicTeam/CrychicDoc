package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

public class WaterDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    public WaterDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        BlockPos $$5 = this.minecraft.player.m_20183_();
        LevelReader $$6 = this.minecraft.player.m_9236_();
        for (BlockPos $$7 : BlockPos.betweenClosed($$5.offset(-10, -10, -10), $$5.offset(10, 10, 10))) {
            FluidState $$8 = $$6.m_6425_($$7);
            if ($$8.is(FluidTags.WATER)) {
                double $$9 = (double) ((float) $$7.m_123342_() + $$8.getHeight($$6, $$7));
                DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, new AABB((double) ((float) $$7.m_123341_() + 0.01F), (double) ((float) $$7.m_123342_() + 0.01F), (double) ((float) $$7.m_123343_() + 0.01F), (double) ((float) $$7.m_123341_() + 0.99F), $$9, (double) ((float) $$7.m_123343_() + 0.99F)).move(-double2, -double3, -double4), 0.0F, 1.0F, 0.0F, 0.15F);
            }
        }
        for (BlockPos $$10 : BlockPos.betweenClosed($$5.offset(-10, -10, -10), $$5.offset(10, 10, 10))) {
            FluidState $$11 = $$6.m_6425_($$10);
            if ($$11.is(FluidTags.WATER)) {
                DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, String.valueOf($$11.getAmount()), (double) $$10.m_123341_() + 0.5, (double) ((float) $$10.m_123342_() + $$11.getHeight($$6, $$10)), (double) $$10.m_123343_() + 0.5, -16777216);
            }
        }
    }
}