package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class LightDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private static final int MAX_RENDER_DIST = 10;

    public LightDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Level $$5 = this.minecraft.level;
        BlockPos $$6 = BlockPos.containing(double2, double3, double4);
        LongSet $$7 = new LongOpenHashSet();
        for (BlockPos $$8 : BlockPos.betweenClosed($$6.offset(-10, -10, -10), $$6.offset(10, 10, 10))) {
            int $$9 = $$5.m_45517_(LightLayer.SKY, $$8);
            float $$10 = (float) (15 - $$9) / 15.0F * 0.5F + 0.16F;
            int $$11 = Mth.hsvToRgb($$10, 0.9F, 0.9F);
            long $$12 = SectionPos.blockToSection($$8.asLong());
            if ($$7.add($$12)) {
                DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, $$5.m_7726_().getLightEngine().getDebugData(LightLayer.SKY, SectionPos.of($$12)), (double) SectionPos.sectionToBlockCoord(SectionPos.x($$12), 8), (double) SectionPos.sectionToBlockCoord(SectionPos.y($$12), 8), (double) SectionPos.sectionToBlockCoord(SectionPos.z($$12), 8), 16711680, 0.3F);
            }
            if ($$9 != 15) {
                DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, String.valueOf($$9), (double) $$8.m_123341_() + 0.5, (double) $$8.m_123342_() + 0.25, (double) $$8.m_123343_() + 0.5, $$11);
            }
        }
    }
}