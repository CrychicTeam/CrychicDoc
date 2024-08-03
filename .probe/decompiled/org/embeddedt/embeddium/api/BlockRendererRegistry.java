package org.embeddedt.embeddium.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import net.minecraft.util.RandomSource;

public class BlockRendererRegistry {

    private static final BlockRendererRegistry instance = new BlockRendererRegistry();

    private final List<BlockRendererRegistry.RenderPopulator> renderPopulators = new CopyOnWriteArrayList();

    private BlockRendererRegistry() {
    }

    public static BlockRendererRegistry instance() {
        return instance;
    }

    public void registerRenderPopulator(BlockRendererRegistry.RenderPopulator populator) {
        this.renderPopulators.add(populator);
    }

    public void fillCustomRenderers(List<BlockRendererRegistry.Renderer> resultList, BlockRenderContext context) {
        if (!this.renderPopulators.isEmpty()) {
            for (BlockRendererRegistry.RenderPopulator populator : this.renderPopulators) {
                populator.fillCustomRenderers(resultList, context);
            }
        }
    }

    public interface RenderPopulator {

        void fillCustomRenderers(List<BlockRendererRegistry.Renderer> var1, BlockRenderContext var2);

        static BlockRendererRegistry.RenderPopulator forRenderer(BlockRendererRegistry.Renderer renderer) {
            return (resultList, ctx) -> resultList.add(renderer);
        }
    }

    public static enum RenderResult {

        OVERRIDE, PASS
    }

    public interface Renderer {

        BlockRendererRegistry.RenderResult renderBlock(BlockRenderContext var1, RandomSource var2, VertexConsumer var3);
    }
}