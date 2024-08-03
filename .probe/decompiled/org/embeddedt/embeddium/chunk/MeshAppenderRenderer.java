package org.embeddedt.embeddium.chunk;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.Entry;
import java.util.List;
import me.jellysquid.mods.sodium.client.compat.ccl.SinkingVertexBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.embeddedt.embeddium.api.MeshAppender;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class MeshAppenderRenderer {

    private static final Vector3fc ZERO = new Vector3f();

    private static final ThreadLocal<Reference2ReferenceOpenHashMap<RenderType, SinkingVertexBuilder>> BUILDERS = ThreadLocal.withInitial(() -> {
        Reference2ReferenceOpenHashMap<RenderType, SinkingVertexBuilder> map = new Reference2ReferenceOpenHashMap();
        for (RenderType layer : RenderType.chunkBufferLayers()) {
            map.put(layer, new SinkingVertexBuilder());
        }
        return map;
    });

    public static void renderMeshAppenders(List<MeshAppender> appenders, BlockAndTintGetter world, SectionPos origin, ChunkBuildBuffers buffers) {
        if (!appenders.isEmpty()) {
            Reference2ReferenceOpenHashMap<RenderType, SinkingVertexBuilder> builders = (Reference2ReferenceOpenHashMap<RenderType, SinkingVertexBuilder>) BUILDERS.get();
            ObjectIterator<Entry<RenderType, SinkingVertexBuilder>> it = builders.reference2ReferenceEntrySet().fastIterator();
            while (it.hasNext()) {
                Entry<RenderType, SinkingVertexBuilder> entry = (Entry<RenderType, SinkingVertexBuilder>) it.next();
                ((SinkingVertexBuilder) entry.getValue()).reset();
            }
            MeshAppender.Context context = new MeshAppender.Context(builders::get, world, origin, buffers);
            for (MeshAppender appender : appenders) {
                appender.render(context);
            }
            ObjectIterator<Entry<RenderType, SinkingVertexBuilder>> itx = builders.reference2ReferenceEntrySet().fastIterator();
            while (itx.hasNext()) {
                Entry<RenderType, SinkingVertexBuilder> entry = (Entry<RenderType, SinkingVertexBuilder>) itx.next();
                Material material = DefaultMaterials.forRenderLayer((RenderType) entry.getKey());
                ((SinkingVertexBuilder) entry.getValue()).flush(buffers.get(material), material, ZERO);
            }
        }
    }
}