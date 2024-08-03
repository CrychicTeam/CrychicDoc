package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;

public class GameTestDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final float PADDING = 0.02F;

    private final Map<BlockPos, GameTestDebugRenderer.Marker> markers = Maps.newHashMap();

    public void addMarker(BlockPos blockPos0, int int1, String string2, int int3) {
        this.markers.put(blockPos0, new GameTestDebugRenderer.Marker(int1, string2, Util.getMillis() + (long) int3));
    }

    @Override
    public void clear() {
        this.markers.clear();
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        long $$5 = Util.getMillis();
        this.markers.entrySet().removeIf(p_113517_ -> $$5 > ((GameTestDebugRenderer.Marker) p_113517_.getValue()).removeAtTime);
        this.markers.forEach((p_269737_, p_269738_) -> this.renderMarker(poseStack0, multiBufferSource1, p_269737_, p_269738_));
    }

    private void renderMarker(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2, GameTestDebugRenderer.Marker gameTestDebugRendererMarker3) {
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, blockPos2, 0.02F, gameTestDebugRendererMarker3.getR(), gameTestDebugRendererMarker3.getG(), gameTestDebugRendererMarker3.getB(), gameTestDebugRendererMarker3.getA() * 0.75F);
        if (!gameTestDebugRendererMarker3.text.isEmpty()) {
            double $$4 = (double) blockPos2.m_123341_() + 0.5;
            double $$5 = (double) blockPos2.m_123342_() + 1.2;
            double $$6 = (double) blockPos2.m_123343_() + 0.5;
            DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, gameTestDebugRendererMarker3.text, $$4, $$5, $$6, -1, 0.01F, true, 0.0F, true);
        }
    }

    static class Marker {

        public int color;

        public String text;

        public long removeAtTime;

        public Marker(int int0, String string1, long long2) {
            this.color = int0;
            this.text = string1;
            this.removeAtTime = long2;
        }

        public float getR() {
            return (float) (this.color >> 16 & 0xFF) / 255.0F;
        }

        public float getG() {
            return (float) (this.color >> 8 & 0xFF) / 255.0F;
        }

        public float getB() {
            return (float) (this.color & 0xFF) / 255.0F;
        }

        public float getA() {
            return (float) (this.color >> 24 & 0xFF) / 255.0F;
        }
    }
}