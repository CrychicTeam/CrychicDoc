package org.embeddedt.embeddium.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;

public interface MeshAppender {

    void render(MeshAppender.Context var1);

    public static record Context(Function<RenderType, VertexConsumer> vertexConsumerProvider, BlockAndTintGetter blockRenderView, SectionPos sectionOrigin, ChunkBuildBuffers sodiumBuildBuffers) {
    }
}