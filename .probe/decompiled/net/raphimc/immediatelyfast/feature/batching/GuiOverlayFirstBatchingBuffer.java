package net.raphimc.immediatelyfast.feature.batching;

import net.minecraft.client.renderer.RenderType;

public class GuiOverlayFirstBatchingBuffer extends BatchingBuffer {

    @Override
    public void endBatch() {
        this.drawFallbackLayersFirst = false;
        this.m_109912_(RenderType.guiOverlay());
        super.m_109911_();
    }
}