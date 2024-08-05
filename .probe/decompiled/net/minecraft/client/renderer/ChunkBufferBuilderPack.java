package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Map;
import java.util.stream.Collectors;

public class ChunkBufferBuilderPack {

    private final Map<RenderType, BufferBuilder> builders = (Map<RenderType, BufferBuilder>) RenderType.chunkBufferLayers().stream().collect(Collectors.toMap(p_108845_ -> p_108845_, p_108843_ -> new BufferBuilder(p_108843_.bufferSize())));

    public BufferBuilder builder(RenderType renderType0) {
        return (BufferBuilder) this.builders.get(renderType0);
    }

    public void clearAll() {
        this.builders.values().forEach(BufferBuilder::m_85729_);
    }

    public void discardAll() {
        this.builders.values().forEach(BufferBuilder::m_85730_);
    }
}