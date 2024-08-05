package net.minecraft.client.renderer.debug;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;

public class RaidDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final int MAX_RENDER_DIST = 160;

    private static final float TEXT_SCALE = 0.04F;

    private final Minecraft minecraft;

    private Collection<BlockPos> raidCenters = Lists.newArrayList();

    public RaidDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void setRaidCenters(Collection<BlockPos> collectionBlockPos0) {
        this.raidCenters = collectionBlockPos0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        BlockPos $$5 = this.getCamera().getBlockPosition();
        for (BlockPos $$6 : this.raidCenters) {
            if ($$5.m_123314_($$6, 160.0)) {
                highlightRaidCenter(poseStack0, multiBufferSource1, $$6);
            }
        }
    }

    private static void highlightRaidCenter(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2) {
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2.offset(-1, -1, -1), blockPos2.offset(1, 1, 1), 1.0F, 0.0F, 0.0F, 0.15F);
        int $$3 = -65536;
        renderTextOverBlock(poseStack0, multiBufferSource1, "Raid center", blockPos2, -65536);
    }

    private static void renderTextOverBlock(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, BlockPos blockPos3, int int4) {
        double $$5 = (double) blockPos3.m_123341_() + 0.5;
        double $$6 = (double) blockPos3.m_123342_() + 1.3;
        double $$7 = (double) blockPos3.m_123343_() + 0.5;
        DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, string2, $$5, $$6, $$7, int4, 0.04F, true, 0.0F, true);
    }

    private Camera getCamera() {
        return this.minecraft.gameRenderer.getMainCamera();
    }
}