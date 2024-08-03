package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Map;
import net.minecraft.client.renderer.RenderType;
import net.raphimc.immediatelyfast.feature.core.BatchableBufferSource;

public class BatchingBuffer extends BatchableBufferSource {

    public static boolean IS_DRAWING;

    public BatchingBuffer() {
    }

    public BatchingBuffer(Map<RenderType, BufferBuilder> layerBuffers) {
        super(layerBuffers);
    }

    public BatchingBuffer(BufferBuilder fallbackBuffer, Map<RenderType, BufferBuilder> layerBuffers) {
        super(fallbackBuffer, layerBuffers);
    }

    @Override
    public void endBatch(RenderType layer) {
        try {
            IS_DRAWING = true;
            super.endBatch(layer);
        } finally {
            IS_DRAWING = false;
        }
    }
}